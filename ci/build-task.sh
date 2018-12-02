#!/bin/bash
set -x

cd ..
./gradlew war -q

aws s3 cp build/libs/jiggen-backend.war s3://jiggen/jiggen-backend.war