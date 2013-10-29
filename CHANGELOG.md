# Java EE Cache Filter

## 2.1.0

* Move from Google Project Hosting (https://code.google.com/p/cache-filter/) to GitHub (https://github.com/samaxes/javaee-cache-filter).
* Update compiler to Java 1.6.
* Replace Servlet API 2.5 dependency with Java EE 6 Web API.

## 2.0

* Added `NoETagFilter` class to disable HTTP `ETag` header set by the `DefaultServlet` in Tomcat.
* Added `NoCacheFilter` class to completely disable browser caching.
* Replaced `privacy<String>` parameter by `private<Boolean>`. Cache directive to control where the response may be cached.
* Added `static<Boolean>` parameter. Conditional requests are not required for static components.
  Cache directive `must-revalidate` should be used for non static components to force them to be revalidated once a response becomes stale.

## 1.2.1

* Optimized the configuration process. All the configurations are now done in the init method.
* Added the Sonatype OSS Parent POM.

## 1.2

* Changed the default package to `com.samaxes.filter`.
* Updated the distribution repositories to Sonatype Nexus.

## 1.1.0

* Use `response.setDateHeader()` instead of `response.setHeader()` to set `Expires` HTTP cache header.
* Compiled against JDK 1.5 instead of JDK 1.6.
