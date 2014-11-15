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

ACLOCAL_AMFLAGS = -I m4 
if BUILD_LUA_SUBDIR
  d_lua = lua
endif
if BUILD_JAVA_SUBDIR
  d_java = java
endif
if BUILD_PYTHON_SUBDIR
  d_python = python
endif
if BUILD_CSHARP_SUBDIR
  d_csharp = csharp
endif
if BUILD_RUBY_SUBDIR
  d_ruby = ruby
endif
if BUILD_PERL_SUBDIR
  d_perl = perl
endif
if BUILD_PHP_SUBDIR
  d_php = php
endif
SUBDIRS = mgmpp ndbpp $(d_swig) $(d_java) $(d_python) $(d_perl) $(d_csharp) $(d_ruby) $(d_php) $(d_lua) xml
EXTRA_DIST = $(srcdir)/interface/globals.i $(srcdir)/interface/ndbapi/*.i $(srcdir)/interface/mgmapi/*.i $(srcdir)/cpp/*.cpp $(srcdir)/cpp/Makefile.am   $(srcdir)/cpp/Makefile.in  $(srcdir)/autogen.sh 

# perllocal.pod gets appended to, so we never want to delete it for real.
distuninstallcheck_listfiles=find . -type f \! -name  perllocal.pod -print 

distclean-local:
	rm -fr autom4te.cache
	@if test -f trs.java.sav ; then mv trs.java.sav java/src/java/com/mysql/cluster/ndbj/ThrowingResultSetJDBC4.java ; fi

dist-hook:
	-bzr log > $(top_builddir)/ChangeLog	

test: check

