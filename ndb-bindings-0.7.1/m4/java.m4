dnl ===========================================================================
dnl Autoconf Support for Java
dnl ===========================================================================


AC_DEFUN([AC_CHECK_JAVA_HOME], [
  AC_REQUIRE([AC_EXEEXT])

  AC_CACHE_CHECK([for JAVA_HOME environment], ac_cv_java_home_env,[
    if test -z "$JAVA_HOME"; then
      ac_cv_java_home_env="no"
    else
      ac_cv_java_home_env="$JAVA_HOME"
    fi
  ])

  if test "$ac_cv_java_home_env" = no; then
    AC_PATH_PROG([JAVAC_PATH],[javac$EXEEXT],[no])
    if test "$JAVAC_PATH" != "no"; then
      JAVA_HOME="$JAVAC_PATH";

      # Unwind symlinks found. This can experience infinite loop
      # so we throw in a sleep so that it won't be a cpu hog.

      link=${JAVAC_PATH}
      while true
      do
          case "$link" in
          /*)
              ;;
          *)
              link="$PWD/$link"
              ;;
          esac
          tmp=`ls -l $link | awk '/->/ {print $NF}'`
          if test -n "$tmp" ; then
              case "$tmp" in
              /*)
                  ;;
              *)
                  tmp="`dirname $link`/$tmp"
                  ;;
              esac
              link=$tmp
          else
              JAVA_HOME="$link"
              break
          fi
          sleep 1
      done


      JAVA_HOME="`echo $JAVA_HOME | sed 's%/[[^/]]*/[[^/]]*$%%'`"

    fi
  else
    JAVA_HOME="$ac_cv_java_home_env"
  fi

  AC_SUBST(JAVA_HOME)
AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_CHECK_JAVA_VENDOR], [
  AC_REQUIRE([AC_CHECK_JAVA_HOME])

  AC_CACHE_CHECK([style of JDK environment], ac_cv_java_style,[
    ac_cv_java_style="unknown"
    if test -d "$JAVA_HOME/../Commands" -a \
              -d "$JAVA_HOME/../Headers" -a \
              -d "$JAVA_HOME/../Libraries"; then
      ac_cv_java_style="Apple"
    elif test -d "$JAVA_HOME/Commands" -a \
              -d "$JAVA_HOME/Headers" ; then 
       if test -d "$JAVA_HOME/Libraries"; then
          ac_cv_java_style="AppleDirect"
       elif test -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK" ; then
          AC_MSG_WARN([javac in path didn't have Libraries - using CurrentJDK instead])
          ac_cv_java_style="AppleDirect"
          JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK"
          JAVAC="$JAVA_HOME/Commands/javac"
          JAVA="$JAVA_HOME/Commands/java"
       fi 
    elif test -d "$JAVA_HOME/bin" -a \
            -d "$JAVA_HOME/include"; then
      if test -d "$JAVA_HOME/jre/lib"; then
        ac_cv_java_style="Sun"
      elif test -d "$JAVA_HOME/jre/bin"; then
        ac_cv_java_style="IBM"
      else 

        if test -z "$JAVAINCDIR" ; then
          

          # Add in default installation directory on Windows for Cygwin
          case $host in
            *-*-cygwin* | *-*-mingw*) 
              JAVAINCDIR="c:/Program*Files/Java/jdk*/include d:/Program*Files/Java/jdk*/include c:/j2sdk*/include d:/j2sdk*/include c:/jdk*/include d:/jdk*/include $JAVAINCDIR"
            ;;
            *-*-darwin*) 
              JAVAINCDIR="/System/Library/Frameworks/JavaVM.framework/Headers $JAVAINCDIR"
            ;;
            *)
              JAVAINCDIR="/usr/j2sdk*/include /usr/local/j2sdk*/include /usr/jdk*/include /usr/local/jdk*/include /opt/j2sdk*/include /opt/jdk*/include /usr/java/include /usr/java/j2sdk*/include /usr/java/jdk*/include /usr/local/java/include /opt/java/include /usr/include/java /usr/local/include/java /usr/lib/java/include /usr/lib/jvm/java*/include /usr/include/kaffe /usr/local/include/kaffe /usr/include"
            ;;
          esac
        fi

        JAVAINC=""
        for d in $JAVAINCDIR ; do
          if test -r "$d/jni.h" ; then
            JAVAINCDIR=$d
            JAVAINC=-I\"$d\"
            break
          fi
        done
        if test "x$JAVAINC" = "x" ; then
          ac_cv_java_style=unknown
        else
          # now look for <arch>/jni_md.h
          JAVAMDDIR=`find "$JAVAINCDIR" -follow -name jni_md.h -print`
          if test "$JAVAMDDIR" = "" ; then
            ac_cv_java_style=unknown
	   AC_MSG_RESULT([unkown])
          else
            ac_cv_java_style=GCJ
            JAVAMDDIR=`dirname "$JAVAMDDIR" | tail -1`
            JAVAINC="${JAVAINC} -I\"$JAVAMDDIR\""
          fi
        fi


      fi
    fi
  ])

  case "$ac_cv_java_style" in
  IBM)
    JAVAOPTS=""
    JAVA_BIN="$JAVA_HOME/bin"
    JAVA_INC="$JAVA_HOME/include"
    JAVA_LIB="$JAVA_HOME/jre/bin"
    ;;
  Sun)
    JAVAOPTS=""
    JAVA_BIN="$JAVA_HOME/bin"
    JAVA_INC="$JAVA_HOME/include"
    JAVA_LIB="$JAVA_HOME/jre/lib"
    ;;
  Apple)
    JAVAOPTS=""
    JAVA_BIN="$JAVA_HOME/../Commands"
    JAVA_INC="$JAVA_HOME/../Headers"
    JAVA_LIB="$JAVA_HOME/../Libraries"
    ;;
  AppleDirect)
    JAVAOPTS=""
    JAVA_BIN="$JAVA_HOME/Commands"
    JAVA_INC="$JAVA_HOME/Headers"
    JAVA_LIB="$JAVA_HOME/Libraries"
    ;;
  GCJ)	
