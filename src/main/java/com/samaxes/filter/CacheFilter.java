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

/**
 * Filter responsible for browser caching.
 * 
 * @author : Samuel Santos
 * @version : $Revision: 25 $
 */
public class CacheFilter implements Filter {

    private boolean configured;

    private String privacy;

    private long seconds;

    private long milliseconds;

    /**
     * Place this filter into service.
     * 
     * @param filterConfig {@link FilterConfig}
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        privacy = filterConfig.getInitParameter("privacy");
        String expirationTime = filterConfig.getInitParameter("expirationTime");

        if (privacy != null && !"".equals(privacy) && expirationTime != null && !"".equals(expirationTime)) {
            seconds = Long.valueOf(expirationTime);
            milliseconds = seconds * 1000L;
            configured = true;
        }
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
    }

    /**
     * Sets cache headers directives.
     * 
     * @param servletRequest {@link ServletRequest}
     * @param servletResponse {@link ServletResponse}
     * @param filterChain {@link FilterChain}
     * @throws IOException {@link FilterChain}
     * @throws ServletException {@link ServletException}
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if (httpServletResponse != null && configured) {
            // set the provided HTTP response parameters
            httpServletResponse.setHeader("Cache-Control", privacy + ", max-age=" + seconds + ", must-revalidate");
            httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + milliseconds);
        }

        // pass the request/response on
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
