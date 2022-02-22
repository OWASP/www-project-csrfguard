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

package org.owasp.csrfguard.config;

import org.owasp.csrfguard.action.IAction;
import org.owasp.csrfguard.token.storage.LogicalSessionExtractor;
import org.owasp.csrfguard.token.storage.TokenHolder;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Interface that enables interaction with configuration providers
 */
public interface ConfigurationProvider {

    /**
     * @return true when this configuration provider can be cached for a minute, i.e. it is all setup
     */
    boolean isCacheable();

    /**
     * @return true if the display of the CSRF configuration at start-up was requested, false otherwise
     */
    boolean isPrintConfig();

    /**
     * @return the name of the CSRF token, used in the DOM
     */
    String getTokenName();

    /**
     * If csrf guard filter should check even if there is no session for the user
     * Note: this changed around 2014/04, the default behavior used to be to
     * not check if there is no session.  If you want the legacy behavior (if your app
     * is not susceptible to CSRF if the user has no session), set this to false
     *
     * @return true when validation is performed even when no session exists
     */
    boolean isValidateWhenNoSessionExists();

    /**
     * This parameter controls how long a generated token should be.
     * @return the configured length of the token
     */
    int getTokenLength();

    /**
     * @return true if token rotation was configured, false otherwise
     */
    boolean isRotateEnabled();

    /**
     * @return true if token-per-page was configured, false otherwise
     */
    boolean isTokenPerPageEnabled();

    /**
     * @return true if pre-generation of page tokens has been configured, false otherwise
     */
    boolean isTokenPerPagePrecreateEnabled();

    /**
     * @return the pseudo-random number generator instance, which is used to generate CSRF tokens
     */
    SecureRandom getPrng();

    /**
     * @return the path of a page to which a new user (with no logical session) will be redirected
     */
    String getNewTokenLandingPage();

    /**
     * @return true if new users (without a logical session) should be redirected to a pre-configured page
     * @see ConfigurationProvider#getNewTokenLandingPage()
     */
    boolean isUseNewTokenLandingPage();

    /**
     * @return true if Asynchronous JavaScript And XML (AJAX) support was configured, false otherwise
     */
    boolean isAjaxEnabled();

    /**
     * The default behavior of CSRFGuard is to protect all pages. Pages marked as unprotected will not be protected.<br>
     * If the Protect property is enabled, this behavior is reversed. Pages must be marked as protected to be protected.
     * All other pages will not be protected. This is useful when the CsrfGuardFilter is aggressively mapped (ex: /*),
     * but you only want to protect a few pages.
     *
     * @return false if all pages are protected, true if pages are required to be explicit protected
     */
    boolean isProtectEnabled();

    /**
     * @return whether the legacy Synchronous AJAX requests are enabled
     */
    boolean isForceSynchronousAjax();

    /**
     * @return the configured set of all protected pages
     */
    Set<String> getProtectedPages();

    /**
     * @return the configured set of all un-protected pages
     */
    Set<String> getUnprotectedPages();

    /**
     * @return the configured set of protected HTTP methods (verbs)
     */
    Set<String> getProtectedMethods();

    /**
     * if there are methods here, then all other HTTP methods are protected and these (e.g. GET) are unprotected
     *
     * @return the unprotected methods
     */
    Set<String> getUnprotectedMethods();

    /**
     * if the filter is enabled
     *
     * @return is csrf guard filter is enabled
     */
    boolean isEnabled();

    /**
     * @return the configured list of actions to be executed in case of a potential CSRF attack
     */
    List<IAction> getActions();

    /**
     * @return the overridden path to the configured CSRFGuard JavaScript logic, or <b>null</b> if the default is used
     */
    String getJavascriptSourceFile();

    /**
     * @return true if tokens should only be injected into links that have the same domain from which the HTML originates,
     * false if subdomains are also permitted
     */
    boolean isJavascriptDomainStrict();

    /**
     * TODO Currently not configurable through the properties!
     *
     * @return the configured domain, whose resources are intended be decorated with CSRF tokens
     */
    String getDomainOrigin();

    /**
     * @return the configured JavaScript cache control
     */
    String getJavascriptCacheControl();

    /**
     * @return the configured JavaScript "Referer" pattern to be used
     */
    Pattern getJavascriptRefererPattern();

    /**
     * JavaScript configuration parameters can be set/overwritten via the servlet configuration.
     * This method is intended to trigger the initialization of the JavaScript parameters, if/after the JavaScript servlet is initialized.
     */
    void initializeJavaScriptConfiguration();

    /**
     * if the token should be injected in GET forms (which will be on the URL)
     * if the HTTP method GET is unprotected, then this should likely be false
     *
     *  @return true if the token should be injected in GET forms via Javascript
     */
    boolean isJavascriptInjectGetForms();

    /**
     * if the token should be injected in the action in forms
     * note, if injectIntoForms is true, then this might not need to be true
     *
     * @return if inject
     */
    boolean isJavascriptInjectFormAttributes();

    /**
     * @return true if injecting tokens into JavaScript forms was configured
     */
    boolean isJavascriptInjectIntoForms();

    /**
     * if the referer to the javascript must match the protocol of the domain
     *
     * @return true if the javascript must match the protocol of the domain
     */
    boolean isJavascriptRefererMatchProtocol();

    /**
     * if the referer to the javascript must match domain
     *
     *  @return true if the javascript must match domain
     */
    boolean isJavascriptRefererMatchDomain();

    /**
     * @return true if injecting tokens into HTML attributes was configured
     */
    boolean isJavascriptInjectIntoAttributes();

    /**
     * @return true if injecting tokens into dynamically injected DOM nodes was configured
     */
    boolean isJavascriptInjectIntoDynamicallyCreatedNodes();

    /**
     * @return the name of the JavaScript dynamic node creation event, if the functionality was configured
     */
    String getJavascriptDynamicNodeCreationEventName();

    /**
     * TODO document
     *
     * @return the configured value of the "X-Requested-With" header
     */
    String getJavascriptXrequestedWith();

    /**
     * @return the content of the template JavaScript code, on which the JavaScript configurations will be applied
     */
    String getJavascriptTemplateCode();

    /**
     * example: "js,css,gif,png,ico,jpg"
     *
     * @return the configured list of un-protected, comma separated extensions
     */
    String getJavascriptUnprotectedExtensions();

    /**
     * @return the configured TokenHolder instance
     */
    TokenHolder getTokenHolder();

    /**
     * @return the configured LogicalSessionExtractor
     */
    LogicalSessionExtractor getLogicalSessionExtractor();

    /**
     * @return the configured page token synchronization tolerance
     */
    Duration getPageTokenSynchronizationTolerance();
}
