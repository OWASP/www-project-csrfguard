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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Servlet implementation class HelloServlet
 */
public class CounterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	private static final ConcurrentMap<String, Integer> COUNTER = new ConcurrentHashMap<>();

    private static final String COUNTER_VALUE_MESSAGE = "Counter value: ";
    public static final String INPUT_PARAMETER_NAME = "value";
    public static final String NO_SESSION_MESSAGE = "The request did not contain an associated session!";

    private static final Logger LOGGER = LoggerFactory.getLogger(CounterServlet.class);

    /**
     * Default constructor.
     */
    public CounterServlet() {}

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            respond(response, COUNTER.compute(session.getId(), (k, v) -> Optional.ofNullable(v).orElse(0)));
        } else {
            LOGGER.error(NO_SESSION_MESSAGE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            final String inputValue = request.getParameter(INPUT_PARAMETER_NAME);

            try {
                final int valueToIncreaseBy = Integer.parseInt(inputValue);
                respond(response, COUNTER.compute(session.getId(), (k, v) -> Objects.isNull(v) ? valueToIncreaseBy : v + valueToIncreaseBy));
            } catch (Exception ignored) {}
        } else {
            LOGGER.error(NO_SESSION_MESSAGE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void respond(HttpServletResponse response, int value) throws IOException {
        response.setContentType("text/plain");
        final PrintWriter writer = new PrintWriter(response.getOutputStream());
        writer.println(COUNTER_VALUE_MESSAGE + value);
        writer.close();
    }
}
