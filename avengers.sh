#! /bin/bash
if [[ "$1" != "" ]]; then
    DB_USERNAME="$1"
else
    DB_USERNAME="DEFAULT DB_USERNAME REPLACE ME"
fi

if [[ "$2" != "" ]]; then
    DB_PASS="$2"
else
    DB_PASS="DEFAULT DB_PASS REPLACE ME"
fi

if [[ "$3" != "" ]]; then
    MARVEL_PUBK="$3"
else
    MARVEL_PUBK="DEFAULT MARVEL_PUBK REPLACE ME"
fi

if [[ "$4" != "" ]]; then
    MARVEL_PRIVK="$4"
else
    MARVEL_PRIVK="DEFAULT MARVEL_PRIVK REPLACE ME"
fi

cd target || { echo "Cannot find target folder, please build app first with assemble.sh"; exit 1; }

commandToRun="java -jar comics-0.0.1-SNAPSHOT.jar --DB_USERNAME=$DB_USERNAME --DB_PASS=$DB_PASS --MARVEL_PUBK=$MARVEL_PUBK --MARVEL_PRIVK=$MARVEL_PRIVK"

eval $commandToRun

