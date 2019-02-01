#!/bin/bash

source /etc/profile

WORK_HOME=$(cd "$(dirname "$0")"; pwd)
echo "work home: ${WORK_HOME}"
JAVA_CMD="java"
CLASS_PATH="${WORK_HOME}/lib/*:$CLASSPATH"
MAIN_CLASS="com.github.johnsonmoon.casclient.Main"
JAVA_OPTS="-Dspring.config.location=file:${WORK_HOME}/conf/ -Dwork.home=${WORK_HOME}"

${JAVA_CMD} ${JAVA_OPTS} -classpath ${CLASS_PATH} ${MAIN_CLASS} &