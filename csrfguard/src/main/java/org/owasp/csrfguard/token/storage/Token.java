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
package org.owasp.csrfguard.token.storage;

import org.owasp.csrfguard.token.storage.impl.PageTokenValue;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Interface used to interact with CSRF tokens
 */
public interface Token {

    /**
     * Returns the master token
     * @return the current master token
     */
    String getMasterToken();

    /**
     * Sets the new master token
     * @param masterToken the new master token
     */
    void setMasterToken(final String masterToken);

    /**
     * @param uri the URI to which the page token should be returned
     * @return the page token for the requested uri
     */
    String getPageToken(final String uri);

    /**
     * @param uri the URI to which the timed page token should be returned
     * @return a timed page token containing a page token and its creation date
     */
    PageTokenValue getTimedPageToken(final String uri);

    /**
     * @param uri the URI to which the page token should be associated
     * @param pageToken the new page token
     */
    void setPageToken(final String uri, final String pageToken);

    /**
     * @param uri the URI to which the page token should be associated
     * @param valueSupplier a supplier that generates new, unique tokens at each invocation
     * @return the newly generated token
     */
    String setPageTokenIfAbsent(final String uri, final Supplier<String> valueSupplier);

    /**
     * @return a map of URIs and their associated page tokens
     */
    Map<String, String> getPageTokens();

    /**
     * Initialize or overwrite the entire page-token map
     * @param pageTokens a map of URIs and their associated page tokens
     */
    void setPageTokens(final Map<String, String> pageTokens);

    /**
     * Rotates all the existing page token values
     * @param tokenValueSupplier a supplier that generates new, unique tokens at each invocation
     */
    void rotateAllPageTokens(final Supplier<String> tokenValueSupplier);

    /**
     * TODO is it worth the added performance penalty in case of a large application with a lot of pages? What would be the risk if this would be contextual to the assigned resource?
     * Disposes the current token from all the stored valid page tokens, disregarding to which resource it was assigned and replaces with a newly generated one.
     * @param tokenFromRequest the current token which needs to be rotated
     * @param tokenValueSupplier a supplier that generates new, unique tokens at each invocation
     */
    void regenerateUsedPageToken(final String tokenFromRequest, final Supplier<String> tokenValueSupplier);
}
