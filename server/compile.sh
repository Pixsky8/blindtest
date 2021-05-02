#! /bin/bash

if [ -n "$(java --version | grep 16)" ]; then
    export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
fi

mvn compile
