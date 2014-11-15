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

from tests import ClusterTestCase
from mysql.cluster import ndbapi
import unittest


import sys

class NdbColumn(ClusterTestCase.ClusterTestCase):

  tablename="test_column"

  def setUp(self):
    super(NdbColumn, self).setUp()

    self.createTable(self.tablename,"""
      ( id integer not null primary key,
        name varchar(24)  ) engine=ndbcluster;""")

  def testStringFormat(self):
    "Check varchar formatting"

    d=self.ndb.getDictionary()
    dt=d.getTable(self.tablename)
    col=dt.getColumn('name')
    vst=ndbapi.ndbFormatString(col,"foo",3)
    self.assertEqual("\x03foo",vst)

test_column = unittest.TestLoader().loadTestsFromTestCase(NdbColumn)

if __name__=="__main__":

    unittest.TextTestRunner(verbosity=2).run(test_column)

