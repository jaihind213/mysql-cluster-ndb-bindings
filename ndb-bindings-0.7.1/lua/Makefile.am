# -*- Mode: Makefile -*-

luaarchdir=${libdir}/lua
luaarch_DATA=ndbapi.so mgmapi.so
builddir=${top_builddir}/lua

CLEANFILES=*.db test-out.rdf core* \
	$(luaarch_DATA) ndbapi.cpp mgmapi.cpp

SWIG_OPTS=-I$(top_srcdir) -I$(srcdir) -I${top_builddir} -I$(SWIG_DIR) @MYSQL_INCLUDES@ 

SUFFIXES = .cpp .i .so .o

EXTRA_DIST = ndbapi.i mgmapi.i

ndbapi.cpp: $(SWIG_NDB_SOURCES) 

mgmapi.cpp: $(SWIG_MGM_SOURCES)

.i.cpp: $(SWIG_SOURCES)
	$(SWIG) -c++ -lua $(SWIG_OPTS) -o ${builddir}/$@ $<

.cpp.o:
	$(CXX) $(SWIG_OPTS) $(DEFAULT_INCLUDES) $(INCLUDES) $(AM_CPPFLAGS) $(CXXFLAGS) $(AM_CFLAGS) $(STANDARD_CFLAGS) -fPIC -DPIC $(LUA_CFLAGS) $< -c -o $@

.o.so: 
	echo $(CXX) $(AM_CFLAGS) $(CFLAGS) $(AM_LDFLAGS) $(LDFLAGS) -shared $< $(LUA_LDFLAGS) -o $@ 
	$(CXX) $(AM_CFLAGS) $(CFLAGS) $(AM_LDFLAGS) $(LDFLAGS) -shared $< $(LUA_LDFLAGS) -o $@ 

clean-local:
	rm -rf mysql
