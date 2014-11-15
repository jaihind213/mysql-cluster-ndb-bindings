dnl -*- mode: m4; c-basic-offset: 2; indent-tabs-mode: nil; -*-
dnl vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
dnl   
dnl ndb-bindings
dnl Copyright (C) 2008 MySQL
dnl 
dnl This program is free software; you can redistribute it and/or modify
dnl it under the terms of the GNU General Public License as published by
dnl the Free Software Foundation; either version 2 of the License, or
dnl (at your option) any later version.
dnl 
dnl This program is distributed in the hope that it will be useful,
dnl but WITHOUT ANY WARRANTY; without even the implied warranty of
dnl MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
dnl GNU General Public License for more details.
dnl 
dnl You should have received a copy of the GNU General Public License
dnl along with this program; if not, write to the Free Software
dnl Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA




 
AC_DEFUN([WITH_MYSQL], [ 
  AC_REQUIRE([AC_PROG_CC])
  AC_REQUIRE([AC_PROG_CXX])
  AC_MSG_CHECKING(for mysql_config executable)

  AC_ARG_WITH([mysql],
    [AS_HELP_STRING([--with-mysql=PATH],
        [path to mysql_config binary or mysql prefix dir])], 
      [with_mysql=$withval],
      [with_mysql=mysql_config])

   AS_IF([test -f $with_mysql -a -x $with_mysql],
      [MYSQL_CONFIG=$with_mysql],
      [AS_IF([test -f $with_mysql/bin/mysql_config -a -x $with_mysql/bin/mysql_config],
        [MYSQL_CONFIG=$with_mysql/bin/mysql_config],
	[AS_IF([test -d $with_mysql],
	  [MYSQL_CONFIG="ISDIR"])]
    )])
        
  AS_IF([test "x$with_mysql" = "xmysql_config"],
    AC_CHECK_PROGS(MYSQL_CONFIG,[$with_mysql]))

  AS_IF([test "x$MYSQL_CONFIG" = "x"],
    AC_MSG_ERROR([MySQL not found]))

    AC_LANG(C++)

  AS_IF([test "x$MYSQL_CONFIG" = "xISDIR"],[
    IBASE="-I${with_mysql}"
    MYSQL_CONFIG="${with_mysql}/scripts/mysql_config"
    ADDIFLAGS="$IBASE/include "
    ADDIFLAGS="$ADDIFLAGS $IBASE/storage/ndb/include/ndbapi "
    ADDIFLAGS="$ADDIFLAGS $IBASE/storage/ndb/include/mgmapi "
    ADDIFLAGS="$ADDIFLAGS $IBASE/storage/ndb/include "
    LDFLAGS="-L${with_mysql}/storage/ndb/src/.libs -L${with_mysql}/libmysql_r/.libs/ -L${with_mysql}/mysys/.libs -L${with_mysql}/mysys -L${with_mysql}/strings/.libs -L${with_mysql}/strings "
  ],[
    IBASE=`$MYSQL_CONFIG --include`
    ADDIFLAGS=""
    # add regular MySQL C flags
    ADDCFLAGS=`$MYSQL_CONFIG --cflags` 
    # add NdbAPI specific C flags
    LDFLAGS="$LDFLAGS "`$MYSQL_CONFIG --libs_r | sed 's/-lmysqlclient_r//'`
    ])


    ADDIFLAGS="$ADDIFLAGS $IBASE/storage/ndb"
    ADDIFLAGS="$ADDIFLAGS $IBASE/storage/ndb/ndbapi"
    ADDIFLAGS="$ADDIFLAGS $IBASE/storage/ndb/mgmapi"
    ADDIFLAGS="$ADDIFLAGS $IBASE/ndb"
    ADDIFLAGS="$ADDIFLAGS $IBASE/ndb/ndbapi"
    ADDIFLAGS="$ADDIFLAGS $IBASE/ndb/mgmapi"
    ADDIFLAGS="$ADDIFLAGS $IBASE"

    CFLAGS="$CFLAGS $ADDCFLAGS $ADDIFLAGS"    
    CXXFLAGS="$CXXFLAGS $ADDCFLAGS $ADDIFLAGS" 
    MYSQL_INCLUDES="$IBASE $ADDIFLAGS"   

    
    dnl AC_CHECK_LIB([mysqlclient_r],[safe_mutex_init],,[AC_MSG_ERROR([Can't link against libmysqlclient_r])])
    dnl First test to see if we can run with only ndbclient
    AC_CHECK_LIB([ndbclient],[decimal_bin_size],,[dnl else
      LDFLAGS="$LDFLAGS -lmysys -ldbug"
      AC_CHECK_LIB([mysqlclient_r],[safe_mutex_init],,)
      AC_CHECK_LIB([ndbclient],[ndb_init],,[
        AC_MSG_ERROR([Can't link against libndbclient])])
      AC_CHECK_LIB([mystrings],[decimal_bin_size],,[
          AC_MSG_ERROR([Can't find decimal_bin_size])])])
    AC_MSG_CHECKING(for NdbApi headers)
     AC_LINK_IFELSE([AC_LANG_PROGRAM([[#include <NdbApi.hpp>]], [[int attr=NdbTransaction::Commit; ]])],[ndbapi_found="yes"],[])
    AS_IF([test "$ndbapi_found" = "yes"], 
       [AC_MSG_RESULT(found)],
       [AC_MSG_ERROR([Couldn't find NdbApi.hpp!])])
    AC_MSG_CHECKING(for NDB_LE_ThreadConfigLoop)
      AC_LINK_IFELSE([AC_LANG_PROGRAM([[#include <mgmapi.h>]], [[int attr=NDB_LE_ThreadConfigLoop; ]])],[have_cge63="yes"],[])
      AS_IF([test "$have_cge63" = "yes"],
        [AC_MSG_RESULT(found)
         HAVE_CGE63="-DCGE63"
         AC_SUBST(HAVE_CGE63)],
        [AC_MSG_RESULT(missing)])

    LDFLAGS="$LDFLAGS $LIBS"
  

    MYSQL_MAJOR_VERSION=`$MYSQL_CONFIG --version | sed -e 's/\.//g' -e 's/-//g' -e 's/[A-Za-z]//g' | cut -c1-2`

    case "$MYSQL_MAJOR_VERSION" in 
      50) AC_DEFINE(MYSQL_50, [1], [mysql5.0])
	;;
      51) AC_DEFINE(MYSQL_51, [1], [mysql5.1])
        ;;
      *) echo "Unsupported version of MySQL Detected!"
        ;;
     esac
    
    AC_SUBST(MYSQL_MAJOR_VERSION)
    AC_SUBST(MYSQL_CONFIG)
    
  
])

