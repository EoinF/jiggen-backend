#!/bin/bash
set -x

cd ..
./gradlew war

cd docker-deploy
aws s3 cp build/libs/jiggen-backend.war s3://jiggen/build-api