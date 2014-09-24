#!/bin/sh
#
# Reads Yurconf client configuration file and exports environment variables
#   YURCONF_URL
#   YURCONF_VARIANTS

# Configuration file that keeps environment start point configuration
CONFIG_FILE="${f.config}"

readConfigParameter() {
	echo "$(cat $CONFIG_FILE | grep "^\s*$1\s*=" | cut -d= -f2 | sed 's/\s//g')"
}

# Configuration start point URL
YURCONF_URL="$(readConfigParameter "url")"
if [ "x" = "x$YURCONF_URL" ]; then
    echo "Failed to read configuration URL from $CONFIG_FILE"
    exit 1
fi

# Configuration environment variant
YURCONF_VARIANTS="$(readConfigParameter "variants")"

export YURCONF_URL
export YURCONF_VARIANTS
