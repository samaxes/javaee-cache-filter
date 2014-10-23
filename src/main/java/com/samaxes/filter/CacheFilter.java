/*
 * Java EE Cache Filter
 * https://github.com/samaxes/javaee-cache-filter
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
import javax.servlet.http.HttpServletResponseWrapper;

import com.samaxes.filter.util.CacheConfigParameter;
import com.samaxes.filter.util.Cacheability;
import com.samaxes.filter.util.HTTPCacheHeader;

/**
 * <p>
 * Filter allowing to enable browser caching.
 * </p>
 * <table summary="Filter options" border="1">
 * <tr>
 * <th>Option</th>
 * <th>Required</th>
 * <th>Default</th>
 * <th>Since</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@code expiration}</td>
 * <td>Yes</td>
 * <td>--</td>
 * <td>2.2.0</td>
 * <td>Cache directive to set an expiration time, in seconds, relative to the current date. Used for both
 * {@code Cache-Control} and {@code Expires} HTTP headers.</td>
 * </tr>
 * <tr>
 * <td>{@code private}</td>
 * <td>No</td>
 * <td>{@code false}</td>
 * <td>2.0.0</td>
 * <td>Cache directive to control where the response may be cached.
 * <ul>
 * <li><code>false</code> indicates that the response MAY be cached by any cache.</li>
 * <li><code>true</code> indicates that the response is intended for a single user and MUST NOT be cached by a shared
 * cache, a private (non-shared) cache MAY cache the response.</li>
 * </ul>
 * </td>
 * </tr>
 * <tr>
 * <td>{@code must-revalidate}</td>
 * <td>No</td>
 * <td>{@code false}</td>
 * <td>2.2.0</td>
 * <td>Cache directive to define whether conditional requests<sup>(1)</sup> are required or not for stale responses.
 * When the {@code must-revalidate} directive is present in a response received by a cache, that cache MUST NOT use the
 * entry after it becomes stale to respond to a subsequent request without first revalidating it with the origin server.
 * </td>
 * </tr>
 * <tr>
 * <td>{@code vary}</td>
 * <td>No</td>
 * <td>--</td>
 * <td>2.3.0</td>
 * <td>Cache directive to instructs proxies to cache different versions of the same resource based on specific
 * request-header fields. Some possible {@code Vary} header values include:
 * <ul>
 * <li><code>Accept-Encoding</code> instructs proxies to cache two versions of a compressible resource: one compressed,
 * and one uncompressed.</li>
 * <li><code>Accept-Language</code> instructs proxies to cache different versions of a resource based on the language of
 * a given request.</li>
 * <li><code>Accept</code> instructs proxies to cache different versions of a resource based on the response format of a
 * given request (e.g. {@code Accept: application/xml} or {@code Accept: application/json}).</li>
 * </ul>
 * <strong>Warn:</strong> if your server does vary responses but does not indicate so via the {@code Vary} header it may
 * result in cache corruption: <a href="http://www.subbu.org/blog/2007/12/vary-header-for-restful-applications">Vary
 * Header for RESTful Applications</a>.</td>
 * </tr>
 * </table>
 * <p>
 * <sup>(1)</sup> If a component is already in the browser's cache and is being re-requested, the browser will pass the
 * {@code Last-Modified} date in the request header. This is called a <em>conditional GET request</em> and if the
 * component has not been modified, the server will return a {@code 304 Not Modified} response.
 * </p>
 * <h2>Sample configuration:</h2>
 * <p>
 * Declare the filter in your web descriptor file {@code web.xml}:
 * </p>
 *
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;imagesCache&lt;/filter-name&gt;
 *     &lt;filter-class&gt;com.samaxes.filter.CacheFilter&lt;/filter-class&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;expiration&lt;/param-name&gt;
 *         &lt;param-value&gt;2592000&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 * &lt;/filter&gt;
 *
 * &lt;filter&gt;
 *     &lt;filter-name&gt;cssCache&lt;/filter-name&gt;
 *     &lt;filter-class&gt;com.samaxes.filter.CacheFilter&lt;/filter-class&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;expiration&lt;/param-name&gt;
 *         &lt;param-value&gt;604800&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;vary&lt;/param-name&gt;
 *         &lt;param-value&gt;Accept-Encoding&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 * &lt;/filter&gt;
 *
 * &lt;filter&gt;
 *     &lt;filter-name&gt;jsCache&lt;/filter-name&gt;
 *     &lt;filter-class&gt;com.samaxes.filter.CacheFilter&lt;/filter-class&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;expiration&lt;/param-name&gt;
 *         &lt;param-value&gt;216000&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;private&lt;/param-name&gt;
 *         &lt;param-value&gt;true&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * <p>
 * Map the filter to serve your static resources:
 * </p>
 *
 * <pre>
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;imagesCache&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/img/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 *
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;cssCache&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;*.css&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 *
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;jsCache&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;*.js&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.3.1
 */
public class CacheFilter implements Filter {

    private long expiration;

    private Cacheability cacheability;

    private boolean mustRevalidate;

    private String vary;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            expiration = Long.valueOf(filterConfig.getInitParameter(CacheConfigParameter.EXPIRATION.getName()));
        } catch (NumberFormatException e) {
            throw new ServletException(new StringBuilder("The initialization parameter ")
                    .append(CacheConfigParameter.EXPIRATION.getName())
                    .append(" is invalid or is missing for the filter ").append(filterConfig.getFilterName())
                    .append(".").toString());
        }
        cacheability = Boolean.valueOf(filterConfig.getInitParameter(CacheConfigParameter.PRIVATE.getName())) ? Cacheability.PRIVATE
                : Cacheability.PUBLIC;
        mustRevalidate = Boolean.valueOf(filterConfig.getInitParameter(CacheConfigParameter.MUST_REVALIDATE.getName()));
        vary = filterConfig.getInitParameter(CacheConfigParameter.VARY.getName());
    }

    /**
     * <p>
     * Set HTTP cache headers.
     * </p>
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        StringBuilder cacheControl = new StringBuilder(cacheability.getValue()).append(", max-age=").append(expiration);
        if (mustRevalidate) {
            cacheControl.append(", must-revalidate");
        }

        // Set cache directives
        httpServletResponse.setHeader(HTTPCacheHeader.CACHE_CONTROL.getName(), cacheControl.toString());
        httpServletResponse.setDateHeader(HTTPCacheHeader.EXPIRES.getName(), System.currentTimeMillis() + expiration
                * 1000L);

        // Set Vary field
        if (vary != null && !vary.isEmpty()) {
            httpServletResponse.setHeader(HTTPCacheHeader.VARY.getName(), vary);
        }

        /*
         * By default, some servers (e.g. Tomcat) will set headers on any SSL content to deny caching. Omitting the
         * Pragma header takes care of user-agents implementing HTTP/1.0.
         */
        filterChain.doFilter(servletRequest, new HttpServletResponseWrapper(httpServletResponse) {
            @Override
            public void addHeader(String name, String value) {
                if (!HTTPCacheHeader.PRAGMA.getName().equalsIgnoreCase(name)) {
                    super.addHeader(name, value);
                }
            }

            @Override
            public void setHeader(String name, String value) {
                if (!HTTPCacheHeader.PRAGMA.getName().equalsIgnoreCase(name)) {
                    super.setHeader(name, value);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }
}