# java.exe on Cygwin requires the Windows standard (Pascal) calling convention as it is a normal Windows executable and not a Cygwin built executable
case $host in
*-*-cygwin* | *-*-mingw*)
    if test "$GCC" = yes; then
        JAVADYNAMICLINKING=" -mno-cygwin -mthreads -Wl,--add-stdcall-alias"
        JAVACFLAGS="-mno-cygwin -mthreads"
    else
        JAVADYNAMICLINKING=""
        JAVACFLAGS=""
    fi ;;
*-*-darwin*)
        JAVADYNAMICLINKING="-dynamiclib -framework JavaVM"
        JAVACFLAGS=""
        ;;
*)
        JAVADYNAMICLINKING=""
        JAVACFLAGS=""
        ;;
esac

# Java on Windows platforms including Cygwin doesn't use libname.dll, rather name.dll when loading dlls
case $host in
*-*-cygwin* | *-*-mingw*) JAVALIBRARYPREFIX="";;
*)JAVALIBRARYPREFIX="lib";;
esac

# Java on Mac OS X tweaks
case $host in
*-*-darwin*)
    JAVASO=".jnilib"
    JAVALDSHARED='$(CC)'
    JAVACXXSHARED='$(CXX)'
    ;;
*)
    JAVASO=$SO
    JAVALDSHARED='$(LDSHARED)'
    JAVACXXSHARED='$(CXXSHARED)'
    ;;
esac
    JAVA_INC="$JAVAINCDIR"
    JAVA_BIN="$JAVA_HOME/bin"
    JAVA_LIB="$JAVAINC/.."
    JAVAOPTS="-source 1.5"
    ;;
  *)
    AC_MSG_WARN([Unknown or unsupported JDK])
    ;;
  esac

  JAVAPREFIX="$JAVA_BIN"

  AC_SUBST(JAVAOPTS)
  AC_SUBST(JAVA_BIN)
  AC_SUBST(JAVA_INC)
  AC_SUBST(JAVA_LIB)
  
AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_CHECK_JAVA_LDFLAGS], [
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_CACHE_CHECK([for linker flags for JNI], ac_cv_java_ldflags,[
    case "$ac_cv_java_style" in
    IBM)
      ac_cv_java_ldflags="-L$JAVA_LIB -ljava -lhpi -ljsig"
      ;;
    Apple)
      ac_cv_java_ldflags="-L$JAVA_LIB -lverify -ljvm -framework JavaVM"
      ;;
    GCI)
      ac_cv_java_ldflags="-L$JAVA_LIB -lgcj"
    *)
      if test -d "$JAVA_HOME/jre/lib/$host_cpu"; then
        ac_cv_java_ldflags="-L$JAVA_HOME/jre/lib/$host_cpu -ljava -lverify -ljsig"
      else
        case $host_cpu in
        i*86)
          ac_cv_java_ldflags="-L$JAVA_HOME/jre/lib/i386 -ljava -lverify -ljsig"
          ;;
        x86_64)
          ac_cv_java_ldflags="-L$JAVA_HOME/jre/lib/amd64 -ljava -lverify -ljsig"
          ;;
        *)
    	  if test -d "$JAVA_LIB/server"; then
        	ac_cv_java_ldflags="$ac_cv_java_ldflags -L$JAVA_LIB/server -ljvm"
    	  elif test -d "$JAVA_LIB/client"; then
        	ac_cv_java_ldflags="$ac_cv_java_ldflags -L$JAVA_LIB/client -ljvm"
    	  elif test -d "$JAVA_LIB/classic"; then
        	ac_cv_java_ldflags="$ac_cv_java_ldflags -L$JAVA_LIB/classic -ljvm"
          elif test "x$ac_cv_java_style" = "xGCJ" ; then
                ac_cv_java_ldflags="$ac_cv_java_ldflags $JAVADYNAMICLINKING"
    	  else
            AC_MSG_RESULT([unknown])
            AC_MSG_WARN([Failed to identify your JVM])
		  fi
          ;;
        esac
      fi
      ;;
    esac
  ])

  JAVA_LDFLAGS="$ac_cv_java_ldflags"
  AC_SUBST(JAVA_LDFLAGS)

AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_CHECK_JAVA_CXXFLAGS], [
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_CACHE_CHECK([for compile flags for JNI], ac_cv_java_cxxflags,[
    case "$ac_cv_java_style-$host_os" in
    Sun-solaris*)
      ac_cv_java_cxxflags="-I$JAVA_INC -I$JAVA_INC/solaris"
      ;;
    Sun-linux*)
      ac_cv_java_cxxflags="-I$JAVA_INC -I$JAVA_INC/linux"
      ;;
    Sun-freebsd* | Sun-kfreebsd*-gnu | Sun-dragonfly*)
      ac_cv_java_cxxflags="-I$JAVA_INC -I$JAVA_INC/freebsd"
      ;;
    *)
      if test "x$ac_cv_java_style" = "xGCJ" ; then 
        ac_cv_java_cxxflags="-I$JAVA_INC -IJAVAMDDIR $JAVACFLAGS"
      else
        ac_cv_java_cxxflags="-I$JAVA_INC"
      fi
      ;;
    esac
  ])
  
  JAVA_CXXFLAGS="$ac_cv_java_cxxflags"
  AC_SUBST(JAVA_CXXFLAGS)

AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_PROG_JAVA], [
  AC_REQUIRE([AC_EXEEXT])
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_PATH_PROG([JAVA], [java$EXEEXT], [no], [$JAVA_BIN])
AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_PROG_JAVAC], [
  AC_REQUIRE([AC_EXEEXT])
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_PATH_PROG([JAVAC], [javac$EXEEXT], [no], [$JAVA_BIN])
AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_PROG_JAVAH], [
  AC_REQUIRE([AC_EXEEXT])
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_PATH_PROG([JAVAH], [javah$EXEEXT], [no], [$JAVA_BIN])
AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_PROG_JAR], [
  AC_REQUIRE([AC_EXEEXT])
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_PATH_PROG([JAR],[jar$EXEEXT],[no],[$JAVA_BIN])
AC_PROVIDE([$0])dnl
])

AC_DEFUN([AC_PROG_JAVADOC], [
  AC_REQUIRE([AC_EXEEXT])
  AC_REQUIRE([AC_CHECK_JAVA_VENDOR])

  AC_PATH_PROG([JAVADOC],[javadoc$EXEEXT],[no],[$JAVA_BIN])
AC_PROVIDE([$0])dnl
])


AC_DEFUN([AC_CHECK_JNI_COMPILE], [
  AC_REQUIRE([AC_CHECK_JAVA_LDFLAGS])
  AC_REQUIRE([AC_CHECK_JAVA_CXXFLAGS])

  AC_CACHE_CHECK([JNI headers and libraries works], ac_cv_jni_works,[
    ac_save_CXXFLAGS="$CXXFLAGS"
    ac_save_LDFLAGS="$LDFLAGS"
    AC_LANG_PUSH([C++])
    
    CXXFLAGS="$CXXFLAGS $JAVA_CXXFLAGS"
    LDFLAGS="$LDFLAGS $JAVA_LDFLAGS"
    
    AC_LINK_IFELSE([AC_LANG_PROGRAM([[#include <jni.h>]], [[
      JavaVM *vm;
      JNIEnv *env;
      JavaVMInitArgs args;
      jint res;
     
      args.version= JNI_VERSION_1_2;
      args.options= NULL;
      args.nOptions= 0;
      
      res= JNI_CreateJavaVM(&vm, (void **) &env, (void *) &args);
    ]])],[ac_cv_jni_works=yes],[ac_cv_jni_works=no])
    
    AC_LANG_POP([])
    CXXFLAGS="$ac_save_CXXFLAGS"
    LDFLAGS="$ac_save_LDFLAGS"
  ])
  if test "$ac_cv_jni_works" != yes; then
    AC_MSG_WARN([
Linking a simple JNI test program failed. Something is wrong with the JDK
configuration or your installed JDK is not supported yet.])
  fi
AC_PROVIDE([$0])dnl
])
      


