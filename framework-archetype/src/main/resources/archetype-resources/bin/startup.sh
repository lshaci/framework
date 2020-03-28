#!/bin/sh

if [ -z "$JAVA_HOME" ]; then
    echo "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
    exit 1;
fi

export SERVER="${artifactId}"

export PROFILE="prod"
while getopts ":p:" opt
do
    case $opt in
        p)
            PROFILE=$OPTARG;;
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`
export DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
export CUSTOM_SEARCH_LOCATIONS=${DEFAULT_SEARCH_LOCATIONS},file:${BASE_DIR}/conf/

JAVA_OPT="${JAVA_OPT} -Xms512m -Xmx512m -Xmn256m -XX:+UseG1GC"

JAVA_OPT="${JAVA_OPT} -Dhome=${BASE_DIR}"
JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/target/${SERVER}.jar"
JAVA_OPT="${JAVA_OPT} --spring.config.location=${CUSTOM_SEARCH_LOCATIONS}"

if [[ "${PROFILE}" == "test" ]]; then
    JAVA_OPT="${JAVA_OPT} --spring.profiles.active=test"
else
    JAVA_OPT="${JAVA_OPT} --spring.profiles.active=prod"
fi


if [ ! -d "${BASE_DIR}/logs" ]; then
  mkdir ${BASE_DIR}/logs
fi

echo "$JAVA ${JAVA_OPT}"

nohup $JAVA ${JAVA_OPT} ${SERVER}.${SERVER} >> ${BASE_DIR}/logs/start.out 2>&1 &
echo -e "server is starting... You can run this command to view the log:\ntail -f ${BASE_DIR}/logs/start.out"
