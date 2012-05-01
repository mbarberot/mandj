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

make $1
