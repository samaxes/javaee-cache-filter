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
package com.samaxes.cachefilter.presentation;

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
 * @deprecated Please update to the new {@link com.samaxes.filter.CacheFilter CacheFilter} instead.
 */
public class CacheFilter implements Filter {

    private FilterConfig filterConfig;

    /**
     * Place this filter into service.
     * 
     * @param filterConfig {@link FilterConfig}
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
        this.filterConfig = null;
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
        String privacy = filterConfig.getInitParameter("privacy");
        String expirationTime = filterConfig.getInitParameter("expirationTime");

        if (httpServletResponse != null && privacy != null && !"".equals(privacy) && expirationTime != null
                && !"".equals(expirationTime)) {
            long seconds = Long.valueOf(expirationTime);

            // set the provided HTTP response parameters
            httpServletResponse.setHeader("Cache-Control", privacy + ", max-age=" + seconds + ", must-revalidate");
            httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + seconds * 1000L);
        }

        // pass the request/response on
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
