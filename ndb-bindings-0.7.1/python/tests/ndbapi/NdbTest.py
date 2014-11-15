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

class NdbTest(ClusterTestCase.ClusterTestCase):

    tablename = "t_ndbtest"

    key1   = "k1"
    key2   = "k2"
    data1  = "d1"
    data2  = "d2"
    data3  = "d3"
    data4  = "d4"
    data5  = "d5"
    data6  = "d6"
    data7  = "d7"

    rowTable1 = "rob"
    rowTable2 = "james"
    rowTable3 = "maeve"


    def setUp(self):
        super(NdbTest, self).setUp()

        self.createTable(self.tablename,"""
        ( k1 VARCHAR(30) NOT NULL, d1 VARCHAR(20),
        d2 INTEGER, primary key (k1) ) engine=ndbcluster;""")

    def runInsertTable1(self):
        trans = self.ndb.startTransaction();

        op = trans.getInsertOperation(self.tablename);

        op.setString( self.key1, self.rowTable1)
        op.setString( self.data1 , "mynewvalue" )
        op.setInt(self.data2, 42)

        trans.execute(ndbapi.Commit)
        trans.close()

    def testInsertTable1(self):
        "Trigger duplicate key exception"

        self.runInsertTable1()
        self.assertRaises(ndbapi.NdbApiException,self.runInsertTable1)



test_ndb = unittest.TestLoader().loadTestsFromTestCase(NdbTest)

if __name__=="__main__":

    unittest.TextTestRunner(verbosity=2).run(test_ndb)
