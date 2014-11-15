#!/usr/bin/env python
# -*- mode: python; c-basic-offset: 2; indent-tabs-mode: nil; -*-
#  vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
##  ndb-bindings: Bindings for the NDB API
##    Copyright (C) 2006 MySQL, Inc.
##    
##    This program is free software; you can redistribute it and/or modify
##    it under the terms of the GNU General Public License as published by
##    the Free Software Foundation; either version 2 of the License, or 
##    (at your option) any later version.
##    
##    This program is distributed in the hope that it will be useful,
##    but WITHOUT ANY WARRANTY; without even the implied warranty of
##    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
##    GNU General Public License for more details.
##
##    You should have received a copy of the GNU General Public License
##    along with this program; if not, write to the Free Software
##    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

# bootstrap setuptools if necessary
from ez_setup import use_setuptools
use_setuptools()

from distutils.command.clean import clean
from distutils.command.build import build
from setuptools import setup,Extension
import os.path, os
import sys

srcdir=os.environ.get("SRCDIR",".")
top_srcdir=os.environ.get("TOP_SRCDIR","..")
builddir=os.environ.get("BUILDDIR",".")
top_builddir=os.environ.get("TOP_BUILDDIR","..")
version=os.environ.get("VERSION","0.0.0")

description = """Python wrapper of the NDBAPI"""

classifiers="""\
Development Status :: 2 - Pre-Alpha
License :: OSI Approved :: GPL
Operating System :: POSIX :: Linux
Programming Language :: C++
Programming Language :: Python
Topic :: Software Development :: Libraries :: Python Modules
"""

setup(name="python-ndbapi",
      version=version,
      description=description,
      long_description=description,
      author="Monty Taylor",
      author_email="mtaylor@mysql.com",
      url="http://launchpad.net/ndb-bindings",
      platforms="linux",
      license="GPL",
      classifiers=filter(None, classifiers.splitlines()),

      ext_modules=[
        Extension("mysql.cluster._mgmapi",
                  sources=["mgmapi.cpp"],
                  libraries=["mgmpp"],
                  library_dirs=["%s/mgmpp/.libs" % top_builddir],
                  depends=["%s/interface/mgmapi/%s" % (top_srcdir,f) for f in os.listdir("%s/interface/mgmapi" % top_srcdir)],
                  language="c++",
                  ),
        Extension("mysql.cluster._ndbapi",
                  sources=["ndbapi.cpp"],
                  library_dirs=["%s/mgmpp/.libs" % top_builddir],
                  depends=["%s/interface/ndbapi/%s" % (top_srcdir,f) for f in os.listdir("%s/interface/ndbapi" % top_srcdir)],
                  language="c++",
                  ),
        Extension("mysql.cluster._events",
                  sources=["events.cpp"],
                  libraries=["mgmpp"],
                  library_dirs=["%s/mgmpp/.libs" % top_builddir],
                  depends=["%s/interface/mgmapi/%s" % (top_srcdir,f) for f in os.listdir("%s/interface/mgmapi" % top_srcdir)],
                  language="c++",
                  ),
        Extension("mysql.cluster._listeners",
                  sources=["listeners.cpp"],
                  libraries=["mgmpp"],
                  library_dirs=["%s/mgmpp/.libs" % top_builddir],
                  depends=["%s/interface/mgmapi/%s" % (top_srcdir,f) for f in os.listdir("%s/interface/mgmapi" % top_srcdir)],
                  language="c++",
                  ),

        ],
      test_suite = "tests.AllTests.test_all",
      entry_points = {
        'sqlalchemy.databases': [ 'ndbapi = mysql.cluster.alchemy:dialect' ]
        },
      py_modules=["mysql", "mysql.cluster", "mysql.cluster.ndbapi","mysql.cluster.mgmapi","mysql.cluster.events","mysql.cluster.alchemy"]
      )

