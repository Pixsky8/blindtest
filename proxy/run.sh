#! /bin/bash

set -e

# $1: path to web-client
if [ $# = 0 ]; then
    echo 'Usage:'
    echo "	$0 <Path to web-client>"
    exit 1
fi

docker run -it -v "$(readlink -f $1)":/usr/share/nginx/html -p 80:80 --network blindtest --ip 172.18.0.42 blindtest_nginx bash
