#!/bin/bash
export APP=/app/Scaffold
export JAVA=`which java`
$JAVA -Dspring.profiles.active=prod -verbose:gc -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data -jar $APP/Scaffold.jar -esname elasticsearch > /dev/null 2>&1 &
echo $! > $APP/Scaffold.pid