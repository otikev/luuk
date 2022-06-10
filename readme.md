# Luuk



## Deploying the Heroku backend

If you haven't already, log in to your Heroku account run the following:
```markdown
heroku login
```
Add the heroku remote. The heroku app name currently is "luukatme"
```markdown
heroku git:remote -a luukatme
```
From the root of this project run:
```markdown
git subtree push --prefix backend heroku master
```

## Accessing local Postgres console

Switch to postgres user
```
~$ sudo su - postgres
```
Launch psql
```
~$ psql
```

Connect to dev db
```
postgres=# \connect luuk_dev;
```

Exiting psql
```
luuk_dev=# \q
```

## Accessing Heroku Postgres console

```
~$ heroku pg:psql
```


Now run your SQL commands

```
luukatme::DATABASE=> select * from users;
```
## Environment variables

### Variables used for database migrations
```
LUUK_DATABASE_URL
LUUK_DATABASE_USERNAME
LUUK_DATABASE_PASSWORD
```

### Variables used for email integration
```
LUUK_ENABLE_EMAILS
LUUK_MAIL_USERNAME
LUUK_MAIL_PASSWORD
```

### Variables used for OAuth2
```
GOOGLE_SERVER_CLIENT_ID
GOOGLE_SERVER_CLIENT_SECRET
FACEBOOK_APP_ID
FACEBOOK_APP_SECRET
```

### Variables used for MPESA integration
```
DARAJA_STK_URL
DARAJA_AUTH_URL
CONSUMER_KEY
CONSUMER_SECRET
STK_TRANSACTION_TYPE
STK_ACCOUNT_REFERENCE
DARAJA_PASSKEY
BASE_URL
BUSINESS_SHORT_CODE
```

### Variables for AWS S3 integration
```
LUUK_AWS_S3_BUCKET
S3_ACCESS_KEY_ID
S3_SECRET_KEY_ID
```

### Reference Documentation
For further reference, please consider the following sections:

* [Heroku Postgres](https://devcenter.heroku.com/articles/heroku-postgresql)
* [Preparing a Spring Boot App for Production on Heroku](https://devcenter.heroku.com/articles/preparing-a-spring-boot-app-for-production-on-heroku)
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#using-boot-devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-security)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-security-oauth2-client)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

