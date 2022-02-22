/*
 * The OWASP CSRFGuard Project, BSD License
 * Copyright (c) 2011, Eric Sheridan (eric@infraredsecurity.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of OWASP nor the names of its contributors may be used
 *        to endorse or promote products derived from this software without specific
 *        prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.owasp.csrfguard;

import org.owasp.csrfguard.http.InterceptRedirectResponse;
import org.owasp.csrfguard.session.LogicalSession;
import org.owasp.csrfguard.token.storage.LogicalSessionExtractor;
import org.owasp.csrfguard.token.transferobject.TokenTO;
import org.owasp.csrfguard.util.CsrfGuardUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class CsrfGuardFilter implements Filter {

    private FilterConfig filterConfig = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfGuardFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        final CsrfGuard csrfGuard = CsrfGuard.getInstance();

        if (csrfGuard.isEnabled()) {
            if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
                doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain, csrfGuard);
            } else {
                handleNonHttpServletMessages(request, response, filterChain);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    private void doFilter(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final FilterChain filterChain, final CsrfGuard csrfGuard) throws IOException, ServletException {
        final InterceptRedirectResponse interceptRedirectResponse = new InterceptRedirectResponse(httpServletResponse, httpServletRequest, csrfGuard);

        final LogicalSessionExtractor sessionKeyExtractor = csrfGuard.getLogicalSessionExtractor();
        final LogicalSession logicalSession = sessionKeyExtractor.extract(httpServletRequest);

        if (logicalSession == null) {
            if (csrfGuard.isUseNewTokenLandingPage()) {
                final LogicalSession createdLogicalSession = sessionKeyExtractor.extractOrCreate(httpServletRequest);
                csrfGuard.writeLandingPage(interceptRedirectResponse, createdLogicalSession.getKey());
            } else {
                handleNoSession(httpServletRequest, httpServletResponse, interceptRedirectResponse, filterChain, csrfGuard);
            }
        } else {
            handleSession(httpServletRequest, interceptRedirectResponse, filterChain, logicalSession, csrfGuard);
        }
    }

    private void handleSession(final HttpServletRequest httpServletRequest, final InterceptRedirectResponse interceptRedirectResponse, final FilterChain filterChain,
                               final LogicalSession logicalSession, final CsrfGuard csrfGuard) throws IOException, ServletException {

        final String logicalSessionKey = logicalSession.getKey();

        if (new CsrfValidator().isValid(httpServletRequest, interceptRedirectResponse)) {
            filterChain.doFilter(httpServletRequest, interceptRedirectResponse);
        } else {
            logInvalidRequest(httpServletRequest);
        }

        final String requestURI = httpServletRequest.getRequestURI();
        final String generatedToken = csrfGuard.getTokenService().generateTokensIfAbsent(logicalSessionKey, httpServletRequest.getMethod(), requestURI);

        CsrfGuardUtils.addResponseTokenHeader(csrfGuard, httpServletRequest, interceptRedirectResponse, new TokenTO(Collections.singletonMap(requestURI, generatedToken)));
    }

    private void handleNoSession(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final InterceptRedirectResponse interceptRedirectResponse, final FilterChain filterChain,
                                 final CsrfGuard csrfGuard) throws IOException, ServletException {
        if (csrfGuard.isValidateWhenNoSessionExists()) {
            if (new CsrfValidator().isValid(httpServletRequest, interceptRedirectResponse)) {
                filterChain.doFilter(httpServletRequest, interceptRedirectResponse);
            } else {
                logInvalidRequest(httpServletRequest);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private void handleNonHttpServletMessages(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        final String message = String.format("CSRFGuard does not know how to work with requests of class %s ", request.getClass().getName());
        LOGGER.warn(message);
        this.filterConfig.getServletContext().log("[WARNING]" + message);

        filterChain.doFilter(request, response);
    }

    private void logInvalidRequest(final HttpServletRequest httpRequest) {
        final String requestURI = httpRequest.getRequestURI();
        final String remoteAddress = httpRequest.getRemoteAddr();

        LOGGER.warn("Invalid request: URI: '{}' | Remote Address: '{}'", requestURI, remoteAddress);
    }
}
