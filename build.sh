#!/bin/bash

JAVAC=$(command -v javac)

if [ "${JAVAC}" == "" ]; then
	echo "ERROR !!! javac not found PATH. Exiting..."
	exit 1
fi

rm -rf WebContent/WEB-INF/classes/*

javac -cp wlp/*:WebContent/WEB-INF/lib/* -d WebContent/WEB-INF/classes/ src/com/dma/web/*.java src/com/dma/svc/*.java src/com/dma/cognos/*.java src/com/dma/properties/*.java 

exit 0
