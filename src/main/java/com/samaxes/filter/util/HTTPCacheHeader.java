/*
 * Java EE Cache Filter
 * https://github.com/samaxes/javaee-cache-filter
 *
 * Copyright 2011 samaxes.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samaxes.filter.util;

/**
 * Cache HTTP response headers.
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.1.0
 */
public enum HTTPCacheHeader {
    /**
     * The Cache-Control general-header field is used to specify directives that MUST be obeyed by all caching
     * mechanisms along the request/response chain.
     */
    CACHE_CONTROL("Cache-Control"),
    /**
     * The Expires entity-header field gives the date/time after which the response is considered stale.
     */
    EXPIRES("Expires"),
    /**
     * The Pragma general-header field is used to include implementation- specific directives that might apply to any
     * recipient along the request/response chain.
     */
    PRAGMA("Pragma"),
    /**
     * The ETag response-header field provides the current value of the entity tag for the requested variant.
     */
    ETAG("ETag");

    private final String name;

    private HTTPCacheHeader(String name) {
        this.name = name;
    }

    /**
     * Gets the parameter name.
     *
     * @return the parameter name
     */
    public String getName() {
        return this.name;
    }
}
