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
 * Enumeration of the possible configuration parameters.
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.2.0
 */
public enum CacheConfigParameter {
    /**
     * Cache directive to set an expiration time, in seconds, relative to the current date.
     */
    EXPIRATION("expiration"),
    /**
     * Cache directive to control where the response may be cached.
     */
    PRIVATE("private"),
    /**
     * Cache directive to define whether conditional requests are required or not for stale responses.
     */
    MUST_REVALIDATE("must-revalidate");

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
