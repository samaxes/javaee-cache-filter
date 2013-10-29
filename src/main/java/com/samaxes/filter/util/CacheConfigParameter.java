/*
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
 * Enumeration of the possible configuration parameters.
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.1.0
 */
public enum CacheConfigParameter {
    /**
     * Defines whether a component is static or not.
     */
    STATIC("static"),
    /**
     * Cache directive to control where the response may be cached.
     */
    PRIVATE("private"),
    /**
     * Cache directive to set an expiration date relative to the current date.
     */
    EXPIRATION_TIME("expirationTime");

    private final String name;

    private CacheConfigParameter(String name) {
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
