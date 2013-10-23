/*
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
import com.samaxes.filter.util.CacheConfigParameter;
import com.samaxes.filter.util.Cacheability;
import com.samaxes.filter.util.HTTPCacheHeader;

/**
 * <p>
 * Filter responsible for browser caching.</p>
 * <table border="1">
 * <tr>
 * <th>Option</th>
 * <th>Required</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>static</td>
 * <td>No</td>
 * <td>false</td>
 * <td>Defines whether a component is static or not. Conditional requests
 * <sup>(1)</sup> are not required for static components.</td>
 * </tr>
 * <tr>
 * <td>private</td>
 * <td>No</td>
 * <td>false</td>
 * <td>Cache directive to control where the response may be cached.<br/>
 *
 * <code>false</code> indicates that the response MAY be cached by any
 * cache.<br/>
 *
 * <code>true</code> indicates that the response is intended for a single user
 * and MUST NOT be cached by a shared cache, a private (non-shared) cache MAY
 * cache the response.
 * </td>
 * </tr>
 * <tr>
 * <td>expirationTime</td>
 * <td>Yes</td>
 * <td> - </td>
 * <td>Cache directive to set an expiration time, in seconds, relative to the
 * current date.</td>
 * </tr>
 * </table>
 * <p>
 * <sup>(1)</sup> If a component is already in the browser's cache and is being
 * re-requested, the browser will pass the Last-Modified date in the request
 * header. This is called a conditional GET request and if the component has not
 * been modified, the server will return a 304 Not Modified response.
 * </p>
 * <p>
 * Example configuration</p>
 * <pre>
 * &lt;filter&gt;
 * &lt;filter-name&gt;imagesCache&lt;/filter-name&gt;
 * &lt;filter-class&gt;com.samaxes.filter.CacheFilter&lt;/filter-class&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;static&lt;/param-name&gt;
 * &lt;param-value&gt;true&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;expirationTime&lt;/param-name&gt;
 * &lt;param-value&gt;2592000&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;/filter&gt;
 *
 * &lt;filter&gt;
 * &lt;filter-name&gt;cssCache&lt;/filter-name&gt;
 * &lt;filter-class&gt;com.samaxes.filter.CacheFilter&lt;/filter-class&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;expirationTime&lt;/param-name&gt;
 * &lt;param-value&gt;604800&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;/filter&gt;
 *
 * &lt;filter&gt;
 * &lt;filter-name&gt;jsCache&lt;/filter-name&gt;
 * &lt;filter-class&gt;com.samaxes.filter.CacheFilter&lt;/filter-class&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;private&lt;/param-name&gt;
 * &lt;param-value&gt;true&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;expirationTime&lt;/param-name&gt;
 * &lt;param-value&gt;216000&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * <p>
 * Map the filter to serve your static resources </p>
 * <pre>
 * &lt;filter-mapping&gt;
 * &lt;filter-name&gt;imagesCache&lt;/filter-name&gt;
 * &lt;url-pattern&gt;/img/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 *
 * &lt;filter-mapping&gt;
 * &lt;filter-name&gt;cssCache&lt;/filter-name&gt;
 * &lt;url-pattern&gt;*.css&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 *
 * &lt;filter-mapping&gt;
 * &lt;filter-name&gt;jsCache&lt;/filter-name&gt;
 * &lt;url-pattern&gt;*.js&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.0.1
 */
public class CacheFilter implements Filter {

    private Cacheability cacheability;
    private boolean isStatic;
    private long seconds;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        cacheability = (Boolean.valueOf(filterConfig.getInitParameter(CacheConfigParameter.PRIVATE.getName()))) ? Cacheability.PRIVATE
                : Cacheability.PUBLIC;
        isStatic = Boolean.valueOf(filterConfig.getInitParameter(CacheConfigParameter.STATIC.getName()));

        try {
            seconds = Long.valueOf(filterConfig.getInitParameter(CacheConfigParameter.EXPIRATION_TIME.getName()));
        } catch (NumberFormatException e) {
            throw new ServletException(new StringBuilder("The initialization parameter ").append(
                    CacheConfigParameter.EXPIRATION_TIME.getName()).append(" is missing for filter ").append(
                            filterConfig.getFilterName()).append(".").toString());
        }
    }

    /**
     * {@inheritDoc}
     *
     * Set cache header directives.
     *
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        StringBuilder cacheControl = new StringBuilder(cacheability.getValue()).append(", max-age=").append(seconds);

        if (!isStatic) {
            cacheControl.append(", must-revalidate");
        }

        // Set cache directives
        httpServletResponse.setHeader(HTTPCacheHeader.CACHE_CONTROL.getName(), cacheControl.toString());
        httpServletResponse.setDateHeader(HTTPCacheHeader.EXPIRES.getName(), System.currentTimeMillis() + seconds
                * 1000L);

        /*
         * By default, some servers (e.g. Tomcat) will set headers on any SSL content to deny caching. Setting the
         * Pragma header to null or to an empty string takes care of user-agents implementing HTTP 1.0.
         */
        if (httpServletResponse.containsHeader("Pragma")) {
            httpServletResponse.setHeader(HTTPCacheHeader.PRAGMA.getName(), null);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }
}