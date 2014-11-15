# -*- Mode: Makefile -*-

# TODO: this is a hack - please fix this
rubyarchdir=$(prefix)/$(RUBY_ARCH_DIR)
rubyarch_DATA=mysql/cluster/ndbapi.so mysql/cluster/mgmapi.so
builddir=${top_builddir}/ruby

CLEANFILES=*.db test-out.rdf core* \
	$(rubyarch_DATA) ndbapi.cpp mgmapi.cpp

SWIG_OPTS=-I$(srcdir) -I$(top_srcdir) -I$(SWIG_DIR) @MYSQL_INCLUDES@ 

EXTRA_DIST = ndbapi.i mgmapi.i test test*rb 


SUFFIXES = .cpp .o .so .i

ndbapi.cpp: ndbapi.i $(SWIG_NDB_SOURCES)
	$(SWIG) -c++ -ruby -I$(top_srcdir) -I$(top_builddir) $(SWIG_OPTS) -o $(builddir)/$@ $(srcdir)/$<

mgmapi.cpp: mgmapi.i $(SWIG_MGM_SOURCES)
	$(SWIG) -c++ -ruby -autorename -I$(top_srcdir) -I$(top_builddir) $(SWIG_OPTS) -o $(builddir)/$@ $(srcdir)/$<

.cpp.o: 
	$(CXX) $(SWIG_OPTS) $(DEFAULT_INCLUDES) $(INCLUDES) $(AM_CPPFLAGS) $(CXXFLAGS) $(AM_CFLAGS) $(STANDARD_CFLAGS) -fPIC -DPIC -I$(RUBY_DIR) -c $<  -o $@

# do we still need this?
#$(INSTALL_PROGRAM) $(RUBY_PACKAGE).so $(RUBY_PACKAGE).bundle; 

mysql/cluster/%.so: %.o
	mkdir -p $(builddir)/mysql/cluster
	echo $(CXX) $(AM_CFLAGS) $(CFLAGS) $(CXXFLAGS) $(AM_LDFLAGS) $(LDFLAGS) -shared $< $(LIBRUBYARG_SHARED) -o $@ 
	if test `uname` = Darwin; then \
	    $(CXX)  -Wl,-F. -bundle -undefined dynamic_lookup -arch i386 -arch ppc $(CXXFLAGS) $(AM_LDFLAGS) $(LDFLAGS) $< $(LIBRUBYARG_SHARED) -o $@  ; \
	else \
	    $(CXX) $(AM_CFLAGS) $(CFLAGS) $(CXXFLAGS) $(AM_LDFLAGS) $(LDFLAGS) -shared $< $(LIBRUBYARG_SHARED) -o $@  ; \
        fi

clean-local:
	rm -rf mysql
