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
 * Possible Cache-Control directives for cacheable responses.
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.3.1
 */
public enum Cacheability {
    /**
     * Indicates that the response MAY be cached by any cache, even if it would normally be non-cacheable or cacheable
     * only within a non-shared cache.
     */
    PUBLIC("public"),
    /**
     * Indicates that all or part of the response message is intended for a single user and MUST NOT be cached by a
     * shared cache. This allows an origin server to state that the specified parts of the response are intended for
     * only one user and are not a valid response for requests by other users. A private (non-shared) cache MAY cache
     * the response.
     */
    PRIVATE("private");

    private final String value;

    private Cacheability(String value) {
        this.value = value;
    }

    /**
     * Gets the Cache-Control directive value.
     *
     * @return the Cache-Control directive value
     */
    public String getValue() {
        return this.value;
    }
}
