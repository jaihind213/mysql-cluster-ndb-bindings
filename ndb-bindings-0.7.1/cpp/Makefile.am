AM_CPPFLAGS = $(GLOBAL_CPPFLAGS)

bin_PROGRAMS =  ndbapi_simple_index \
		ndbapi_scan \
                testchars \
                teststrins \
                testdateins \
		testprog \
		testscan \
		testdecimal \
		testabort \
		OutOfConnectionProblemTest
LDADD = -lmysqlclient_r

ndbapi_simple_index_SOURCES = ndbapi_simple_index.cpp
ndbapi_scan_SOURCES = ndbapi_scan.cpp
testchars_SOURCES = testchars.cpp
teststrins_SOURCES = teststrins.cpp
testdateins_SOURCES = testdateins.cpp
testprog_SOURCES = testprog.cpp
testdecimal_SOURCES = testdecimal.cpp
testscan_SOURCES = testscan.cpp
testabort_SOURCES = testabort.cpp
OutOfConnectionProblemTest_SOURCES = OutOfConnectionProblemTest.cpp

distdir:
	@echo "doing nothing in testndbapi"
