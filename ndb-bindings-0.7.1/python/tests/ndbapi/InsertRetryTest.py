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

class InsertRetryTest(ClusterTestCase.ClusterTestCase):

    tablename = "t_insert_retry"
    col1 = "id"
    col2 = "name"
    col2val = "monty"
    NUM_INSERTS = 2

    def setUp(self):
        super(InsertRetryTest, self).setUp()

        self.createTable(self.tablename,
                         """(%s int, %s varchar(56), 
                             primary key(id))
                            engine=ndbcluster default charset=utf8""" %
                         (self.col1,self.col2))

    def mysqlInsert(self):
        for i in range(0,self.NUM_INSERTS):
            cur=self.MYSQL_CONN.cursor()

            # Not using bind variables because the % escaping gets ugly
            cur.execute("insert into %s (id,name) values (%d,'%s')" %
                         (self.tablename, i,"%s%d" % (self.col2val,i)))
            self.MYSQL_CONN.commit()

    def testInsert(self):
        "Insert via NDB, Read with SQL"

        trans = self.ndb.startTransaction()

        for i in range(0,self.NUM_INSERTS):

            op = trans.getInsertOperation(self.tablename)
            op.equal(self.col1, i)
            op.setValue(self.col2, "%s%d" % (self.col2val,i))

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)
        trans.close()

        for i in range(0,self.NUM_INSERTS):

            ret_val = self.getSingleValue(self.tablename, self.col2,
                                          "where %s=%d" % (self.col1, i))
            self.assertEquals("%s%d" % (self.col2val,i), ret_val)


    def testRead(self):
        "Insert with SQL, read with NDB"

        self.mysqlInsert()

        trans = self.ndb.startTransaction()

        theResults = list()

        for i in range(0,self.NUM_INSERTS):

            op = trans.getSelectOperation(self.tablename,
                                          ndbapi.NdbOperation.LM_Read)

            op.equal(self.col1,i)
            theResults.append(op.getValue(self.col2))

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)

        for i in range(0,self.NUM_INSERTS):
            val = theResults[i].value
            self.assertEquals("%s%d" % (self.col2val,i),val)

        trans.close()

    def testDelete(self):
        "Insert with SQL, delete with NDB"

        self.mysqlInsert()

        trans = self.ndb.startTransaction()

        for i in range(0,self.NUM_INSERTS):

            op = trans.getDeleteOperation(self.tablename)
            op.equal(self.col1,i)

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)
        trans.close()

        self.assertEquals(0,self.getRowCount(self.tablename))



test_ndb = unittest.TestLoader().loadTestsFromTestCase(InsertRetryTest)

if __name__=="__main__":
    unittest.TextTestRunner(verbosity=2).run(test_ndb)
