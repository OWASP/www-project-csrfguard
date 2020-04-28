---

title: Roadmap
displaytext: CSRFGuard 4.0
layout: null
tab: true
order: 5
tags: csrfguard

---

# Documentations for various Web Application Servers

We need first to create a complete set of installation guide for various Java Application Server.

# CSRFGuard 4.0 features

After we work on the roadmap including the following enhancement:

- CSRF Guard utilizes the external interface of Tomcat web server; it may not provide adequate protection for other web server applications (e.g. Internet Information Server IIS).
- CSRF Guard can provide adequate protection against CSRF attacks; however, it can be compromised by server side vulnerabilities such as cross site scripting or client side vulnerabilities (session hijacking or clickjacking) or unintentional leverage token by unauthorized parties.
- CSRF Guard makes the connection between a token and a session id to check the token validity, Because of this dependency on the session identifiers; it cannot defend against login CSRF attacks.
- CSRF Guard filters input HTTP request by token pattern; therefore, it cannot check the input validation error, to effectively protect against CSRF attack, the developer needs to
ensure the web application implements countermeasures to defense against cross scripting attacks.
- Enhance Token Injection Process 
- Add protection against Clickjacking
- Add protection against Session Hijacking bypassing CSRFGuard
- Add protection against XSS by passing CSFRGuard
