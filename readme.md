<center>
	<img src="csrfguard-test/csrfguard-test-jsp/src/main/webapp/owasp_logo.png"/>
</center>

# [OWASP CSRFGuard 4.x](https://owasp.org/www-project-csrfguard)

[![License](https://img.shields.io/badge/license-BSD-4EB1BA.svg)](https://www.opensource.org/licenses/bsd-license.php)
[![GitHub release](https://img.shields.io/github/release/OWASP/www-project-csrfguard)](https://github.com/OWASP/www-project-csrfguard/releases)
[![Maven Central release](https://img.shields.io/maven-central/v/org.owasp/csrfguard)](https://search.maven.org/search?q=g:org.owasp%20AND%20a:csrfguard)
[![OWASP Flagship](https://img.shields.io/badge/owasp-flagship-brightgreen.svg)](https://owasp.org/projects#div-flagships)
[![Java CI](https://github.com/OWASP/www-project-csrfguard/actions/workflows/ci.yaml/badge.svg)](https://github.com/OWASP/www-project-csrfguard/actions/workflows/ci.yaml)
[![OWASP Dependency check](https://github.com/OWASP/www-project-csrfguard/actions/workflows/dependency_check.yaml/badge.svg)](https://github.com/OWASP/www-project-csrfguard/actions/workflows/dependency_check.yaml)
[![Snyk Security Analysis](https://github.com/OWASP/www-project-csrfguard/actions/workflows/snyk.yaml/badge.svg)](https://github.com/OWASP/www-project-csrfguard/actions/workflows/snyk.yaml)


## Overview

Welcome to the home of the OWASP CSRFGuard Project! OWASP CSRFGuard is a library that implements a variant of the synchronizer token pattern to mitigate the risk of Cross-Site Request Forgery (CSRF) attacks.
The OWASP CSRFGuard library is integrated through the use of a JavaEE Filter and exposes various automated and manual ways to integrate per-session or pseudo-per-request tokens into HTML. When a user interacts
with this HTML, CSRF prevention tokens (i.e. cryptographically random synchronizer tokens) are submitted with the corresponding HTTP request. It is the responsibility of OWASP CSRFGuard to ensure the token is
present and is valid for the current HTTP request. Any attempt to submit a request to a protected resource without the correct corresponding token is viewed as a CSRF attack in progress and is discarded. Prior
to discarding the request, CSRFGuard can be configured to take one or more actions such as logging aspects of the request and redirecting the user to a landing page. The latest release enhances this strategy to
support the optional verification of HTTP requests submitted using Ajax as well as the optional verification of referrer headers.

## Project Leads

The CSRFGuard project is run by [Azzeddine RAMRAMI](mailto:azzeddine.ramrami@owasp.org) and [Istvan ALBERT-TOTH](mailto:istvan.alberttoth@owasp.org).

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

1. Make sure you have [Apache Maven](https://maven.apache.org/) 3.0.4+, JDK 1.8+ and respectively JDK 11+ for Jakarta installed
2. Clone this repository locally
3. Build the project by running ```mvn clean install``` in the project root directory
4. Build and run the test JSP web application by running one of the following commands:
   ```shell
   mvn pre-integration-test -Pdeploy-jsp-webapp -pl csrfguard-test/csrfguard-test-jsp
   ```
   ```bash
   mvn -Pdeploy-jsp-webapp -pl csrfguard-test/csrfguard-test-jsp tomcat7:run
   ```
6. Optional: you can use ```mvnDebug``` to enable remote debugging, then connect your IDE to it (default port is 8000)
7. Use a web browser to access ```http://localhost:8080``` to open the home page of the test project

## Uploading to the Maven Central repository (for project leaders)

1. Follow the [Sonatype Open-Source Project Maven Repository Usage Guide](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide) to create a Sonatype user account;
2. Next, [open a support request](https://issues.sonatype.org/browse/OSSRH) to get your newly created username added to the Maven groupId ```org.owasp```;
3. The ticket must be approved by a CSRFGuard project leader or someone who already has permissions to deploy under the group and artifactId.
4. Once the support request has been completed, follow the instructions in the Sonatype Maven repository usage guide mentioned above to upload new versions to the Maven Central repository.

### GPG Key generation and distribution
See: https://central.sonatype.org/publish/requirements/gpg/

```shell
gpg --full-gen-key
gpg --list-keys
gpg --list-secret-keys # if you've migrated your keys from another machine, make sure you have your secret key(s) imported
gpg --keyserver keys.gnupg.net --send-key <your_public_key_here> # you can define other supported key servers as well
```

### Example `.m2/settings.xml` snippet required for releasing:

```xml
<settings xmlns="https://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="https://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>ossrh</id> <!-- the id must match with the repository id defined in distributionManagement -->
            <username><!--your_oss_sonatype_username--></username>
            <password><!--your_oss_sonatype_password--></password>
        </server>
    </servers>

    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <gpg.passphrase><!--your_gpg_passphrase--></gpg.passphrase>
            </properties>
        </profile>
    </profiles>
</settings>
```

Test signing your artifacts manually:
```shell
mvn clean verify -Psign-artifacts -Dgpg.passphrase=<your_gpg_passphrase> # should generate .asc files in the target directory
```

### Deploying to OSS Sonatype / Maven Central

#### Deploy a `-SNAPSHOT` version:
   ```shell
   mvn clean deploy
   ```

#### Prepare a release:
```shell
mvn release:clean release:prepare
```
1. Set the version number you want to release in <_MAJOR.MINOR.PATCH_> format (e.g. `4.0.0`)
2. Set the SCM release tag: (e.g. `4.0.0`)
3. Set the new development version (e.g. `4.0.1-SNAPSHOT`)

Check the created commits and tag to make sure everything looks as expected:
   ```shell
   git log
   git show HEAD
   git show HEAD^
   git tag -l # list tags
   ```

#### Rollback the local release:
```shell
mvn release:rollback # or "git reset HEAD^^ --hard"
git tag -d <tag_name_to_delete>
```

#### Upload the release to OSS Sonatype (staging):
```shell
mvn release:perform 
```

The `maven-release-plugin` executes the `deploy` (default). This triggers the execution of the `nexus-staging-maven-plugin`, which uploads the artifacts to the [OSS Sonatype staging repository](https://oss.sonatype.org/#stagingRepositories) and releases them if they meet the requirements.

### [Manual release of a staging repository](https://central.sonatype.org/publish/release/) (in case `autoReleaseAfterClose` is set to `false`)

* Visit https://oss.sonatype.org/#stagingRepositories
* Review the newly created repository against the [requirements](https://central.sonatype.org/publish/requirements/) (JAR files, sources, JavaDocs and associated PGP armored ASCII files are present with the desired version etc.)
* `Close` the repository to trigger the validation of the uploaded components
* If there were no errors, click `Release`

Upon release, the new version is published to the Central Repository, typically within 30 minutes, but updates to [search](https://search.maven.org/) can take up to 4 hours.

#### Push the new release to GitHub:
```shell
git push origin master
git push origin <tag_name>
```

### Maven repositories

You can download pre-compiled versions from:

* [Maven Central repository](https://search.maven.org/search?q=csrfguard)
* [OSS Sonatype Nexus repository](https://oss.sonatype.org/#nexus-search;gav~~csrfguard~~~)

## CSRFGuard 4.0.0 Release Notes

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
