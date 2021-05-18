#!/bin/bash

openssl req -x509 -newkey rsa:4096 -sha256 -nodes -keyout IdentityServer4Auth.key -out IdentityServer4Auth.crt -subj "/CN=*server-root*" -days 3650
openssl pkcs12 -export -out testcert.pfx -inkey IdentityServer4Auth.key -in IdentityServer4Auth.crt
rm IdentityServer4Auth.crt IdentityServer4Auth.key
