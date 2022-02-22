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

package org.owasp.csrfguard.action;

import org.owasp.csrfguard.CsrfGuard;
import org.owasp.csrfguard.CsrfGuardException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * Interface enabling interaction with Actions, that are invoked in case of a potential CSRF attack
 */
public interface IAction extends Serializable {

	/**
	 * Sets the name of the action
	 *
	 * @param name the name of the action
	 */
	void setName(String name);

	/**
	 * @return the name of the action
	 */
	String getName();

	/**
	 * Sets a parameter with a custom name and value
	 *
	 * @param name the name of the parameter
	 * @param value the value of the parameter
	 */
	void setParameter(String name, String value);

	/**
	 * @param name the name of the parameter
	 * @return the configured parameter based on its name
	 */
	String getParameter(String name);

	/**
	 * @return the whole parameter map
	 */
	Map<String, String> getParameterMap();

	/**
	 * Executes this action.
	 * @param request the HTTP request that triggered a potential CSRF attack
	 * @param response the HTTP response object associated with the potentially malicious HTTP request
	 * @param csrfGuardException the CSRF Guard exception object
	 * @param csrfGuard the main CSRF Guard object, with access to inner workings of the solution
	 *
	 * @throws CsrfGuardException the exception type thrown in case of a potential CSRF attack
	 */
	void execute(HttpServletRequest request, HttpServletResponse response, CsrfGuardException csrfGuardException, CsrfGuard csrfGuard) throws CsrfGuardException;
}
