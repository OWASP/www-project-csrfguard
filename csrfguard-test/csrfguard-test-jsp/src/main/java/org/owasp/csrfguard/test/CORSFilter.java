/*
 * The OWASP CSRFGuard Project, BSD License
 * Eric Sheridan (eric@infraredsecurity.com), Copyright (c) 2011
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    3. Neither the name of OWASP nor the names of its contributors may be used
 *       to endorse or promote products derived from this software without specific
 *       prior written permission.
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
package org.owasp.csrfguard.test;

import org.owasp.csrfguard.CsrfGuard;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Enables Cross-Origin Resource Sharing
 * Only for testing purposes.
 * Disabled by default through the web.xml
 */
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");

//        httpResponse.addHeader("Access-Control-Allow-Headers", "*");
            httpResponse.addHeader("Access-Control-Allow-Headers", String.join(",", CsrfGuard.getInstance().getTokenName(),
                                                                               "X-Requested-With"));
        }

        { // Access-Control-Allow-Credentials cannot be set to true if the Access-Control-Allow-Origin is "*"
//        httpResponse.addHeader("Access-Control-Allow-Origin", "http://attacker.local:8080");  // sudo echo "127.0.0.1 attacker.local" >> /etc/hosts
//        httpResponse.addHeader("Access-Control-Allow-Credentials", Boolean.toString(true));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}
