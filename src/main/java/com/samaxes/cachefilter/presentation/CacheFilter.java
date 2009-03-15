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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter responsible for browser caching.
 * 
 * @author : Samuel Santos
 * @version : $Revision: 25 $
 */
public class CacheFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheFilter.class);

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
        LOGGER.debug("Setting cache headers for file {}", ((HttpServletRequest) servletRequest).getRequestURI());

        String privacy = filterConfig.getInitParameter("privacy");
        String expirationTime = filterConfig.getInitParameter("expirationTime");

        if (StringUtils.isNotBlank(privacy) && StringUtils.isNotBlank(expirationTime)) {
            // set the provided HTTP response parameters
            setCacheExpireDate((HttpServletResponse) servletResponse, privacy, Integer.valueOf(expirationTime));
        }

        // pass the request/response on
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setCacheExpireDate(HttpServletResponse response, String privacy, int seconds) {
        if (response != null) {
            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.SECOND, seconds);
            response.setHeader("Cache-Control", privacy + ", max-age=" + seconds + ", must-revalidate");
            response.setHeader("Expires", htmlExpiresDateFormat().format(cal.getTime()));
        }
    }

    private DateFormat htmlExpiresDateFormat() {
        DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDateFormat;
    }
}
