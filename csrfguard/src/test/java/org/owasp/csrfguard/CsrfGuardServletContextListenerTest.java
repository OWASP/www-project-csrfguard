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
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.owasp.csrfguard;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.owasp.csrfguard.config.overlay.ConfigurationOverlayProvider;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class CsrfGuardServletContextListenerTest {

	@Test
	void loadsMetaInfPropertiesWhenConfiguredFileIsMissing() {
		final ServletContext context = mock(ServletContext.class);
		when(context.getContextPath()).thenReturn("/");
		when(context.getInitParameter("Owasp.CsrfGuard.Config")).thenReturn("missing-csrfguard-test.properties");
		when(context.getResourceAsStream(ConfigurationOverlayProvider.META_INF_CSRFGUARD_PROPERTIES))
				.thenReturn(new ByteArrayInputStream("test.marker=fallback\n".getBytes(StandardCharsets.UTF_8)));

		try (final MockedStatic<CsrfGuard> csrfGuard = mockStatic(CsrfGuard.class)) {
			csrfGuard.when(CsrfGuard::getInstance).thenReturn(mock(CsrfGuard.class));

			assertDoesNotThrow(() -> new CsrfGuardServletContextListener().contextInitialized(new ServletContextEvent(context)));

			csrfGuard.verify(() -> CsrfGuard.load(argThat((Properties properties) ->
					"fallback".equals(properties.getProperty("test.marker")))));
		}
	}
}
