### Project Classification

![Flagship Project](assets/images/mature_projects.png)

![Builders](assets/images/owasp_builders_small.png)
![Breakers](assets/images/owasp_breakers_small.png)

![Tool Project](assets/images/owasp_tool_project.png)

## CSRFGuard 4.0 released in April 2021

[https://owasp.org/www-project-csrfguard/](https://owasp.org/www-project-csrfguard/)

BSD License, All rights reserved.

## Overview

Welcome to the home of the OWASP CSRFGuard Project! OWASP CSRFGuard is a library that implements a variant of the synchronizer token pattern to mitigate the risk of Cross-Site Request Forgery (CSRF) attacks. The OWASP CSRFGuard library is integrated through the use of a JavaEE Filter and exposes various automated and manual ways to integrate per-session or pseudo-per-request tokens into HTML. When a user interacts with this HTML, CSRF prevention tokens (i.e. cryptographically random synchronizer tokens) are submitted with the corresponding HTTP request. It is the responsibility of OWASP CSRFGuard to ensure the token is present and is valid for the current HTTP request. Any attempt to submit a request to a protected resource without the correct corresponding token is viewed as a CSRF attack in progress and is discarded. Prior to discarding the request, CSRFGuard can be configured to take one or more actions such as logging aspects of the request and redirecting the user to a landing page. The latest release enhances this strategy to support the optional verification of HTTP requests submitted using Ajax as well as the optional verification of referrer headers.

## Project Leads

The CSRFGuard project is run by [Azzeddine RAMRAMI](mailto:azzeddine.ramrami@owasp.org) and [Istvan ALBERT-TOTH](mailto:istvan.alberttoth@owasp.org).

## License

OWASP CSRFGuard 4.x is offered under the [BSD license](http://www.opensource.org/licenses/bsd-license.php).

## Using with Maven
Add the following dependencies to your Maven POM file to use the library:  
**Note**: for the [Jakarta](https://github.com/OWASP/www-project-csrfguard/tree/jakarta) releases use the `-jakarta` suffix in the `version`.

```xml
<dependency>
    <groupId>org.owasp</groupId>
    <artifactId>csrfguard</artifactId>
    <version>4.3.0</version>
</dependency>

<!-- Stateful web application support -->
<dependency>
	<groupId>org.owasp</groupId>
	<artifactId>csrfguard-extension-session</artifactId>
	<version>4.3.0</version>
</dependency>

<!-- JSP TAG support -->
<dependency>
	<groupId>org.owasp</groupId>
	<artifactId>csrfguard-jsp-tags</artifactId>
	<version>4.3.0</version>
</dependency>
```

## Building the code

1. Make sure you have [Apache Maven](http://maven.apache.org/) 3.0.4+ and JDK 11+ installed
2. Clone this repository locally
3. Build the project by running ```mvn clean install``` in the project root directory
4. Build and run the test JSP web application by running ```mvn pre-integration-test -Pdeploy-jsp-webapp -pl csrfguard-test/csrfguard-test-jsp``` or ```mvn -Pdeploy-jsp-webapp -pl csrfguard-test/csrfguard-test-jsp tomcat7:run```
5. Optional: you can use ```mvnDebug``` to enable remote debugging, then connect your IDE to it (default port is 8000)
6. Use a web browser to access ```http://localhost:8080``` to open the home page of the test project

## Uploading to the Maven Central repository

1. Follow the [Sonatype Open-Source Project Maven Repository Usage Guide](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide) to create a Sonatype user account;
2. Next, [open a support request](https://issues.sonatype.org/browse/OSSRH) to get your newly created username added to the Maven groupId ```org.owasp```;
3. Once the support request has been completed, follow the instructions in the Sonatype Maven repository usage guide mentioned above to upload new versions to the Maven Central repository.

### Maven Central repository

You can download pre-compiled versions from:

* [Maven Central repository](https://search.maven.org/search?q=csrfguard)
* [OSS Sonatype Nexus repository](https://oss.sonatype.org/#nexus-search;gav~~csrfguard~~~)

## Discussions and Email list

If you have questions, would like to share or discuss ideas, please use the official [discussions page](https://github.com/OWASP/www-project-csrfguard/discussions) (**preferred**). You can also sign up for the OWASP CSRFGuard email list [here](https://groups.google.com/a/owasp.org/g/csrfguard-project).

## CSRFGuard 4.0 Release Notes:

* [Support for stateless web applications](https://github.com/aramrami/OWASP-CSRFGuard/issues/122)
* [Apply "TokenPerPage" approach to AJAX](https://github.com/aramrami/OWASP-CSRFGuard/issues/123)
* [Reduced code duplication](https://github.com/aramrami/OWASP-CSRFGuard/issues/127)
* [Proper multi-module maven project structure](https://github.com/aramrami/OWASP-CSRFGuard/issues/128)
* [The test JSP web application now relies on the latest development JavaScript code](https://github.com/aramrami/OWASP-CSRFGuard/issues/133)
* [Improved code quality](https://github.com/aramrami/OWASP-CSRFGuard/issues/134)
* [Addressing synchronous XMLHttpRequest deprecation](https://github.com/aramrami/OWASP-CSRFGuard/issues/137)
* [Approach changed for master and page token retrieval](https://github.com/aramrami/OWASP-CSRFGuard/issues/139)
* [Improved test coverage](https://github.com/aramrami/OWASP-CSRFGuard/issues/140)
* [Better solution for looking up page tokens in the JS](https://github.com/aramrami/OWASP-CSRFGuard/issues/141)
* [The javascript template is now parsable and minifiable](https://github.com/aramrami/OWASP-CSRFGuard/issues/142)
* [Short-circuit the solution logic if CSRFGuard is disabled](https://github.com/aramrami/OWASP-CSRFGuard/issues/143)
* [Do not generate page tokens for pages that are not protected](https://github.com/aramrami/OWASP-CSRFGuard/issues/144)
* [Page tokens generated on first use are not sent back to the client](https://github.com/aramrami/OWASP-CSRFGuard/issues/145)
* [Issue with the token-per-page support for REST endpoint containing path parameters](https://github.com/aramrami/OWASP-CSRFGuard/issues/146)
* [Possible race condition on first access of endpoints when token-per-page and AJAX request options are enabled](https://github.com/aramrami/OWASP-CSRFGuard/issues/147)
* [Tokens are not injected into dynamically created DOM elements ](https://github.com/aramrami/OWASP-CSRFGuard/issues/148)
* [Make the configuration more resilient to errors](https://github.com/aramrami/OWASP-CSRFGuard/issues/149)
* [Tokens should not be injected into external links if the domainStrict property is set to true](https://github.com/aramrami/OWASP-CSRFGuard/issues/150)
* [Tokens not injected in dynamic content returned from Ajax](https://github.com/aramrami/OWASP-CSRFGuard/issues/151)
* Heavily refactored, improved and more optimized code-base
* Documentation update and typo fixes.
* Copyright update and unification.



