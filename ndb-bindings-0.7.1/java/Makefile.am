AM_CXXFLAGS = $(GLOBAL_CPPFLAGS) $(JAVA_CXXFLAGS)

lib_LTLIBRARIES = libndbj.la libmgmj.la 
nodist_libndbj_la_SOURCES = ndbj.cxx
libndbj_la_LIBADD = ${top_builddir}/ndbpp/libndbpp.la
nodist_libmgmj_la_SOURCES = mgmj.cxx events.cxx listeners.cxx
libmgmj_la_LIBADD = ${top_builddir}/mgmpp/libmgmpp.la

package_namespace=com/mysql/cluster
# automake 1.9 neglects to define this
builddir=$(top_builddir)/java/generated/java
package_builddir=$(builddir)/$(package_namespace)
package_srcdir=$(srcdir)/$(package_namespace)
NDBAPI_SWIG_SRC = ${srcdir}/Ndbj.i $(SWIG_NDB_SOURCES) ${srcdir}/swig/*
MGMAPI_SWIG_SRC = ${srcdir}/Mgmj.i $(SWIG_MGM_SOURCES)
# One file for each swig component used to re-gen all java if it's missing
DEP_NDBJ_SRC=$(builddir)/com/mysql/cluster/ndbj/NdbjJNI.java
DEP_MGMJ_SRC=$(builddir)/com/mysql/cluster/mgmj/MgmjJNI.java
DEP_MGMJ_E_SRC=$(builddir)/com/mysql/cluster/mgmj/events/EventsJNI.java
DEP_MGMJ_L_SRC=$(builddir)/com/mysql/cluster/mgmj/listeners/ListenersJNI.java
CLEANFILES = ndbj.cxx mgmj.cxx ${builddir}/*log ${builddir}/ndbj.jar ${builddir}/*.h events.cxx listeners.cxx

EXTRA_DIST = lib build.xml ndbj.props.in Ndbj.i Mgmj.i Events.i Listeners.i swig/*.i $(NDBAPI_JAVA_SRC) $(MGMAPI_JAVA_SRC) src/java test/java

all-local: ndbj.jar

clean-local:
	rm -rf $(builddir) ndbj.jar

SUFFIXES = .cxx .i

${NDBAPI_BUILT_J}: ndbj.cxx

${MGMAPI_BUILT_J}: mgmj.cxx listeners.cxx events.cxx

${SWIG_BIN}: 
	( cd $(top_builddir)/swig && $(MAKE) $(AM_MAKEFLAGS) )

${top_builddir}/mgmpp/libmgmpp.la: ${MGMPP_SOURCES}
	( cd ${top_builddir}/mgmpp && $(MAKE) $(AM_MAKEFLAGS) )


${top_builddir}/mgmpp/libndbpp.la: ${NDBPP_SOURCES}
	( cd ${top_builddir}/ndbpp && $(MAKE) $(AM_MAKEFLAGS) )

ndbj.cxx $(DEP_NDBJ_SRC): ${NDBAPI_SWIG_SRC} ${SWIG_BIN}
	@mkdir -p ${package_builddir}/ndbj
	$(SWIG) -java -outdir ${builddir}/${package_namespace}/ndbj -package `echo ${package_namespace}/ndbj | sed 's,/,.,g'` -I$(SWIG_DIR) -I${srcdir}/swig $(MYSQL_INCLUDES) $(DEFAULT_INCLUDES) -o ndbj.cxx $<

mgmj.cxx $(DEP_MGMJ_SRC): ${MGMAPI_SWIG_SRC} ${SWIG_BIN}
	@mkdir -p ${package_builddir}/mgmj
	$(SWIG) -java -outdir ${builddir}/${package_namespace}/mgmj -package `echo ${package_namespace}/mgmj | sed 's,/,.,g'` -I$(SWIG_DIR) -I${srcdir}/swig $(MYSQL_INCLUDES) $(DEFAULT_INCLUDES) -o mgmj.cxx $<

events.cxx $(DEP_MGMJ_E_SRC): ${srcdir}/Events.i ${MGMAPI_SWIG_SRC}  ${SWIG_BIN}
	@mkdir -p ${package_builddir}/mgmj/events
	$(SWIG) -java -outdir ${builddir}/${package_namespace}/mgmj/events -package `echo ${package_namespace}/mgmj/events | sed 's,/,.,g'` -I$(SWIG_DIR) -I${srcdir}/swig $(MYSQL_INCLUDES) $(DEFAULT_INCLUDES) -o events.cxx $<

listeners.cxx $(DEP_MGMJ_L_SRC): ${srcdir}/Listeners.i ${MGMAPI_SWIG_SRC} ${SWIG_BIN}
	@mkdir -p ${package_builddir}/mgmj/listeners
	$(SWIG) -java -outdir ${builddir}/${package_namespace}/mgmj/listeners -package `echo ${package_namespace}/mgmj/listeners | sed 's,/,.,g'` -I$(SWIG_DIR) -I${srcdir}/swig $(MYSQL_INCLUDES) $(DEFAULT_INCLUDES) -o listeners.cxx $<

# Phony target to run Ant
ndbj.RunAnt: libndbj.la libmgmj.la
	@echo "Compiling NDB/J java files and building ndbj.jar"
	@$(ANT) jar

ndbj.jar: libndbj.la libmgmj.la $(DEP_NDBJ_SRC) \
			$(DEP_MGMJ_SRC) $(DEP_MGMJ_E_SRC) $(DEP_MGMJ_L_SRC) ndbj.RunAnt

check-local: all-am
	@$(ANT) test

install-exec-local: ndbj.jar
	mkdir -p $(DESTDIR)$(datadir)/java
	cp ndbj.jar $(DESTDIR)$(datadir)/java/ndb-ndbj-$(PACKAGE_VERSION).jar
	( cd $(DESTDIR)$(datadir)/java ; ln -sf ndb-ndbj-$(PACKAGE_VERSION).jar ndb-ndbj.jar )

uninstall-local: 
	rm $(DESTDIR)$(datadir)/java/ndb-ndbj-$(PACKAGE_VERSION).jar

test: check

