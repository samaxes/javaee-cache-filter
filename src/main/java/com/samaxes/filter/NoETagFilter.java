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

import com.samaxes.filter.util.HTTPCacheHeader;

/**
 * <p>
 * Filter allowing to disable {@code ETag} header from a HTTP response.
 * <p>
 * <h2>Sample configuration</h2>
 * <p>
 * <strong>Note:</strong> This configuration describes how to disable HTTP {@code ETag} header set by the
 * <strong>DefaultServlet</strong> in Tomcat.
 * </p>
 * <p>
 * Declare the filter in your web descriptor file {@code web.xml}:
 * </p>
 *
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;noEtag&lt;/filter-name&gt;
 *     &lt;filter-class&gt;com.samaxes.filter.NoETagFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * </pre>
 * <p>
 * Map the filter to Tomcat&#x27;s <strong>DefaultServlet</strong>:
 * </p>
 *
 * <pre>
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;noEtag&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;default&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.3.0
 */
public class NoETagFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * <p>
     * Disables {@code ETag} HTTP header.
     * </p>
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        filterChain.doFilter(servletRequest, new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
            @Override
            public void setHeader(String name, String value) {
                if (!HTTPCacheHeader.ETAG.getName().equalsIgnoreCase(name)) {
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
