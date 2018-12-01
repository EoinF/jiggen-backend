#!/bin/sh
set -x

aws s3 cp s3://jiggen/build-api/jiggen-backend.war /usr/local/tomcat/webapps/ROOT.war

catalina.sh run