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

AC_DEFUN([WITH_CSHARP], [

  AC_ARG_WITH([csharp], 
    [AS_HELP_STRING([--with-csharp],
      [BUILD NDB/.NET @<:@default=yes@:>@])],
    [with_csharp=$withval], 
    [with_csharp=yes])

  AS_IF([test "x$with_csharp" = "xyes"],[
    AC_CHECK_PROGS(MONO,[mono])
    AC_CHECK_PROGS(MCS,[gmcs])
  ])
  AS_IF([test "x$MONO" = "x"],[with_csharp="no"])
  AS_IF([test "x$MCS" = "x"],[with_csharp="no"])
])
