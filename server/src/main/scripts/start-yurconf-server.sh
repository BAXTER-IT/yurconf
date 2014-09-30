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

LIBDIR="${f.lib.dir}"

# Finds a dependency (1-st param) in a specified path (2-nd param)
findDep() {
	echo "$(echo $1 | xargs find $2 -name | head -1)"
}

# Looks up the dependency specified by param
lookupDep() {
	p="$(findDep $1 $LIBDIR)"
	if [ "x$p" == "x" ]; then
		p="$(findDep $1 /usr/share/java)"
		if [ "x$p" == "x" ]; then
			return 1
		fi
	fi
	echo "$p"
}

# Relativizes a path given by parameter
toRel() {
	echo "$(perl -e 'use File::Spec; print File::Spec->abs2rel(@ARGV) . "\n"' $1 $(pwd))"
}

DEPS="${unix-classpath}"
YCP="$(toRel ${f.lib.dir}/${project.build.finalName}.jar)"
for d in ${DEPS//:/ }; do
	pattern="'$( echo $d | sed 's/\.jar/\*\.jar/' | sed 's/\///g' )'"
	YCP="$YCP:$(toRel $(lookupDep $pattern))"
done

echo "Reconstructred classpath: $YCP"

PROGRAM="$JAVA $JAVA_OPTS -cp $YCP org.yurconf.server.Main"

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
