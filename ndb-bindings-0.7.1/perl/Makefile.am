PERL_MAKEFILE=Makefile.perl
builddir=${top_builddir}/perl

#SWIG_OUTPUTS=lib/mysql/cluster/mgmapi.pm lib/mysql/cluster/ndbapi.pm
SWIG_OUTPUTS=lib/mgmapi.pm lib/ndbapi.pm
PERL_CRUFT=pm_to_blib ndbapi.bs ndbapi_wrap.o  mgmapi.bz mgmapi_wrap.o
PERL_SWIG_SRC=swig/perl_callback_typemap.i swig/perl_extend_ndbtransaction_async.i 
fakedir=$(prefix)
fake_DATA=build-perl-stamp

EXTRA_DIST=ndbapi.i mgmapi.i Makefile.PL ndbapi/Makefile.PL \
	mgmapi/Makefile.PL async.pl blob.pl test.pl swig

CLEANFILES=$(PERL_CRUFT) ${SWIG_OUTPUTS}
MAINTAINERCLEANFILES=

SWIG_OPTS=-perl5 $(DEFS) -outdir ${builddir}/lib -I${top_builddir} -I${top_srcdir} -I$(SWIG_DIR) -I$(srcdir)/swig $(MYSQL_INCLUDES)

#ndbapi/ndbapi_wrap.cpp lib/mysql/cluster/ndbapi.pm: ndbapi.i  $(SWIG_NDB_SOURCES) 
${builddir}/ndbapi/ndbapi_wrap.cpp ${builddir}/lib/ndbapi.pm: ${srcdir}/ndbapi.i  $(SWIG_NDB_SOURCES) 
	@mkdir -p ${builddir}/lib 
	@mkdir -p ${builddir}/ndbapi
	$(SWIG) $(SWIG_OPTS) -o ${builddir}/ndbapi/ndbapi_wrap.cpp ${srcdir}/ndbapi.i

${builddir}/mgmapi/mgmapi_wrap.cpp ${builddir}/lib/mgmapi.pm: ${srcdir}/mgmapi.i  $(SWIG_MGM_SOURCES) 
	@mkdir -p ${builddir}/lib
	@mkdir -p ${builddir}/mgmapi
	$(SWIG) $(SWIG_OPTS) -o ${builddir}/mgmapi/mgmapi_wrap.cpp ${srcdir}/mgmapi.i

$(PERL_MAKEFILE): $(srcdir)/Makefile.PL ${builddir}/ndbapi/ndbapi_wrap.cpp ${builddir}/mgmapi/mgmapi_wrap.cpp ${builddir}/lib/ndbapi.pm ${builddir}/lib/mgmapi.pm $(srcdir)/Makefile.am
	echo "$(DEB_MAKE_PL_OPT) should be INSTALLDIRS=vendor"
	test ${srcdir}/Makefile.PL -ef ${builddir}/Makefile.PL || cp ${srcdir}/Makefile.PL ${builddir}/Makefile.PL
	CFLAGS="-I${top_builddir} -I${top_srcdir} -I../${top_srcdir}/mgmpp -I../${top_srcdir}/ndbpp $(CPPFLAGS) $(CFLAGS) $(MYSQL_INCLUDES)" LDFLAGS="$(AM_LDFLAGS) $(LDFLAGS) $(LIBS)" CC="${CC}" CXX="${CXX}" CPP="${CPP}" $(PERL) $(srcdir)/Makefile.PL $(MAKE_PL_OPTS) $(DEB_MAKE_PL_OPT) PREFIX="${prefix}"
	
-perl install-perl: $(PERL_MAKEFILE) ${builddir}/ndbapi/ndbapi_wrap.cpp ${builddir}/mgmapi/mgmapi_wrap.cpp
	@target=`echo $@ | sed -e 's/-perl//'`; \
	echo $(MAKE) -f $(PERL_MAKEFILE) $$target; \
	DESTDIR=$(DESTDIR) $(MAKE) $(AM_MAKEFLAGS) -f $(PERL_MAKEFILE) $$target  PREFIX="${prefix}"

test-perl: $(PERL_MAKEFILE)
	$(RUN)$(MAKE) -f $(PERL_MAKEFILE) test

clean-perl realclean-perl:
	@target=`echo $@ | sed -e 's/-perl//'`; \
	if test -r $(PERL_MAKEFILE); then \
	  echo $(MAKE) -f $(PERL_MAKEFILE) $$target; \
          $(MAKE) -f $(PERL_MAKEFILE) $$target; \
        fi
	test ${srcdir}/Makefile.PL -ef ${builddir}/Makefile.PL || rm -f ${builddir}/Makefile.PL

build-perl: -perl
build-perl-stamp: ndbapi.i mgmapi.i ${builddir}/ndbapi/ndbapi_wrap.cpp ${builddir}/mgmapi/mgmapi_wrap.cpp
	$(MAKE) build-perl && touch build-perl-stamp

check-local: build-perl-stamp test-perl

# Perl make clean leaves the old makefile around
clean-local: realclean-perl
	rm -rf lib blib $(PERL_MAKEFILE) $(PERL_MAKEFILE).old build-perl-stamp

# See this is the fake bit
install-fakeDATA: install-perl

uninstall-local:
	cat `find $(DESTDIR)$(prefix) | grep ndbapi..packlist` | xargs -n1 rm -rf
