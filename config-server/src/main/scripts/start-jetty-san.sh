#! /bin/sh
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
CONFIG_FILE="${san.dest.root}/etc/configuration-server"

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

if [ -f /usr/share/jetty/start.jar ]; then                                                                                                            
    JETTY_JAR="/usr/share/jetty/start.jar"  
    JAVA_OPTS="$JAVA_OPTS -Djetty.home=/usr/share/jetty"                                                                                                          
fi                                                                                                                                                    
if [ -f /usr/share/jetty6/lib/start.jar ]; then                                                                                                       
    JETTY_JAR="/usr/share/jetty6/lib/start.jar"
    JAVA_OPTS="$JAVA_OPTS -Djetty.home=/usr/share/jetty6"                                                                                                       
fi                                                 

if [ "x" == "x$JETTY_JAR" ]; then
	echo "Cannot find Jetty Jar"
	exit 1
fi

JAVA_OPTS="$JAVA_OPTS -DSTART=${san.dest.root}/etc/jetty/jetty.conf"
PROGRAM="/usr/bin/java $JAVA_OPTS -jar $JETTY_JAR ${san.dest.root}/etc/jetty/jetty.xml"

# Run in terminal or as a daemon?
if $RUNASDAEMON ; then
    # The output of stderr and stdout will be stored in this file
    exec $PROGRAM > ${san.dest.root}/log/config-server.out 2>&1
else
    $PROGRAM
fi
