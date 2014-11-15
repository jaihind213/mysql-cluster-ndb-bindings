BUILT_SOURCES_NDB = ndbapi.cpp  php_libndbapi.h  
BUILT_SOURCES_MGM = mgmapi.cpp   php_libmgmapi.h
BUILT_SOURCES = $(BUILT_SOURCES_NDB) $(BUILT_SOURCES_MGM) 
NDBAPI_SWIG_SRC = ndbapi.i $(SWIG_NDB_SOURCES)
MGMAPI_SWIG_SRC = mgmapi.i $(SWIG_MGM_SOURCES)

libdir = $(prefix)/$(PHP_ARCH_DIR)
lib_LTLIBRARIES = libndbapi.la libmgmapi.la
nodist_libndbapi_la_SOURCES = $(NDB_BUILT_SOURCES)
libndbapi_la_CPPFLAGS = $(PHP_CPPFLAGS) $(MYSQL_INCLUDES)
nodist_libmgmapi_la_SOURCES = $(NDB_BUILT_SOURCES)
libmgmapi_la_CPPFLAGS = $(PHP_CPPFLAGS) $(MYSQL_INCLUDES)

CLEANFILES = $(BUILT_SOURCES) libmgmapi.php libndbapi.php
EXTRA_DIST = ndbapi.i mgmapi.i


ndbapi.cpp: $(NDBAPI_SWIG_SRC)

mgmapi.cpp: $(MGMAPI_SWIG_SRC)

.i.cpp: $(SWIG_SOURCES)
	$(SWIG) -php5 -I$(SWIG_DIR) $(DEFAULT_INCLUDES) $(MYSQL_INCLUDES) -o $@ $<

distclean-local:
	rm -fr $(BUILT_SOURCES) 

