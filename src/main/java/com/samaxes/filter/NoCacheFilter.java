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
package com.samaxes.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.samaxes.filter.util.HTTPCacheHeader;

/**
 * <p>
 * Completely disable browser caching.
 * </p>
 * <p>
 * Example configuration:
 * </p>
 *
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 *     &lt;filter-class&gt;com.samaxes.filter.NoCacheFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * </pre>
 * <p>
 * Map the filter to serve your dynamic resources:
 * </p>
 *
 * <pre>
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 *     &lt;servlet-name&gt;MyServlet&lt;/servlet-name&gt;
 *     &lt;dispatcher&gt;REQUEST&lt;/dispatcher&gt;
 *     &lt;dispatcher&gt;FORWARD&lt;/dispatcher&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.1.0
 */
public class NoCacheFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Set cache header directives. {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // set cache directives
        httpServletResponse.setHeader(HTTPCacheHeader.CACHE_CONTROL.getName(), "no-cache, no-store, must-revalidate");
        httpServletResponse.setDateHeader(HTTPCacheHeader.EXPIRES.getName(), 0L);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }
}
