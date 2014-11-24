#!/bin/sh

# Setup variables
EXEC=/usr/bin/jsvc
JAVA_HOME=/usr/lib/jvm/java-8-oracle/
CLASS_PATH="/home/fc/.m2/repository/org/mongodb/mongo-java-driver/2.12.1/mongo-java-driver-2.12.1.jar:/home/fc/.m2/repository/io/netty/netty-all/5.0.0.Alpha1/netty-all-5.0.0.Alpha1.jar:/home/fc/.m2/repository/ch/qos/logback/logback-classic/1.1.2/logback-classic-1.1.2.jar:/home/fc/.m2/repository/ch/qos/logback/logback-core/1.1.2/logback-core-1.1.2.jar:/home/fc/.m2/repository/org/slf4j/slf4j-api/1.7.6/slf4j-api-1.7.6.jar:/home/fc/.m2/repository/org/projectlombok/lombok/1.14.8/lombok-1.14.8.jar:/home/fc/.m2/repository/com/google/inject/guice/3.0/guice-3.0.jar:/home/fc/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/home/fc/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:/home/fc/.m2/repository/commons-daemon/commons-daemon/1.0.15/commons-daemon-1.0.15.jar"
APP_CLASS_PATH="/home/fc/javaspace/NettyMongoQueue/target/netty-mongoqueue-1.0-SNAPSHOT.jar"
CLASS=com.fc.server.MongoQueueServer
USER=root
LOG_FOLDER=/var/log/nettyMongo/
PID=$LOG_FOLDER/pid.pid
LOG_OUT=$LOG_FOLDER/example.out
LOG_ERR=$LOG_FOLDER/example.err

mkdir -p $LOG_FOLDER

do_exec()
{
    $EXEC -home "$JAVA_HOME" -cp $CLASS_PATH:$APP_CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS
}

case "$1" in
    start)
        do_exec
            ;;
    stop)
        do_exec "-stop"
            ;;
    restart)
        if [ -f "$PID" ]; then
            do_exec "-stop"
            do_exec
        else
            echo "service not running, will do nothing"
            exit 1
        fi
            ;;
    *)
            echo "usage: daemon {start|stop|restart}" >&2
            exit 3
            ;;
esac