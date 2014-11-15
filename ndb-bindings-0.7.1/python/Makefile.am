# -*- Mode: Makefile -*-
#  ndb-bindings: Bindings for the NDB API
#  Copyright (C) 2008 MySQL, Inc.
#  
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or 
#  (at your option) any later version.
#  
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

builddir=${top_builddir}/python
pythonarchdir=$(PYTHON_DIR)
pythonarch_DATA=${builddir}/mysql/cluster/_ndbapi.so ${builddir}/mysql/cluster/_mgmapi.so

CLEANFILES=ndbapi.cpp ndbapi.h mgmapi.cpp mysql/cluster/ndbapi.py mysql/cluster/mgmapi.py mysql/cluster/events.py mysql/cluster/listeners.py *.so mysql/cluster/*pyc *pyc mgmapi.h events.cpp listeners.cpp events.h listeners.h mysql/cluster/*.so ez_setup.pyc ${builddir}/mysql/__init__.pyc
EXTRA_DIST = *py mgmapi.i ndbapi.i events.i listeners.i PKG-INFO README MANIFEST.in mysql/cluster/alchemy.py mysql/__init__.py mysql/cluster/__init__.py  tests/ndbapi/*py tests/*py examples/*py swig/*.i

SWIG_OPTS=-O -I$(srcdir) -I$(top_srcdir) -I$(top_builddir) -I$(top_srcdir)/mgmpp -I$(SWIG_DIR) @MYSQL_INCLUDES@ -outdir $(builddir)/mysql/cluster


${SWIG_BIN}:
	( cd $(top_builddir)/swig && $(MAKE) $(AM_MAKEFLAGS) )

ndbapi.cpp: ${SWIG_NDB_SOURCES} $(srcdir)/swig/*.i ndbapi.i ${SWIG_BIN}

mgmapi.cpp: ${SWIG_MGM_SOURCES} mgmapi.i ${SWIG_BIN}

events.cpp: ${SWIG_MGM_SOURCES} events.i ${SWIG_BIN}

listeners.cpp: ${SWIG_MGM_SOURCES} listeners.i ${SWIG_BIN}

SUFFIXES = .cpp .i

.i.cpp: $(SWIG_SOURCES) $(srcdir)/swig/*.i ${SWIG_BIN}
	@test ! -d $(builddir)/mysql && cp -a $(srcdir)/mysql $(builddir) || true
	@chmod -R u+w $(builddir)/mysql
	$(SWIG) -c++ -python $(SWIG_OPTS) -o $@ $<

$(builddir)/mysql/cluster/_ndbapi.so $(builddir)/mysql/cluster/_mgmapi.so $(builddir)/mysql/cluster/_events.so $(builddir)/mysql/cluster/_listeners.so: $(builddir)/ndbapi.cpp $(builddir)/mgmapi.cpp $(builddir)/events.cpp $(builddir)/listeners.cpp
	 CC="${CXX}" CFLAGS="${CFLAGS}" CPPFLAGS="${CPPFLAGS}" LDFLAGS="${LDFLAGS}" SRCDIR="${srcdir}" TOP_SRCDIR="${top_srcdir}" BUILDDIR="${builddir}" TOP_BUILDDIR="${top_builddir}" ${PYTHON} $(srcdir)/setup.py build  --build-base="${top_builddir}/python/build"

clean-local:
	${PYTHON} ${srcdir}/setup.py clean
	rm -rf ${builddir}/build
	@test ${builddir}/mysql -ef ${srcdir}/mysql || rm -rf ${builddir}/mysql
	@rm ez_setup.pyc

install: $(builddir)/mysql/cluster/_ndbapi.so $(builddir)/mysql/cluster/_mgmapi.so 
	@mkdir -p ${pythondir}
	PYTHONPATH="${PYTHONPATH}:${pythondir}" CC="${CXX}" CFLAGS="${CFLAGS}" CPPFLAGS="${CPPFLAGS}" LDFLAGS="${LDFLAGS}" VERSION="${VERSION}" ${PYTHON} ${srcdir}/setup.py install --prefix=${DESTDIR}${prefix} --single-version-externally-managed --root=/

develop: $(builddir)/mysql/cluster/_ndbapi.so $(builddir)/mysql/cluster/_mgmapi.so
	CC="${CXX}" CFLAGS="${CFLAGS}" CPPFLAGS="${CPPFLAGS}" LDFLAGS="${LDFLAGS}" VERSION="${VERSION}" ${PYTHON} setup.py develop --prefix=${DESTDIR}${prefix}


dist-hook:
	@rm -f *pyc mysql/cluster/*pyc