##### http://autoconf-archive.cryp.to/ac_prog_java_works.html
#
# SYNOPSIS
#
#   AC_PROG_JAVA_WORKS
#
# DESCRIPTION
#
#   Internal use ONLY.
#
#   Note: This is part of the set of autoconf M4 macros for Java
#   programs. It is VERY IMPORTANT that you download the whole set,
#   some macros depend on other. Unfortunately, the autoconf archive
#   does not support the concept of set of macros, so I had to break it
#   for submission. The general documentation, as well as the sample
#   configure.in, is included in the AC_PROG_JAVA macro.
#
# LAST MODIFICATION
#
#   2000-07-19
#
# COPYLEFT
#
#   Copyright (c) 2000 Stephane Bortzmeyer <bortzmeyer@pasteur.fr>
#
#   This program is free software; you can redistribute it and/or
#   modify it under the terms of the GNU General Public License as
#   published by the Free Software Foundation; either version 2 of the
#   License, or (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful, but
#   WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
#   General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
#   02111-1307, USA.
#
#   As a special exception, the respective Autoconf Macro's copyright
#   owner gives unlimited permission to copy, distribute and modify the
#   configure scripts that are the output of Autoconf when processing
#   the Macro. You need not follow the terms of the GNU General Public
#   License when using or distributing such scripts, even though
#   portions of the text of the Macro appear in them. The GNU General
#   Public License (GPL) does govern all other use of the material that
#   constitutes the Autoconf Macro.
#
#   This special exception to the GPL applies to versions of the
#   Autoconf Macro released by the Autoconf Macro Archive. When you
#   make and distribute a modified version of the Autoconf Macro, you
#   may extend this special exception to the GPL to apply to your
#   modified version as well.

AC_DEFUN([AC_PROG_JAVA_WORKS], [
AC_CHECK_PROG(uudecode, uudecode$EXEEXT, yes)
if test x$uudecode = xyes; then
AC_CACHE_CHECK([if uudecode can decode base 64 file], ac_cv_prog_uudecode_base64, [
dnl /**
dnl  * Test.java: used to test if java compiler works.
dnl  */
dnl public class Test
dnl {
dnl
dnl public static void
dnl main( String[] argv )
dnl {
dnl     System.exit (0);
dnl }
dnl
dnl }
cat << \EOF > Test.uue
begin-base64 644 Test.class
yv66vgADAC0AFQcAAgEABFRlc3QHAAQBABBqYXZhL2xhbmcvT2JqZWN0AQAE
bWFpbgEAFihbTGphdmEvbGFuZy9TdHJpbmc7KVYBAARDb2RlAQAPTGluZU51
bWJlclRhYmxlDAAKAAsBAARleGl0AQAEKEkpVgoADQAJBwAOAQAQamF2YS9s
YW5nL1N5c3RlbQEABjxpbml0PgEAAygpVgwADwAQCgADABEBAApTb3VyY2VG
aWxlAQAJVGVzdC5qYXZhACEAAQADAAAAAAACAAkABQAGAAEABwAAACEAAQAB
AAAABQO4AAyxAAAAAQAIAAAACgACAAAACgAEAAsAAQAPABAAAQAHAAAAIQAB
AAEAAAAFKrcAErEAAAABAAgAAAAKAAIAAAAEAAQABAABABMAAAACABQ=
====
EOF
if uudecode$EXEEXT Test.uue; then
        ac_cv_prog_uudecode_base64=yes
else
        echo "configure: __oline__: uudecode had trouble decoding base 64 file 'Test.uue'" >&AS_MESSAGE_LOG_FD
        echo "configure: failed file was:" >&AS_MESSAGE_LOG_FD
        cat Test.uue >&AS_MESSAGE_LOG_FD
        ac_cv_prog_uudecode_base64=no
fi
rm -f Test.uue])
fi
if test x$ac_cv_prog_uudecode_base64 != xyes; then
        rm -f Test.class
        AC_MSG_WARN([I have to compile Test.class from scratch])
        if test x$ac_cv_prog_javac_works = xno; then
                AC_MSG_WARN([Cannot compile java source. $JAVAC does not work properly])
        else
                if test x$ac_cv_prog_javac_works = x; then
                        AC_PROG_JAVAC
                fi
		fi
fi
AC_CACHE_CHECK(if $JAVA works, ac_cv_prog_java_works, [
JAVA_TEST=Test.java
CLASS_TEST=Test.class
TEST=Test
changequote(, )dnl
cat << \EOF > $JAVA_TEST
/* [#]line __oline__ "configure" */
public class Test {
public static void main (String args[]) {
        System.exit (0);
} }
EOF
changequote([, ])dnl
if test x$ac_cv_prog_uudecode_base64 != xyes; then
        if AC_TRY_COMMAND($JAVAC $JAVACFLAGS $JAVA_TEST) && test -s $CLASS_TEST; then
                :
        else
          echo "configure: failed program was:" >&AS_MESSAGE_LOG_FD
          cat $JAVA_TEST >&AS_MESSAGE_LOG_FD
          AC_MSG_WARN(The Java compiler $JAVAC failed (see config.log, check the CLASSPATH?))
        fi
fi
if AC_TRY_COMMAND($JAVA $JAVAFLAGS $TEST) >/dev/null 2>&1; then
  ac_cv_prog_java_works=yes
else
  echo "configure: failed program was:" >&AS_MESSAGE_LOG_FD
  cat $JAVA_TEST >&AS_MESSAGE_LOG_FD
  AC_MSG_WARN(The Java VM $JAVA failed (see config.log, check the CLASSPATH?))
fi
rm -fr $JAVA_TEST $CLASS_TEST Test.uue
])
AC_PROVIDE([$0])dnl
]
)



