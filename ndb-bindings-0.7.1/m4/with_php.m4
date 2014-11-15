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


AC_DEFUN([WITH_PHP],[

  AC_ARG_WITH([php],
    [AS_HELP_STRING([--with-php],
      [Build NDB/PHP @<:@default=no@:>@])],
    [with_php=$withval],
    [with_php=no])

  AS_IF([test "x$with_php" != "xno"],[
    dnl We explicitly requested PHP build. Fail on too-young SWIG.
    AS_IF([test "x$SWIG_CAN_BUILD_PHP" != "xyes"],
      [AC_MSG_ERROR("Your version of SWIG is too young to build NDB/PHP. >=1.3.33 is required!")])
    AS_IF([test "x$with_php" != "xyes"],
      [ac_check_php_config=$with_php],
      [ac_check_php_config="php-config php-config5"])
      AC_CHECK_PROGS(PHP_CONFIG, [$ac_check_php_config])
    ])

  AS_IF([test "x$PHP_CONFIG" != "x"],[
    PHP_CFLAGS=`$PHP_CONFIG --includes`
    PHP_CPPFLAGS=`$PHP_CONFIG --includes`
    PHP_LDFLAGS=`$PHP_CONFIG --ldflags`
    PHP_EXTDIR=`$PHP_CONFIG --extension-dir`
    strip_php_prefix=`$PHP_CONFIG --prefix | sed 's/\//./g'`
    PHP_ARCH_DIR=`echo $PHP_EXTDIR | sed "s/$strip_php_prefix//"`
  ],[
    PHP_CFLAGS=
    PHP_CPPFLAGS=
    PHP_LDFLAGS=
    PHP_EXTDIR=
    PHP_ARCH_DIR=
    with_php=no
  ])

  AC_SUBST(PHP_CFLAGS)
  AC_SUBST(PHP_CPPFLAGS)
  AC_SUBST(PHP_LDFLAGS)
  AC_SUBST(PHP_EXTDIR)
  AC_SUBST(PHP_ARCH_DIR)
])

