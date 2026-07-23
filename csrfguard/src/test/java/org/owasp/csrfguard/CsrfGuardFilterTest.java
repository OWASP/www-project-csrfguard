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

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.owasp.csrfguard.session.LogicalSession;
import org.owasp.csrfguard.token.service.TokenService;
import org.owasp.csrfguard.token.storage.LogicalSessionExtractor;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.mockito.Mockito.*;

class CsrfGuardFilterTest {

    private static final String TOKEN_HEADER = "X-CSRF-TOKEN";

    @Test
    void returnsMasterTokenWhenPerPageTokensAreDisabled() throws Exception {
        assertResponseToken(false, "{\"masterToken\":\"csrf-token\",\"pageTokens\":{}}");
    }

    @Test
    void returnsPageTokenWhenPerPageTokensAreEnabled() throws Exception {
        assertResponseToken(true, "{\"pageTokens\":{\"/page/uri\":\"csrf-token\"}}");
    }

    private void assertResponseToken(final boolean tokenPerPageEnabled, final String expectedHeader) throws Exception {
        final CsrfGuard csrfGuard = mock(CsrfGuard.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);
        final LogicalSessionExtractor sessionExtractor = mock(LogicalSessionExtractor.class);
        final LogicalSession logicalSession = mock(LogicalSession.class);
        final TokenService tokenService = mock(TokenService.class);

        when(csrfGuard.isEnabled()).thenReturn(true);
        when(csrfGuard.isProtectEnabled()).thenReturn(true);
        when(csrfGuard.getProtectedPages()).thenReturn(Collections.emptySet());
        when(csrfGuard.getBannedUserAgentProperties()).thenReturn(Collections.emptySet());
        when(csrfGuard.getLogicalSessionExtractor()).thenReturn(sessionExtractor);
        when(csrfGuard.getTokenService()).thenReturn(tokenService);
        when(csrfGuard.isAjaxEnabled()).thenReturn(true);
        when(csrfGuard.isTokenPerPageEnabled()).thenReturn(tokenPerPageEnabled);
        when(csrfGuard.getTokenName()).thenReturn(TOKEN_HEADER);
        when(sessionExtractor.extract(request)).thenReturn(logicalSession);
        when(logicalSession.getKey()).thenReturn("session-key");
        when(tokenService.generateTokensIfAbsent("session-key", "POST", "/page/uri")).thenReturn("csrf-token");
        when(request.getRequestURI()).thenReturn("/page/uri");
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeaders("X-Requested-With")).thenReturn(Collections.enumeration(Collections.singletonList("XMLHttpRequest")));

        try (final MockedStatic<CsrfGuard> csrfGuardMock = mockStatic(CsrfGuard.class)) {
            csrfGuardMock.when(CsrfGuard::getInstance).thenReturn(csrfGuard);

            new CsrfGuardFilter().doFilter(request, response, filterChain);
        }

        verify(response).setHeader(TOKEN_HEADER, expectedHeader);
    }
}
