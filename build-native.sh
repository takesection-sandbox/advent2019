#!/usr/bin/env bash

PATH=$PATH:$GRAALVM_HOME/bin ./mvnw clean install -Dnative

mkdir -p target/ssl-libs/lib
cp $GRAALVM_HOME/jre/lib/security/cacerts target/ssl-libs
cp $GRAALVM_HOME/jre/lib/amd64/libsunec.so target/ssl-libs/lib/
