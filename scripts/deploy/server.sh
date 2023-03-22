#!/bin/sh
umask 022

####################################################################################################
#
# 该脚本用于Linux系统下启动、停止和重启SpringBoot应用，同时可以查看SpringBoot应用的状态信息及系统环境信息
# 示例：
# 启动
# ./springboot.sh start
# 停止
# ./springboot.sh stop
# 重启
# ./springboot.sh restart
# 查看状态信息
# ./springboot.sh status
# 查看系统信息
# ./springboot.sh info
#
####################################################################################################

##################################################
# 环境变量及系统参数
##################################################

CURRENTPATH=$(
       cd $(dirname $0)
       pwd
)
cd ${CURRENTPATH}

APP_NAME=$(ls | grep .jar$ | awk '{print$0}')
JAR_FILE=${CURRENTPATH}/${APP_NAME}

LOG_DIR=/data/logs
STDOUT_FILE=${LOG_DIR}/${APP_NAME}-std.out
GC_LOG_FILE=${LOG_DIR}/${APP_NAME}-gc.log
DUMP_LOG_FILE=${LOG_DIR}/${APP_NAME}-dump.dump

SERVER_PORT=8081
PID=0
CHECK_COUNT=0
CHECK_LIMIT=100
KILL_COUNT=0
KILL_LIMIT=100

JAVA_HOME="/home/work/develop/jdk1.8"
# JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home"
JAVA="${JAVA_HOME}/bin/java"

# 设置JVM参数
JAVA_OPTS="-Xss512k -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -Xms2G -Xmx2G -XX:+UseG1GC"

# 打印GC日志
JAVA_OPTS="${JAVA_OPTS} -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:"${GC_LOG_FILE}

# JVM发生OOM时，dump文件
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="${DUMP_LOG_FILE}

# JVM参数优化
JAVA_OPTS="${JAVA_OPTS} -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:AutoBoxCacheMax=20000"

# 支持-D参数
JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Djava.io.tmpdir=/tmp -Dfile.encoding=UTF-8"

# 开启远程debug
JAVA_OPTS="${JAVA_OPTS} -Xdebug -Xrunjdwp:transport=dt_socket,address=8005,server=y,suspend=n"

# 数据库参数
SPRING_PARAM=" --spring.datasource.username=root --spring.datasource.password=chatGPT123@  --spring.datasource.ip=localhost:3306 "

##################################################
# 准备
##################################################
prepare() {
       if [ "${APP_NAME}" = "" ]; then
              echo "No app name specified"
              exit 1
       fi
       if [ ! -d ${LOG_DIR} ]; then
              mkdir -p ${LOG_DIR}
       fi
       if [ -f ${STDOUT_FILE} ]; then
              mv ${STDOUT_FILE} ${STDOUT_FILE}.$(date +"%Y%m%d%H%M%S")
       fi
       if [ -f ${GC_LOG_FILE} ]; then
              mv ${GC_LOG_FILE} ${GC_LOG_FILE}.$(date +"%Y%m%d%H%M%S")
       fi
       if [ -f ${DUMP_LOG_FILE} ]; then
              mv ${DUMP_LOG_FILE} ${DUMP_LOG_FILE}.$(date +"%Y%m%d%H%M%S")
       fi
}

##################################################
# 获取进程ID
##################################################
getPid() {
       PID=$(${JAVA_HOME}/bin/jps -m | grep ${APP_NAME} | awk -F" " '{print $1}' | xargs)
}

##################################################
# 启动
##################################################
start() {
       getPid
       if [ ! "${PID}" = "" ]; then
              echo "[$(date +%F-%T)] ${APP_NAME} already started(PID=${PID})" 2>&1 | tee -a ${STDOUT_FILE}
       else
              echo "[$(date +%F-%T)] Starting ${APP_NAME} ..." 2>&1 | tee -a ${STDOUT_FILE}
              nohup ${JAVA} ${JAVA_OPTS} -jar ${JAR_FILE} ${SPRING_PARAM} >/dev/null 2>&1 &
              sleep 3
              check
       fi
}

