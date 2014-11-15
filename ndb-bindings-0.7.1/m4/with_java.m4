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


AC_DEFUN([WITH_JAVA], [

  AC_ARG_WITH([java], 
    [AS_HELP_STRING([--with-java],
      [BUILD NDB/J @<:@default=yes@:>@])],
    [with_java=$withval], 
    [with_java=java])

  AS_IF([test "x$with_java" != "xno"],[
    AS_IF([test "x$with_java" != "xyes"],[JAVA=$with_java])   
    AC_PROG_JAVA()
    AS_IF([test "x$JAVA" = "x"],AC_MSG_ERROR([Couldn't find java.]))
    AC_PROG_JAVAC()
    AS_IF([test "x$JAVAC" = "x"],AC_MSG_ERROR([Couldn't find javac.]))
    AC_PROG_JAR()
    AS_IF([test "x$JAR" = "x"],AC_MSG_ERROR([Couldn't find jar.]))
    AC_PROG_JAVADOC()
    AS_IF([test "x$JAVADOC" = "x"],AC_MSG_ERROR([Couldn't find javadoc.]))
    AC_CHECK_JUNIT()
    AC_CHECK_JAVA_CXXFLAGS()
    AC_JAVA_VERSION()
    AS_IF([test $ac_java_version = 4],[
      AC_MSG_ERROR([Java version 1.5 or higher required.])
    ])
    SWAP_JDBC_RESULTSET()
    
    AS_IF([test "x$JAVAC" = "xno"],[
      AS_IF([test "w$with_java" = "xyes"],[
        AC_MSG_ERROR([Unknown or unsupported JDK])
      ],[
        with_java=no
      ])
    ])
    AC_CHECK_PROGS(ANT, [ant])
    AS_IF([test "x$ANT" = "x"],AC_MSG_ERROR([Couldn't find ant.]))
  ])
])
