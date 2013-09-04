#!/bin/sh
# 
# Reads client configuration from ${f.etc.config}
#

# Configuration file that keeps environment start point configuration
CONFIG_FILE="${f.etc.config}"

readConfigParameter() {
	echo "$(cat $CONFIG_FILE | grep "^\s*$1\s*=" | cut -d= -f2 | sed 's/\s//g')"
}

# Configuration start point URL
CONFIG_URL="$(readConfigParameter "url")"
if [ "x" = "x$CONFIG_URL" ]; then
    echo "Failed to read configuration URL from $CONFIG_FILE"
    exit 1
fi

# Configuration environment variant
CONFIG_VARIANTS="$(readConfigParameter "variants")"

export CONFIG_URL
export CONFIG_VARIANTS
