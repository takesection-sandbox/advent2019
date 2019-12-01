#!/usr/bin/env bash

PATH=$PATH:$GRAALVM_HOME/bin ./mvnw clean install -Dnative
