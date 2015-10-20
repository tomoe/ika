#!/bin/bash -x

THIS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

BGPD=/usr/lib/quagga/bgpd
PORT=2000 # this is different from the standard port as Ika server listens it
CONF_FILE=$THIS_DIR/bgpd.conf

$BGPD -p $PORT -f $CONF_FILE


