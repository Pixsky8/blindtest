#! /bin/bash

set -e

# $1: path to config directory
if [ $# = 0 ]; then
    echo 'Usage:'
    echo "	$0 <Path to config directory>"
    exit 1
fi

docker run -it -v "$(readlink -f "$1")":/config --network blindtest --ip 172.18.0.24 blindtest_server
