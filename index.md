---
layout: col-sidebar
title: CSRFGuard
tags: csrfguard defenders breakers vulnerability-management
level: 4
type: tool
project: true
pitch: OWASP CSRFGuard is a library that implements a variant of the synchronizer token pattern to mitigate the risk of Cross-Site Request Forgery (CSRF) attacks.
---
![Tool Project](assets/images/owasp_tool_project.png)

<!-- rebuild 40 -->

The OWASP CSRFGuard is one of the world’s most popular free security tools and is actively maintained by a pool of international volunteers*. 

Welcome to the home of the OWASP CSRFGuard Project! OWASP CSRFGuard is a library that implements a variant of the synchronizer token pattern to mitigate the risk of Cross-Site Request Forgery (CSRF) attacks. 

The OWASP CSRFGuard library is integrated through the use of a JavaEE Filter and exposes various automated and manual ways to integrate per-session or pseudo-per-request tokens into HTML. When a user interacts with this HTML, CSRF prevention tokens (i.e. cryptographically random synchronizer tokens) are submitted with the corresponding HTTP request. 

It is the responsibility of OWASP CSRFGuard to ensure the token is present and is valid for the current HTTP request. 

Any attempt to submit a request to a protected resource without the correct corresponding token is viewed as a CSRF attack in progress and is discarded. Prior to discarding the request, CSRFGuard can be configured to take one or more actions such as logging aspects of the request and redirecting the user to a landing page. 
The latest release enhances this strategy to support the optional verification of HTTP requests submitted using Ajax as well as the optional verification of referrer headers.
