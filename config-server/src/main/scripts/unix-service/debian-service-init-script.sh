#! /bin/sh
### BEGIN INIT INFO
# Provides:          ${unix.service.name}
# Required-Start:    $local_fs $remote_fs $network $syslog
# Required-Stop:     $local_fs $remote_fs $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: ${project.name} initscript
# Description:       This script controls the Baxter ${project.name} service
### END INIT INFO

set -e

. /lib/lsb/init-functions

# Daemon process id will be written here
PIDFILE="/var/run/${unix.service.name}.pid"

# Daemon start script
DAEMON="${unix.application.start.script}"
DAEMONUSER=${unix.user}

MAX_WAIT_CHILD_ITER=50
MAX_WAIT_THIS_ITER=20
MAX_WAIT_MARKER_ITER=500

waitForMarker() {
    OUT="$1"
    while [ ! -f $OUT ]; do
        sleep 0.3
    done
    IDX=0
    while [ $IDX -ne $MAX_WAIT_MARKER_ITER ]; do
        # Only standard marker should be considered
        MARKER=$(cat $OUT | grep -e 'Application started' -e 'OK....' -e 'Started in ' -e '::Started ')
        if [ "x$MARKER" != "x" ]; then
            return 0
        else
            if pidofproc -p $PIDFILE $DAEMON >/dev/null ; then
                sleep 0.5
                IDX=$(expr $IDX + 1)
            else
                return 2
            fi
        fi
    done
    return 1
}

doStart()
{
    # Daemon will be started with this user
    MARKER_FOUND=false
    if start-stop-daemon --test --start --quiet --chuid $DAEMONUSER --make-pidfile --pidfile $PIDFILE --background --exec $DAEMON > /dev/null; then
        OUTFILE="${unix.application.out}"
        rm -f $OUTFILE
        ARGS="--daemon"
        if start-stop-daemon --start --quiet --chuid $DAEMONUSER --make-pidfile --pidfile $PIDFILE --background --exec $DAEMON -- $ARGS ; then
            if waitForMarker $OUTFILE ; then
                MARKER_FOUND=true
            fi
        fi
    fi
    if $MARKER_FOUND ; then
        log_end_msg 0
    else
        log_end_msg 1
    fi
}

doStop()
{
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
        if [ -n "$PID" ]; then
            if [ ! "x" = "x$(ps --no-headers --ppid $PID)" ]; then
                pkill -P $PID
                # Wait maximum times
                ITER=0
                while  [ ! "x" = "x$(ps --no-headers --ppid $PID)" ]; do
                    # TODO some applications may require a time to shut down. Need to check if processes quit, then continue
                    sleep 0.1
                    ITER=$(expr $ITER + 1)
                    if [ $ITER -eq $MAX_WAIT_CHILD_ITER ]; then
                        break
                    fi 
                done
            fi
        fi
    fi
    if start-stop-daemon --stop --signal 1 --quiet --oknodo --pidfile $PIDFILE ; then
        ITER=0
        while pidofproc -p $PIDFILE $DAEMON > /dev/null
        do
            sleep 0.1
            ITER=$(expr $ITER + 1)
            if [ $ITER -eq $MAX_WAIT_THIS_ITER ]; then
                if [ -f $PIDFILE ]; then
                    kill -9 $(cat $PIDFILE)
                fi
                break
            fi 
        done
        rm -f $PIDFILE
        log_end_msg 0
    else
        log_end_msg 1
    fi
}

SERVICENAME="${project.name}"

case "$1" in
  start)
        log_daemon_msg "Starting $SERVICENAME" "${unix.service.name}"
        doStart
        ;;
  stop)
        log_daemon_msg "Stopping $SERVICENAME" "${unix.service.name}"
        doStop
         ;;
  restart)
        log_daemon_msg "Restarting $SERVICENAME" "${unix.service.name}"
        doStop
        doStart
        ;;
  status)
        status_of_proc -p $PIDFILE $DAEMON "$SERVICENAME" && exit 0 || exit $?
        ;;
  *)
        SVC="${unix.service.name}"
        echo "Usage: service $SVC {start|stop|restart|status}" >&2
        exit 3
        ;;
esac

:
