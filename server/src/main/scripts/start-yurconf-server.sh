#!/bin/sh
#
# Starts the Yurconf Server.
# Should be typically called by unix service or wrapper script.
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
CONFIG_FILE="${f.config}"

# Listening host
YURCONF_HOST="$(cat $CONFIG_FILE | grep "host=" |  cut -d= -f2)"
if [ ! "x" = "x$YURCONF_HOST" ]; then
    JAVA_OPTS="$JAVA_OPTS -Dyurconf.host=$YURCONF_HOST"
fi

# Listening port
YURCONF_PORT="$(cat $CONFIG_FILE | grep "port=" |  cut -d= -f2)"
if [ ! "x" = "x$YURCONF_PORT" ]; then
    JAVA_OPTS="$JAVA_OPTS -Dyurconf.port=$YURCONF_PORT"
fi

JAVA_OPTS="$JAVA_OPTS -Dyurconf.repository=${f.repository}"
JAVA_OPTS="$JAVA_OPTS -Dyurconf.processors=${f.processors}"
JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=${f.log.config}"
JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"

if [ "x" = "x$JAVA_HOME" ]; then
	JAVA="java"
else
	JAVA="$JAVA_HOME/bin/java"
fi

MAIN_JAR="${f.jar}"
PROGRAM="$JAVA $JAVA_OPTS -jar $MAIN_JAR"

# Run in terminal or as a daemon?
if $RUNASDAEMON ; then
	if [ "x$OUTFILE" = "x" ]; then
		if [ "x$OUTDIR" = "x" ]; then
			OUTDIR="${f.out.dir}"
		fi
		OUTFILE="$OUTDIR/yurconf-server.out"
	fi
    # The output of stderr and stdout will be stored in this file
    exec $PROGRAM > $OUTFILE 2>&1
else
    $PROGRAM
fi