##################################################
# 检查应用是否启动成功
##################################################
check() {
       while ((1 > 0)); do
              getPid
              if [ "${PID}" = "" ]; then
                     echo "[$(date +%F-%T)] Start ${APP_NAME}  failed" 2>&1 | tee -a ${STDOUT_FILE}
                     break
              else
                     status=$(curl http://127.0.0.1:${SERVER_PORT}/actuator/health 2>/dev/null | grep "\"status\":\"UP\"")
                     if [ "${status}" ]; then
                            echo "[$(date +%F-%T)] ${APP_NAME} started(PID=${PID})" 2>&1 | tee -a ${STDOUT_FILE}
                            break
                     fi
              fi

              let CHECK_COUNT++
              if [ ${CHECK_COUNT} -eq ${CHECK_LIMIT} ]; then
                     echo "[$(date +%F-%T)] The times of check exceeds the limit" 2>&1 | tee -a ${STDOUT_FILE}
                     getPid
                     if [ ! "${PID}" = "" ]; then
                        kill -9 ${PID}
                     fi
                     break
              fi
              sleep 3
              echo "[$(date +%F-%T)] Starting ${APP_NAME} ..." 2>&1 | tee -a ${STDOUT_FILE}
       done
}

##################################################
# 停止
##################################################
stop() {
       getPid
       if [ ! "${PID}" = "" ]; then
              echo "[$(date +%F-%T)] Stopping ${APP_NAME} ... " 2>&1 | tee -a ${STDOUT_FILE}
              
              if [ ${KILL_COUNT} -eq ${KILL_LIMIT} ]; then
                     echo "[$(date +%F-%T)] kill -9 ${PID}" 2>&1 | tee -a ${STDOUT_FILE}
                     kill -9 ${PID}
              else
                     echo "[$(date +%F-%T)] kill -9 ${PID}" 2>&1 | tee -a ${STDOUT_FILE}
                     kill -9 ${PID}
              fi

              sleep 3
              getPid
              if [ ! "${PID}" = "" ]; then
                     let KILL_COUNT++
                     echo "[$(date +%F-%T)] ${APP_NAME} is still running, try the next stop[${KILL_COUNT}]" 2>&1 | tee -a ${STDOUT_FILE}
                     stop
              else
                     echo "[$(date +%F-%T)] ${APP_NAME} stopped" 2>&1 | tee -a ${STDOUT_FILE}
              fi
       else
              echo "[$(date +%F-%T)] ${APP_NAME} is not running" 2>&1 | tee -a ${STDOUT_FILE}
       fi
}

##################################################
# 检查程序运行状态
##################################################
status() {
       getPid
       if [ ! "${PID}" = "" ]; then
              echo "${APP_NAME} is running!"
              echo "PID: ${PID}"
              echo "CMD: $(ps -p ${PID} -o cmd | awk 'NR==2{print}')"
              echo "STARTED: $(ps -p ${PID} -o lstart | awk 'NR==2{print}')"
              echo "ELAPSED: $(ps -p ${PID} -o etime | awk 'NR==2{print}')"
       else
              echo "${APP_NAME} is not running"
       fi
}

##################################################
# 系统信息
##################################################
info() {
       echo "System Information:"
       echo "================================================================================"
       echo $(cat /etc/centos-release)
       echo $(uname -a)
       echo
       echo "JAVA_HOME=$JAVA_HOME"
       echo $($JAVA_HOME/bin/java -version)
       echo
       echo "APP_NAME=${APP_NAME}"
       echo "SERVER_PORT=${SERVER_PORT}"
       echo "================================================================================"
}

##################################################
# 读取脚本的第一个参数($1)进行判断
# 参数取值范围：{start|stop|restart|status|info}
# 如参数不在指定范围之内，则打印帮助信息
##################################################
case "$1" in
'start')
       prepare
       start
       ;;
'stop')
       stop
       ;;
'restart')
       prepare
       stop
       start
       ;;
'status')
       status
       ;;
'info')
       info
       ;;
*)
       echo "Unsupported option $1"
       echo
       echo "Usage: $0 {start|stop|restart|status|info}"
       exit 1
       ;;
esac

exit 0
