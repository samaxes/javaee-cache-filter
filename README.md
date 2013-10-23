# Java EE Cache Filter

Java EE Cache Filter provides a collection of common Servlet filters for Java web applications based on the [Servlet 2.5 specification](http://jcp.org/en/jsr/detail?id=154).
It allows you to transparently set HTTP cache headers in order to enable browser caching.

## Why does cache matter?

*From [Best Practices for Speeding Up Your Web Site](http://developer.yahoo.com/performance/rules.html):*

> Web page designs are getting richer and richer, which means more scripts, stylesheets, images, and Flash in the page. A first-time visitor to your page may have to make several HTTP requests, but by using the Expires header you make those components cacheable. This avoids unnecessary HTTP requests on subsequent page views. Expires headers are most often used with images, but they should be used on all components including scripts, stylesheets, and Flash components.

> Browsers (and proxies) use a cache to reduce the number and size of HTTP requests, making web pages load faster.

## Available filters and docs

Click on the filter's name to access its documentation:

| Filter                                    | Description                                                                                                         |
| ---                                       | ---                                                                                                                 |
| [CacheFilter](../../wiki/CacheFilter)     | Allows you to enable browser caching for requested resources.                                                       |
| [NoCacheFilter](../../wiki/NoCacheFilter) | Allows you to completely disable browser caching for requested resources.                                           |
| [NoETagFilter](../../wiki/NoETagFilter)   | Allows you to disable HTTP ETag headers set by most Java web containers (e.g. [Tomcat](http://tomcat.apache.org/)). |

## Maven dependency

Add Java EE Cache Filter dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.samaxes.filter</groupId>
    <artifactId>cachefilter</artifactId>
    <version>VERSION</version>
</dependency>
```

## System Requirements
  
Java EE Cache Filter works with Java EE 5 or newer.

## License

This distribution is licensed under the terms of the Apache License, Version 2.0 (see [LICENSE](LICENSE)).
