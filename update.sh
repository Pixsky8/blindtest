#! /bin/bash

### DOCKER UPDATE ###
docker build -t blindtest_proxy proxy
docker build -t blindtest_server server

### ANGULAR UPDATE ###
cd web-client
npm install
ng build
cd -
