BUILT_SOURCES_NDB = ndbapi.xml
BUILT_SOURCES_MGM = mgmapi.xml
BUILT_SOURCES = $(BUILT_SOURCES_NDB) $(BUILT_SOURCES_MGM) 
NDBAPI_SWIG_SRC = ndbapi.i $(SWIG_NDB_SOURCES)
MGMAPI_SWIG_SRC = mgmapi.i $(SWIG_MGM_SOURCES)

CLEANFILES = $(BUILT_SOURCES) 
EXTRA_DIST = ndbapi.i mgmapi.i


all: ndbapi.xml mgmapi.xml 

.i.xml: $(SWIG_SOURCES)
	$(SWIG) -xml -I$(top_srcdir) -I$(SWIG_DIR) $(DEFAULT_INCLUDES) $(MYSQL_INCLUDES) -o $@ $<

distclean-local:
	rm -fr $(BUILT_SOURCES) 

