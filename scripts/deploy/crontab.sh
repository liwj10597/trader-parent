#!/bin/sh
umask 022


JAVA_HOME="/home/work/develop/jdk1.8"
APP_NAME="trader-server-launcher"

PID=$(${JAVA_HOME}/bin/jps -m | grep ${APP_NAME} | awk -F" " '{print $1}' | xargs)

ps -p $PID
if [ $? -eq 0 ]; then
    echo "app process exist"
else
    /home/work/server/server.sh start
fi