#! /bin/sh
#
# Starts the ${project.name} within a Jetty Web Container
# Should be typically called by unix service script
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
CONFIG_FILE="${unix.config.dir}/configuration-server"

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

JAVA_OPTS="$JAVA_OPTS -DSTART=${jetty.startup.file}"
PROGRAM="/usr/bin/java $JAVA_OPTS -jar /usr/share/jetty/start.jar ${jetty.config.file}"

# Run in terminal or as a daemon?
if $RUNASDAEMON ; then
    # The output of stderr and stdout will be stored in this file
    exec $PROGRAM > ${unix.application.out} 2>&1
else
    $PROGRAM
fi
