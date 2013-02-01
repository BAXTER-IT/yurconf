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

JAVA_OPTS="$JAVA_OPTS -DSTART=${jetty.startup.file}"
PROGRAM="/usr/bin/java $JAVA_OPTS -jar /usr/share/jetty/start.jar ${jetty.config.file}"

# Run in terminal or as a daemon?
if $RUNASDAEMON ; then
    # The output of stderr and stdout will be stored in this file
    exec $PROGRAM > ${unix.application.out} 2>&1
else
    $PROGRAM
fi
