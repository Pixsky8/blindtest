#! /bin/bash

set -e

docker run -d --network blindtest -p 80:80 -v `pwd`/web-client/dist/web-client:/usr/share/nginx/html blindtest_nginx &
docker run -d -v `pwd`/server/config:/config --network blindtest --ip 172.18.0.24 blindtest_server
