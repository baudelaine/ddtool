#!/bin/bash

JAVAC=$(command -v javac)

if [ "${JAVAC}" == "" ]; then
	echo "ERROR !!! javac not found PATH. Exiting..."
	exit 1
fi

rm -rf WebContent/WEB-INF/classes/*

javac -cp wlp/*:WebContent/WEB-INF/lib/* -d WebContent/WEB-INF/classes/ src/com/baudelaine/dd/*.java src/datadixit/limsbi/svc/*.java src/datadixit/limsbi/cognos/*.java src/datadixit/limsbi/db/*.java src/datadixit/limsbi/properties/*.java src/datadixit/limsbi/pojos/*.java src/datadixit/limsbi/action/*.java

exit 0
