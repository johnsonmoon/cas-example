#!/bin/bash

WORK_HOME=$(cd "$(dirname "$0")"; pwd)

cd ${WORK_HOME}/cas-overlay-template-5.3/

./build.sh clean

sleep 1

./build.sh package