##### http://autoconf-archive.cryp.to/ac_prog_javac_works.html
#
# SYNOPSIS
#
#   AC_PROG_JAVAC_WORKS
#
# DESCRIPTION
#
#   Internal use ONLY.
#
#   Note: This is part of the set of autoconf M4 macros for Java
#   programs. It is VERY IMPORTANT that you download the whole set,
#   some macros depend on other. Unfortunately, the autoconf archive
#   does not support the concept of set of macros, so I had to break it
#   for submission. The general documentation, as well as the sample
#   configure.in, is included in the AC_PROG_JAVA macro.
#
# LAST MODIFICATION
#
#   2000-07-19
#
# COPYLEFT
#
#   Copyright (c) 2000 Stephane Bortzmeyer <bortzmeyer@pasteur.fr>
#
#   This program is free software; you can redistribute it and/or
#   modify it under the terms of the GNU General Public License as
#   published by the Free Software Foundation; either version 2 of the
#   License, or (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful, but
#   WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
#   General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
#   02111-1307, USA.
#
#   As a special exception, the respective Autoconf Macro's copyright
#   owner gives unlimited permission to copy, distribute and modify the
#   configure scripts that are the output of Autoconf when processing
#   the Macro. You need not follow the terms of the GNU General Public
#   License when using or distributing such scripts, even though
#   portions of the text of the Macro appear in them. The GNU General
#   Public License (GPL) does govern all other use of the material that
#   constitutes the Autoconf Macro.
#
#   This special exception to the GPL applies to versions of the
#   Autoconf Macro released by the Autoconf Macro Archive. When you
#   make and distribute a modified version of the Autoconf Macro, you
#   may extend this special exception to the GPL to apply to your
#   modified version as well.

AC_DEFUN([AC_PROG_JAVAC_WORKS],[
AC_REQUIRE([AC_PROG_JAVAC])
AC_CACHE_CHECK([if $JAVAC works], ac_cv_prog_javac_works, [
JAVA_TEST=Test.java
CLASS_TEST=Test.class
cat << \EOF > $JAVA_TEST
/* [#]line __oline__ "configure" */
public class Test {
}
EOF
if AC_TRY_COMMAND($JAVAC $JAVACFLAGS $JAVA_TEST) >/dev/null 2>&1; then
  ac_cv_prog_javac_works=yes
else
  AC_MSG_WARN([The Java compiler $JAVAC failed (see config.log, check the CLASSPATH?)])
  echo "configure: failed program was:" >&AS_MESSAGE_LOG_FD
  cat $JAVA_TEST >&AS_MESSAGE_LOG_FD
fi
rm -f $JAVA_TEST $CLASS_TEST
])
AC_PROVIDE([$0])dnl
])
