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


AC_DEFUN([WITH_RUBY], [

  AC_ARG_WITH([ruby],
    [AS_HELP_STRING([--with-ruby],
      [BUILD NDB/Ruby @<:@default=yes@:>@])],
      [with_ruby=$withval],
      [with_ruby=ruby])

  AS_IF([test "x$with_ruby" != "xno"],[
    AS_IF([test "x$with_ruby" != "xyes"],
      [ac_chk_ruby=$with_ruby],
      [ac_chk_ruby=ruby1.8 ruby])
    AC_CHECK_PROGS(RUBY,$ac_chk_ruby)
  ])

  AS_IF([test "x$RUBY" != "x"],[

    AC_MSG_CHECKING(for ruby devel)

    dnl need to change quotes to allow square brackets
    changequote(<<, >>)dnl
    ruby_prefix=`$RUBY -rrbconfig -e "print Config::CONFIG['archdir']"`
    strip_ruby_prefix=`$RUBY -rrbconfig -e "print Config::CONFIG['prefix']" | sed 's/\//./g'`
    
    RUBY_LIB=`$RUBY -rrbconfig -e "puts Config::CONFIG['ruby_install_name']"`
    LIBRUBYARG_SHARED=`$RUBY -rrbconfig -e "puts Config::CONFIG['LIBRUBYARG_SHARED']"`
    RUBY_DIR=`$RUBY -rrbconfig -e "puts Config::CONFIG['archdir']"`
    RUBY_ARCH_DIR=`echo $RUBY_DIR | sed "s/$strip_ruby_prefix//"`
    RUBY_LIBDIR=`$RUBY -rrbconfig -e "puts Config::CONFIG['rubylibdir']"`
    RUBY_INCLUDES="-I$ruby_prefix"
    changequote([, ])dnl

    ac_save_CFLAGS="$CFLAGS"
    ac_save_CPPFLAGS="$CPPFLAGS"
    ac_save_LDFLAGS="$LDFLAGS"
    CFLAGS="$ac_save_CFLAGS $RUBY_INCLUDES"
    CPPFLAGS="$ac_save_CPPFLAGS $RUBY_INCLUDES"
    LDFLAGS="$ac_save_LDFLAGS $LIBRUBYARG_SHARED"
    
    AC_LINK_IFELSE([AC_LANG_PROGRAM([[#include <ruby.h>]], [[VALUE rb_ac_test = rb_define_module("actest");]])],[with_ruby="yes";AC_MSG_RESULT(found)],[with_ruby="no";AC_MSG_RESULT(missing)])

    CPPFLAGS="$ac_save_CPPFLAGS"
    CFLAGS="$ac_save_CFLAGS"
    LDFLAGS="$ac_save_LDFLAGS"
  ],[
    # This allows 'make clean' in the ruby directory to work when
    # ruby isn't available
    RUBY=
    RUBY_INCLUDES=
    LIBRUBYARG_SHARED=
    RUBY_LIB=
    RUBY_DIR=
    RUBY_LIBDIR=
    RUBY_ARCH_DIR=
    with_ruby="no"
  ])

  AC_SUBST(RUBY_INCLUDES)
  AC_SUBST(LIBRUBYARG_SHARED)
  AC_SUBST(RUBY_LIB)
  AC_SUBST(RUBY_DIR)
  AC_SUBST(RUBY_LIBDIR)
  AC_SUBST(RUBY_ARCH_DIR)

  AS_IF([test "x$RUBY_DIR" = "x"],[with_ruby="no"])
])
