#! /bin/bash

mvn -q exec:java -Dexec.args="$*"
