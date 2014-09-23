#!/bin/sh
#
# yurconf-server	Start/stop Yurconf service
#
# chkconfig: 345 10 90
#

### BEGIN INIT INFO
# Provides:          ${unix.service}
# Required-Start:    $local_fs $network
# Required-Stop:     $local_fs $network
# Default-Start:     3 4 5
# Default-Stop:      0 1 2 6
# Short-Description: ${project.name} initscript
# Description:       This script controls the ${unix.service} service
### END INIT INFO

# set -e

if [ -f /etc/init.d/functions ]; then
    . /etc/init.d/functions
fi

# Daemon process id will be written here
if [ "x$PIDDIR" == "x" ]; then
	PIDDIR="${f.pid.dir}"
fi
mkdir -p $PIDDIR
if [ ! -w $PIDDIR ]; then
	echo "PID Directory $PIDDIR is not writable, please check it"
	exit 2
fi
PIDFILE="$PIDDIR/${unix.service}.pid"

# Daemon start script
DAEMON="${f.bin.dir}/start-${unix.service}.sh"
# Daemon will be started with this user
DAEMONUSER=${unix.user}

MAX_WAIT_CHILD_ITER=50
MAX_WAIT_THIS_ITER=20
MAX_WAIT_MARKER_ITER=500

SERVICENAME="${unix.service}"

# Configuration file that keeps server binding info
CONFIG_FILE="${f.config}"

# Listening port
YURCONF_PORT="$(cat $CONFIG_FILE | grep "port=" |  cut -d= -f2)"

if [ "x$OUTDIR" == "x" ]; then
	OUTDIR="${f.out.dir}"
fi
if [ ! -w $OUTDIR ]; then
	echo "OUT Directory $OUTDIR is not writable, please check it"
	exit 2
fi
OUTFILE="$OUTDIR/${unix.service}.out"
export OUTFILE

# TODO instead of waiting for the marker we should ping yurconf server via http?
waitForMarker() {
    OUT="$1"
    while [ ! -f $OUT ]; do
        sleep 0.3
    done
    IDX=0
    while [ $IDX -ne $MAX_WAIT_MARKER_ITER ]; do
        # Only standard marker should be considered
        MARKER=$(cat $OUT | grep -e 'Yurconf Server started in' )
        if [ "x$MARKER" != "x" ]; then
            return 0
        else
            if running $PIDFILE ; then
                sleep 0.5
                IDX=$(expr $IDX + 1)
                if [ "x" = "x$(netstat -pan | grep $YURCONF_PORT)" ]; then
                    return 0
                fi
            else
                return 2
            fi
        fi
    done
    return 1
}

running()
{
    [ -f $1 ] || return 1
    PID=$(cat $1)
    ps -p $PID >/dev/null 2>/dev/null || return 1
    return 0
}

doStart()
{
    MARKER_FOUND=false
    if running $PIDFILE ; then
    	echo "Yurconf Server probably is already running"
    	return 1
    else
        rm -f $OUTFILE
        ARGS="--daemon"
        touch $PIDFILE
        chown $DAEMONUSER $PIDFILE
        su - $DAEMONUSER -c "
			$DAEMON $ARGS &
			PID=\$!
			disown \$PID
			echo \$PID > $PIDFILE"
        if waitForMarker $OUTFILE ; then
            MARKER_FOUND=true
            echo "Found marker in out file. Should be started now."
        fi
    fi
    if $MARKER_FOUND ; then
        return 0
    else
    	echo "Could not find marker"
        return 1
    fi
}

doStop()
{
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
        if [ -n "$PID" ]; then
            if [ ! "x" = "x$(ps --no-headers -p $PID)" ]; then
                kill $PID
                # Wait maximum times
                ITER=0
                while  [ ! "x" = "x$(ps --no-headers -p $PID)" ]; do
                    # TODO some applications may require a time to shut down. Need to check if processes quit, then continue
                    sleep 0.1
                    ITER=$(expr $ITER + 1)
                    if [ $ITER -eq $MAX_WAIT_CHILD_ITER ]; then
                        break
                    fi
                done
            fi
            if [ ! "x" = "x$(ps --no-headers -p $PID)" ]; then
            	echo "Still alive... killing it"
                kill -9 $PID
                sleep 1
            fi
        fi
    	rm -f $PIDFILE
    fi
}

case "$1" in
  start)
        echo "Starting $SERVICENAME Yurconf Server"
        doStart
        ;;
  stop)
        echo "Stopping $SERVICENAME Yurconf Server"
        doStop
         ;;
  restart)
        echo "Restarting $SERVICENAME Yurconf Server"
        doStop
        doStart
        ;;
  status)
        status -p $PIDFILE $DAEMON && exit 0 || exit $?
        ;;
  *)
        echo "Usage: service $SERVICENAME {start|stop|restart|status}" >&2
        exit 3
        ;;
esac

:
