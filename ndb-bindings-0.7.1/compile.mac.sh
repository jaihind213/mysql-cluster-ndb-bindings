#!/bin/sh

#################
#set variable MYSQL_CLUSTER_HOME as it needs the libndbclient lib
MYSQL_CLUSTER_HOME=
#################

CURR_DIR=`pwd`
LIB_INSTALL_DIR="${CURR_DIR}/ndbj_libs_mac"

mkdir -p $LIB_INSTALL_DIR
mkdir -p $LIB_INSTALL_DIR/lib

echo .
echo .
echo "Compilation requires swig 1.3.31 !!!!!"
echo .
echo .


echo "Running autogen.."
./autogen.sh

if [ "$MYSQL_CLUSTER_HOME" == "" ];then
	echo "please variable MYSQL_CLUSTER_HOME as it needs the libndbclient lib"
        exit 0
fi

ant_installed=`which ant`
echo $ant_installed

if [ "$ant_installed" == "" ];then
        echo "please add ant binary to PATH"
        exit 0
fi

if [ "$JAVA_HOME" == "" ];then
        echo "please variable JAVA_HOME"
        exit 0
fi

echo "RUNNING_CONFIGURE..."

./configure --with-mysql=$MYSQL_CLUSTER_HOME --with-csharp=no --with-python=no --with-lua=no --with-ruby=no --with-perl=no --prefix=$LIB_INSTALL_DIR

echo "RUNNING_MAKE..."
make
echo "RUNNING_MAKE_INSTAL..."

make install

echo .
echo .

echo "-----------------------------------------------------------------------------" 
echo "You will need libndbclient.6.0.0.dylib  also , so "
echo "copying libndbclient.6.0.0.dylib  from $MYSQL_CLUSTER_HOME/lib to $LIB_INSTALL_DIR "
cp $MYSQL_CLUSTER_HOME/lib/libndbclient.6.0.0.dylib  $LIB_INSTALL_DIR/lib
echo "The libraries are available here: $LIB_INSTALL_DIR/lib"
echo "-----------------------------------------------------------------------------"
