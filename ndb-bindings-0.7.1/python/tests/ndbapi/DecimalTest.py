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
import unittest, decimal

class DecimalTest(ClusterTestCase.ClusterTestCase):


    tablename = "t_decimal"

    val1 = "10.5"
    val2 = "-101234567.5"
    NUM_INSERTS = 2

    def setUp(self):
        super(DecimalTest, self).setUp()

        cur=self.MYSQL_CONN.cursor()

        self.createTable(self.tablename,"""
        ( id integer NOT NULL primary key,
          val decimal(15,5)
        ) engine=ndbcluster""")

        cur.execute("insert into %s values (1,%s)" % (self.tablename, self.val1))
        self.MYSQL_CONN.commit()

    def testDecimal(self):
        "Insert and retrive Decimal type"

        # insert rows
        trans = self.ndb.startTransaction()

        op = trans.getInsertOperation(self.tablename)

        op.equalInt("id",2)
        op.setDecimal("val",decimal.Decimal(self.val2))
        trans.executeCommit()
        trans.close()

        # retrieve the row inserted by MySQL
        trans = self.ndb.startTransaction()

        op = trans.getSelectOperation(self.tablename,
                                      ndbapi.NdbOperation.LM_CommittedRead)

        op.equalInt("id",1)

        rec = op.getValue("val")
        trans.executeCommit()

        d = rec.value
        self.assertEquals(d,decimal.Decimal(self.val1))
        trans.close()

        # retrieve the row we inserted
        trans = self.ndb.startTransaction()

        op = trans.getSelectOperation(self.tablename,
                                   ndbapi.NdbOperation.LM_CommittedRead)

        op.equalInt("id",2)

        rec = op.getValue("val")
        trans.executeCommit()

        d = rec.value
        self.assertEquals(d,decimal.Decimal(self.val2))
        trans.close()



test_ndb = unittest.TestLoader().loadTestsFromTestCase(DecimalTest)

if __name__=="__main__":
    unittest.TextTestRunner(verbosity=2).run(test_ndb)
