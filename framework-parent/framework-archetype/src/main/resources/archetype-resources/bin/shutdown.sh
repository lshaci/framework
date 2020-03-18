#!/bin/sh

cd `dirname $0`/../target
target_dir=`pwd`

export SERVER="${artifactId}"

pid=`ps ax | grep -i '${SERVER}.${SERVER}' | grep ${target_dir} | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
    echo "No ${SERVER}.${SERVER} running."
    exit -1;
fi

echo "The ${SERVER}.${SERVER}(${pid}) is running..."

kill ${pid}

echo "Send shutdown request to ${SERVER}.${SERVER}(${pid}) OK"
