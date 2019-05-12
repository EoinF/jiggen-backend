#!/bin/bash
set -ex

cd ..
chmod +x gradlew
./gradlew war -q

aws s3 cp build/libs/jiggen-backend.war s3://jiggen/jiggen-backend.war