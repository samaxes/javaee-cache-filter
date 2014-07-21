# Java EE Cache Filter

## 2.2.0

* Use a `HttpServletResponseWrapper` to remove HTTP/1.0 `Pragma` header.
* Replace `expirationTime<Long>` parameter with `expiration<Long>`.
* Replace `static<Boolean>` parameter with `must-revalidate<Boolean>`.

## 2.1.0

* Move from Google Project Hosting (https://code.google.com/p/cache-filter/) to GitHub (https://github.com/samaxes/javaee-cache-filter).
* Update compiler to Java 6.
* Replace Servlet API 2.5 dependency with Java EE 6 Web API.

## 2.0

* Add `NoETagFilter` class to disable HTTP `ETag` header set by the `DefaultServlet` in Tomcat.
* Add `NoCacheFilter` class to completely disable browser caching.
* Replace `privacy<String>` parameter with `private<Boolean>`. Cache directive to control where the response may be cached.
* Add `static<Boolean>` parameter. Conditional requests are not required for static components.
  Cache directive `must-revalidate` should be used for non static components to force them to be revalidated once a response becomes stale.

## 1.2.1

* Optimize the configuration process. All the configurations are now done in the init method.
* Add the Sonatype OSS Parent POM.

## 1.2

* Change the default package to `com.samaxes.filter`.
* Update the distribution repositories to Sonatype Nexus.

## 1.1.0

* Use `response.setDateHeader()` instead of `response.setHeader()` to set `Expires` HTTP cache header.
* Compile against JDK 1.5 instead of JDK 1.6.
