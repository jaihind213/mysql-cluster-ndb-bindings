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


AC_DEFUN([WITH_PERL], [

  AC_ARG_WITH([perl], 
    [AS_HELP_STRING([--with-perl],
      [BUILD NDB/Perl @<:@default=yes@:>@])],
    [with_perl=$withval], 
    [with_perl=yes])


  AS_IF([test "x$with_perl" != "xno"],[
    AS_IF([test "x$with_perl" != "xyes"],
      [ac_chk_perl=$with_perl],
      [ac_chk_perl=perl])
    AC_CHECK_PROGS(PERL,$ac_chk_perl)
  ])

dnl Don't think we need these anymore, but it's a good reference
dnl if test "x$PERL" != "x"; then
dnl  PERLCCFLAGS=`$PERL -MConfig -e 'print $Config{ccflags};'`
dnl  PERLCPPFLAGS=`$PERL -MConfig -e 'print $Config{cppflags};'`
dnl  PERLLIBS=`$PERL -MConfig -e 'print $Config{perllibs};'`
dnl fi 

])
