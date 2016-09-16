#!/usr/bin/env bash


# Now we check to see if there are any java opts on the environemnt.
# These get listed first, with the script able to override them.
java_opts="-Dapplication.name=ttpa"

if [[ "$JAVA_OPTS" != "" ]]; then
    java_opts=" ${JAVA_OPTS}"
fi


args=""

JAR=$(find ../lib -type f -name time-to-pay-arrangement-*.jar)
exec java ${java_opts} ${args} -jar $JAR