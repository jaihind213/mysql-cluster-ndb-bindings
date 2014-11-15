#!/bin/sh

CURR_DIR=`pwd`
LIB_INSTALL_DIR="${CURR_DIR}/ndbj_libs_deb"

mkdir -p $LIB_INSTALL_DIR


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
 
echo "You will need libndbclient.so.6.0.0  also , so "
echo "copying libndbclient.so.6.0.0  from $MYSQL_CLUSTER_HOME/lib to $LIB_INSTALL_DIR "
cp $MYSQL_CLUSTER_HOME/lib/libndbclient.so.6.0.0  $LIB_INSTALL_DIR/lib

