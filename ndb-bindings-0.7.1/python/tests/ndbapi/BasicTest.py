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

class BasicTest(ClusterTestCase.ClusterTestCase):

    tablename = "basic_test"

    def setUp(self):
        super(BasicTest, self).setUp()

        self.createTable(self.tablename,
                         """(id int, 
                             name varchar(32), 
                             age int, 
                             primary key(id)) 
                            engine=ndbcluster""")

    def testSingleInsertString(self):
        "Insert a string with NdbOperation."

        trans = self.ndb.startTransaction()

        op = trans.getInsertOperation(self.tablename)

        op.equal("id", 1)
        op.setValue("name", "Monty")

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)

        trans.close()

        nameVal = self.getSingleValue(self.tablename, "name", "where id=1")
        self.assertEquals("Monty",nameVal)

    def testSingleInsertInt(self):
        "Insert an integer with NdbOperation."

        trans = self.ndb.startTransaction()

        op = trans.getInsertOperation(self.tablename)

        op.equal("id", 1)
        op.setValue("age", 100)

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)

        trans.close()

        nameVal = self.getSingleValue(self.tablename, "age", "where id=1")
        self.assertEquals(100, nameVal)



test_ndb = unittest.TestLoader().loadTestsFromTestCase(BasicTest)

if __name__=="__main__":
    unittest.TextTestRunner(verbosity=2).run(test_ndb)
