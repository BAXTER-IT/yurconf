#!/bin/sh
#
# Starts the ${project.name} within a Jetty Web Container.
# Should be typically called by unix service script.
#
# Supported options:
#   --daemon        starts the application as daemon and dumps output to file
#

set -e

if [ "$1" = "--daemon" ]; then
    RUNASDAEMON=true
    shift
else
    RUNASDAEMON=false
fi

# Configuration file that keeps server binding info
CONFIG_FILE="${f.unix.config}"

# Listening host
JETTY_HOST="$(cat $CONFIG_FILE | grep "host=" |  cut -d= -f2)"
if [ ! "x" = "x$JETTY_HOST" ]; then
    JAVA_OPTS="$JAVA_OPTS -Djetty.host=$JETTY_HOST"
fi

# Listening port
JETTY_PORT="$(cat $CONFIG_FILE | grep "port=" |  cut -d= -f2)"
if [ ! "x" = "x$JETTY_PORT" ]; then
    JAVA_OPTS="$JAVA_OPTS -Djetty.port=$JETTY_PORT"
fi

JAVA_OPTS="$JAVA_OPTS -Djetty.host=$JETTY_HOST"
JAVA_OPTS="$JAVA_OPTS -Djetty.port=$JETTY_PORT"
JAVA_OPTS="$JAVA_OPTS -Dcom.baxter.config.Repository=${f.repository}"
JAVA_OPTS="$JAVA_OPTS -Dcom.baxter.config.Processors=${f.processors.dir}"
# TODO Only one of below is needed at a time, need to detect from environment
JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=${f.log.config}"
JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=${f.log.config}"

if [ "x" = "x$JAVA_HOME" ]; then
	JAVA="java"
else
	JAVA="$JAVA_HOME/bin/java"
fi

MAIN_JAR="${f.jar}"
PROGRAM="$JAVA $JAVA_OPTS -jar $MAIN_JAR"

# Run in terminal or as a daemon?
if $RUNASDAEMON ; then
	if [ "x$OUTDIR" = "x" ]; then
		OUTDIR="${f.out.dir}"
	fi
    # The output of stderr and stdout will be stored in this file
    exec $PROGRAM > $OUTDIR/${unix.service.name}.out 2>&1
else
    $PROGRAM
fi
