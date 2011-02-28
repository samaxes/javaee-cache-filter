/*
 * $Id$ 
 *
 * Copyright 2008 samaxes.com
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

import com.samaxes.filter.util.CacheParameter;
import com.samaxes.filter.util.Cacheability;

/**
 * Filter responsible for browser caching.
 * 
 * @author Samuel Santos
 * @version $Revision: 25 $
 */
public class CacheFilter implements Filter {

    private static final Long CACHE_DAYS = 3L;

    private Cacheability cacheability;

    private boolean isStatic;

    private long seconds;

    /**
     * Place this filter into service.
     * 
     * @param filterConfig the filter configuration object used by a servlet container to pass information to a filter
     *        during initialization
     * @throws ServletException to inform the container to not place the filter into service
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        cacheability = (Boolean.valueOf(filterConfig.getInitParameter(CacheParameter.PRIVATE.getName()))) ? Cacheability.PRIVATE
                : Cacheability.PUBLIC;
        isStatic = Boolean.valueOf(filterConfig.getInitParameter(CacheParameter.STATIC.getName()));

        try {
            seconds = Long.valueOf(filterConfig.getInitParameter(CacheParameter.EXPIRATION_TIME.getName()));
        } catch (NumberFormatException e) {
            seconds = CACHE_DAYS * 24 * 60 * 60;
            filterConfig.getServletContext().log(
                    "Using default expiration time of " + seconds + " seconds (" + CACHE_DAYS + " days).");
        }
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
        StringBuilder cacheControl = new StringBuilder(cacheability.getValue());

        if (!isStatic) {
            cacheControl.append(", must-revalidate");
        }

        // set cache directives
        httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + seconds * 1000L);
        httpServletResponse.setHeader("Cache-Control", cacheControl.toString());

        /*
         * By default, Tomcat will set headers on any SSL content to deny caching. This will cause downloads to Internet
         * Explorer to fail - http://support.microsoft.com/?id=316431. Setting the Pragma header to null or to an empty
         * string takes care of user-agents implementing HTTP 1.0.
         */
        if (httpServletResponse.containsHeader("Pragma")) {
            httpServletResponse.setHeader("Pragma", null);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
    }
}
