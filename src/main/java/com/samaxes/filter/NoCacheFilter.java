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
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.samaxes.filter.util.Cacheability;
import com.samaxes.filter.util.HTTPCacheHeader;
import com.samaxes.filter.util.StringUtil;

/**
 * <p>
 * Filter allowing to completely disable browser caching.
 * </p>
 * <h2>Options</h2>
 * <table summary="Filter options" border="1">
 * <tr>
 * <th>Option</th>
 * <th>Required</th>
 * <th>Default</th>
 * <th>Since</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@code no-cache}</td>
 * <td>No</td>
 * <td>{@code true}</td>
 * <td>2.4</td>
 * <td>Forces caches to submit the request to the origin server for validation before releasing a cached copy.
 * </td>
 * </tr>
 * <tr>
 * <td>{@code no-store}</td>
 * <td>No</td>
 * <td>{@code true}</td>
 * <td>2.4</td>
 * <td>The cache should not store anything about the client request or server response.
 * </td>
 * </tr>
 * </table>
 * <h2>Sample configuration:</h2>
 * <p>
 * Declare the filter in your web descriptor file {@code web.xml}:
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
 * &lt;!-- Using Servlet mapping --&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 *     &lt;servlet-name&gt;MyServlet&lt;/servlet-name&gt;
 *     &lt;dispatcher&gt;REQUEST&lt;/dispatcher&gt;
 *     &lt;dispatcher&gt;FORWARD&lt;/dispatcher&gt;
 * &lt;/filter-mapping&gt;
 *
 * &lt;!-- Or URL mapping --&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author Samuel Santos
 * @author John Yeary
 * @version 2.3.1
 */
public class NoCacheFilter implements Filter {

    private static Cacheability[] ALLOWED_DIRECTIVES = new Cacheability[]{Cacheability.NO_CACHE, Cacheability.NO_STORE};

    private String cacheControl;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        List<String> cacheControlDirectives = new LinkedList<String>();

        for (Cacheability allowedDirective : ALLOWED_DIRECTIVES) {
            String directive = allowedDirective.getValue();
            String filterConfigValue = filterConfig.getInitParameter(directive);
            if (filterConfigValue == null || Boolean.valueOf(filterConfigValue)) {
                cacheControlDirectives.add(directive);
            }
        }
        cacheControl = StringUtil.join(", ", cacheControlDirectives.toArray(new String[]{}));
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

        // set cache directives
        httpServletResponse.setHeader(HTTPCacheHeader.CACHE_CONTROL.getName(), cacheControl);
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
