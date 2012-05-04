# !bin/bash


#HOST=$2
#PORT=$3
#SICSTUS=

JDK_HOME=/usr/lib/jvm/java-6-openjdk
JDK_JVM=$JDK_HOME/jre/lib/i386/client
LD_LIBRARY_PATH=$JDK_JVM
export SISCTUS
export HOST
export PORT
export JDK_HOME
export JDK_JVM
export LD_LIBRARY_PATH


# - Argument de l'executable
HOST=localhost
PORT=5555

case "$1" in
    compile)
    make
    ;;
    j1)
    ./bin/client $HOST $PORT
    ;;
    j2)
    cp bin/* j2/bin -R ;
    ./j2/run.sh j2
    ;;
    novalid)
    ../arbitre/arbitre_sans_valide $PORT
    ;;
    notimeout)
    ../arbitre/arbitre_sans_timeout $PORT
    ;;
    arbitre)
    ../arbitre/arbitre $PORT
    ;;
esac