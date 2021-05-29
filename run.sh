#! /bin/bash

# $1: path to the data folder (if applicable)

set -e

docker run -d --network blindtest -p 80:80 -v `pwd`/web-client/dist/web-client:/usr/share/nginx/html blindtest_nginx &

if [ -z "$1" ]; then
    docker run -d -v `pwd`/server/config:/config --network blindtest --ip 172.18.0.24 blindtest_server
else
    docker run -d -v `pwd`/server/config:/config -v "$1":/data --network blindtest --ip 172.18.0.24 blindtest_server
fi
