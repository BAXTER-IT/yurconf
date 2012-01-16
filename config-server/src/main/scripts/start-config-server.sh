#!/bin/bash
# Start script for Baxter Config Server
# v. ${project.version}
# 

# Setup the JVM
if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
        JAVA="$JAVA_HOME/bin/java"
    else
        JAVA="java"
    fi
fi

SCRIPT_DIR="$(cd $(dirname $0);pwd)"
JETTY_HOME=$SCRIPT_DIR/..

pushd "$JETTY_HOME"

echo "Starting Jetty Container..."
"$JAVA" -jar start.jar

popd
 