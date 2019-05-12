#!/bin/bash
set -x

git clone https://github.com/EoinF/jiggen-backend.git
chmod +x jiggen-backend/ci/build-task.sh
chmod +x jiggen-backend/gradlew
cd jiggen-backend/ci
./build-task.sh