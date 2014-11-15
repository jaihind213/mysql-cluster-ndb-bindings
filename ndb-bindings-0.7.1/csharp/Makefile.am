builddir=${top_builddir}/csharp
NDBAPI_SOURCE =  $(srcdir)/ndbapi/*.cs
MGMAPI_SOURCE =  $(srcdir)/mgmapi/*.cs
MYSQL_DLL = $(srcdir)/MySql.Data.dll
CLEANFILES = ndbapi.dll *exe test/*exe ndbapi.cpp MySql.Cluster.NdbApi.dll ndbapi/generated/* mgmapi/generated/* mgmapi.cpp MySql.Cluster.MgmApi.dll

lib_LTLIBRARIES = libndbcli.la libmgmcli.la
nodist_libndbcli_la_SOURCES = ndbapi.cpp
libndbcli_la_CPPFLAGS = $(PTHREAD_CFLAGS)
libndbcli_la_DEPENDENCIES = MySql.Cluster.NdbApi.dll 
nodist_libmgmcli_la_SOURCES = mgmapi.cpp
libmgmcli_la_CPPFLAGS = $(PTHREAD_CFLAGS)
libmgmcli_la_DEPENDENCIES = MySql.Cluster.MgmApi.dll 
libmgmcli_la_LIBADD = ${top_builddir}/mgmpp/libmgmpp.la

EXTRA_DIST = ndbapi.i mgmapi.i $(NDBAPI_SOURCE) mgmapi/*.cs test/*cs bindings.snk *.dll.config

clean-local:
	@rm -rf $(builddir)/ndbapi/generated $(builddir)/mgmapi/generated

test: examples/test.exe examples/test2.exe 

SUFFIXES = .exe .cs .i .cpp

.cs.exe: MySql.Cluster.NdbApi.dll ndbapi.cpp
	$(MCS) -keyfile:$(srcdir)/bindings.snk -r:$(builddir)/MySql.Cluster.NdbApi.dll -r:$(MYSQL_DLL) $<

MySql.Cluster.MgmApi.dll: $(MGMAPI_SOURCE) 
	@test $(srcdir)/bindings.snk -ef $(builddir)/bindings.snk || cp -f $(srcdir)/bindings.snk $(builddir)/bindings.snk
	$(MCS) -target:library -keyfile:$(srcdir)/bindings.snk  -out:$(builddir)/$@ $(srcdir)/mgmapi/*.cs $(builddir)/mgmapi/generated/*.cs 
MySql.Cluster.NdbApi.dll: $(NDBAPI_SOURCE)
	$(MCS) -target:library -keyfile:$(srcdir)/bindings.snk -out:$(builddir)/$@ $(srcdir)/ndbapi/*.cs $(builddir)/ndbapi/generated/*.cs 

#$(CSHARP_SOURCES)
#.PRECIOUS: %.cpp %.so %.dll
#/usr/bin/objcopy --set-section-flags .debug_str=contents,debug $@

ndbapi.cpp: ${SWIG_NDB_SOURCES}

mgmapi.cpp: ${SWIG_MGM_SOURCES}

${SWIG_BIN}:
	( cd $(top_builddir)/swig && $(MAKE) $(AM_MAKEFLAGS) )

mgmapi.cpp: mgmapi.i ${SWIG_SOURCES} ${SWIG_BIN}
	@mkdir -p $(builddir)/$*/generated
	${SWIG} -csharp -dllimport libmgmcli -namespace MySql.Cluster.MgmApi \
		-I$(top_builddir) -I$(top_srcdir) -I$(SWIG_DIR) \
		$(MYSQL_INCLUDES) -outdir $(builddir)/$*/generated \
		-o ${builddir}/$@ $<

ndbapi.cpp: ndbapi.i ${SWIG_SOURCES} ${SWIG_BIN}
	@mkdir -p $(builddir)/$*/generated
	${SWIG} -csharp -dllimport libndbcli -namespace MySql.Cluster.NdbApi \
		-I$(top_builddir) -I$(top_srcdir) -I$(SWIG_DIR) \
		$(MYSQL_INCLUDES) -outdir $(builddir)/$*/generated \
		-o ${builddir}/$@ $<

install-exec-local: MySql.Cluster.NdbApi.dll  MySql.Cluster.MgmApi.dll
	mkdir -p $(DESTDIR)$(datadir)/cli
	$(INSTALL_DATA) MySql.Cluster.NdbApi.dll* $(DESTDIR)$(datadir)/cli
	$(INSTALL_DATA) MySql.Cluster.MgmApi.dll* $(DESTDIR)$(datadir)/cli

uninstall-local:
	@rm $(DESTDIR)$(datadir)/cli/MySql.Cluster.NdbApi.dll $(DESTDIR)$(datadir)/cli/MySql.Cluster.MgmApi.dll 
