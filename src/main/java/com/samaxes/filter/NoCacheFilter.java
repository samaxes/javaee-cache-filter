/*
 * $Id$
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
package com.samaxes.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Completely disable browser caching.
 * 
 * @author Samuel Santos
 * @version $Revision$
 */
public class NoCacheFilter implements Filter {

    /**
     * Place this filter into service.
     * 
     * @param filterConfig the filter configuration object used by a servlet container to pass information to a filter
     *        during initialization
     * @throws ServletException to inform the container to not place the filter into service
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Set cache header directives.
     * 
     * @param servletRequest provides data including parameter name and values, attributes, and an input stream
     * @param servletResponse assists a servlet in sending a response to the client
     * @param filterChain gives a view into the invocation chain of a filtered request
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // set cache directives
        httpServletResponse.setDateHeader("Expires", 0L);
        httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
    }
}
