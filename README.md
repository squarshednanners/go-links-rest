Go Links Backend REST API
===============

[![Build Status](https://travis-ci.org/squarshednanners/go-links-rest.svg?branch=master)](https://travis-ci.org/squarshednanners/go-links-rest)

Check out the frontend code at: https://github.com/squarshednanners/go-links-ui

#### QuickStart:
	1. Requirement: Redis: Installation instructions: https://redis.io/
	2. Run src/main/java/com/go/Application.java using -Dspring.profiles.active=local

Optionally SSL can be configured by uncommenting out the SSL properties in the application.properties file

localhost self-signed sample cert commands:
#generate .jks
keytool -genkey -keyalg RSA -alias gokeys -keystore gokeys.jks -storepass mypass -validity 3650 -keysize 2048 -storetype JKS -ext san=dns:localhost,ip:127.0.0.1,ip:::1

#export cert
keytool -exportcert -rfc -alias gokeys -keystore gokeys.jks -file gokeysc.crt

#generate pkcs12
keytool -importkeystore -srckeystore gokeys.jks -destkeystore gokeys.p12 -srcalias gokeys -srcstoretype jks -deststoretype pkcs12

#create pem file
openssl pkcs12 -in gokeys.p12 -out gokeys.pem

#create pem file with no password
openssl rsa -in gokeys.pem -out gokeys_nopassphrase.pem
openssl x509 -in gokeys.pem >>gokeys_nopassphrase.pem
