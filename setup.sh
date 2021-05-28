#! /bin/bash

set -e

### DOCKER SETUP ###
docker network create --subnet=172.18.0.0/16 blindtest

docker build -t blindtest_proxy proxy
docker build -t blindtest_server server

### ANGULAR SETUP ###
cd web-client
npm install
ng build
cd -
