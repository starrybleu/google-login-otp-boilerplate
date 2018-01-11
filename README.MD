# google-login-otp-boilerplate

This project can be referenced for novices to spring boot + spring security who want to implement google-login or OTP based login.

이 프로젝트는 스프링부트와 스프링 시큐리티를 이용하여 구글 로그인과 OTP 인증을 구현하고자 하는 초심자들을 위한 참고 자료가 될 수 있습니다.
# Stack
[backend]
1. spring boot
2. spring security oauth2
3. MariaDB

[frontend]
1. npm
2. webpack ( + webpack-dev-server for development environment)

- can be extended with frontend frameworks.

[knowledge]
1. Time based OTP Algorithm and mechanism 

# How to start

Set up your databases connection informations on application-local.yml.
Also set up both your secret and client_id from google api on google-oauth.yml.

$ npm install
$ ./gradlew bootRun -Dspring.profiles.active=local