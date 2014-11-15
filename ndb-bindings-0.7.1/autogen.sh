#!/bin/sh
# Taken from lighthttpd server (BSD). Thanks Jan!
# Run this to generate all the initial makefiles, etc.

die() { echo "$@"; exit 1; }

# ACLOCAL=${ACLOCAL:-aclocal}
ACLOCAL_FLAGS="-I m4"
# AUTOHEADER=${AUTOHEADER:-autoheader}
# AUTOMAKE=${AUTOMAKE:-automake}
AUTOMAKE_FLAGS="--add-missing --copy --gnu -W no-portability"
# AUTOCONF=${AUTOCONF:-autoconf}

ARGV0=$0
ARGS="$@"

if test -d ".bzr" ; then
  bzr log --short > ChangeLog || touch ChangeLog
else
  touch ChangeLog
fi


run() {
	echo "$ARGV0: running \`$@' $ARGS"
	$@ $ARGS
}

## jump out if one of the programs returns 'false'
set -e

if test x$ACLOCAL = x; then
  if test \! "x`which aclocal-1.10 2> /dev/null | grep -v '^no'`" = x; then
    ACLOCAL=aclocal-1.10
  elif test \! "x`which aclocal110 2> /dev/null | grep -v '^no'`" = x; then
    ACLOCAL=aclocal110
  elif test \! "x`which aclocal 2> /dev/null | grep -v '^no'`" = x; then
    ACLOCAL=aclocal
  else 
    echo "automake 1.10.x (aclocal) wasn't found, exiting"; exit 0
  fi
fi

if test x$AUTOMAKE = x; then
  if test \! "x`which automake-1.10 2> /dev/null | grep -v '^no'`" = x; then
    AUTOMAKE=automake-1.10
  elif test \! "x`which automake110 2> /dev/null | grep -v '^no'`" = x; then
    AUTOMAKE=automake110
  elif test \! "x`which automake 2> /dev/null | grep -v '^no'`" = x; then
    AUTOMAKE=automake
  else 
    echo "automake 1.10.x wasn't found, exiting"; exit 0
  fi
fi


if test x$AUTOCONF = x; then
  if test \! "x`which autoconf-2.61 2> /dev/null | grep -v '^no'`" = x; then
    AUTOCONF=autoconf-2.61
  elif test \! "x`which autoconf261 2> /dev/null | grep -v '^no'`" = x; then
    AUTOCONF=autoconf261
  elif test \! "x`which autoconf 2> /dev/null | grep -v '^no'`" = x; then
    AUTOCONF=autoconf
  else 
    echo "autoconf 2.61+ wasn't found, exiting"; exit 0
  fi
fi

if test x$AUTOHEADER = x; then
  if test \! "x`which autoheader-2.61 2> /dev/null | grep -v '^no'`" = x; then
    AUTOHEADER=autoheader-2.61
  elif test \! "x`which autoheader261 2> /dev/null | grep -v '^no'`" = x; then
    AUTOHEADER=autoheader261
  elif test \! "x`which autoheader 2> /dev/null | grep -v '^no'`" = x; then
    AUTOHEADER=autoheader
  else 
    echo "autoconf 2.61+ (autoheader) wasn't found, exiting"; exit 0
  fi
fi


run $ACLOCAL $ACLOCAL_FLAGS || die "Can't execute aclocal"
run $AUTOHEADER || die "Can't execute autoheader"

# --add-missing instructs automake to install missing auxiliary files
# and --force to overwrite them if they already exist
run $AUTOMAKE $AUTOMAKE_FLAGS  || die "Can't execute automake"
run $AUTOCONF || die "Can't execute autoconf"


