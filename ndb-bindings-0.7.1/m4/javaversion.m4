AC_DEFUN([AC_JAVA_VERSION], [
  AC_REQUIRE([AC_PROG_JAVAC])
  dnl This isn't a true test - but it is good enough for what we need it for
  dnl What we're actually testing here is if we have jdbc4 or not...

  AC_CACHE_CHECK(Java version of $JAVA, ac_java_version, [
    JAVA_TEST=Test.java
    CLASS_TEST=Test.class
    rm -f $JAVA_TEST $CLASS_TEST
    cat << \EOF > $JAVA_TEST
/*  __oline__ "configure" */
import java.sql.RowId;
public class Test {
public RowId getRowId(int arg0) {
throw new RuntimeException("");
} }
EOF
  if AC_TRY_COMMAND($JAVAC $JAVACFLAGS $JAVA_TEST) && test -s $CLASS_TEST; then
    ac_java_version=6
  else
    rm -f $CLASS_TEST $JAVA_TEST
cat << \EOF > $JAVA_TEST
/*  __oline__ "configure" */
public class Test {
@Deprecated
public void testAnnotation(int arg0) {
throw new RuntimeException("");
} }
EOF
    if AC_TRY_COMMAND($JAVAC $JAVACFLAGS $JAVA_TEST) && test -s $CLASS_TEST; then
      ac_java_version=5
    else 
      ac_java_version=4

    fi
  fi
  rm -f $JAVA_TEST $CLASS_TEST
  ])
])

AC_DEFUN([SWAP_JDBC_RESULTSET],[
  AC_REQUIRE([AC_JAVA_VERSION])

  if test "x$ac_java_version" = "x5" ; then 

    if test ! -f trs.java.sav ; then 
      # Want to make sure we only do this once
      cp java/src/java/com/mysql/cluster/ndbj/ThrowingResultSetJDBC4.java trs.java.sav
    fi

    cat << \EOF > java/src/java/com/mysql/cluster/ndbj/ThrowingResultSetJDBC4.java
/* This is a generated file to work around jre5/jre6 issues. If this file 
   is here, the build thinks you are using java5. If you aren't please 
   report a bug */
package com.mysql.cluster.ndbj;

public class ThrowingResultSetJDBC4 {

	public ThrowingResultSetJDBC4() {
		super();
	}

}
EOF
  else 
    if test -f trs.java.sav ; then 
      mv trs.java.sav java/src/java/com/mysql/cluster/ndbj/ThrowingResultSetJDBC4.java
    fi 
  fi 
])
