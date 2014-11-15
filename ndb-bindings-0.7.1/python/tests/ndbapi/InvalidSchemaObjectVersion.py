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

class InvalidSchemaObjectVersionTest(ClusterTestCase.ClusterTestCase):

    tablename = "t_temp_lock"
    col1 = "id"
    col2 = "name"
    col2val = "monty"
    NUM_INSERTS = 2

    def setUp(self):
        super(self.__class__, self).setUp()

        self.createTable(self.tablename,
                         """(%s int, %s varchar(56), 
                             primary key(id))
                            engine=ndbcluster default charset=utf8""" %
                         (self.col1,self.col2))

    def getWorkTrans(self):
        "Return a transaction that has the stuff in it we want to do"

        trans = self.ndb.startTransaction()

        for i in range(0,self.NUM_INSERTS):

            op = trans.getInsertOperation(self.tablename)

            op.equal(self.col1,i)
            op.setValue(self.col2,self.col2val)

        return trans

    def testInvalidSchema(self):
        "Trigger InvalidSchemaObjectVersion"

        dictionary = self.ndb.getDictionary()
        table = dictionary.getTable(self.tablename)

        cursor = self.MYSQL_CONN.cursor()
        cursor.execute("drop table %s" % self.tablename)
        cursor.execute("""create table %s (%s int, %s varchar(56), 
                             primary key(id))
                            engine=ndbcluster default charset=utf8""" %
                         (self.tablename, self.col1,self.col2))

        trans = self.getWorkTrans()

        self.assertRaises(ndbapi.SchemaError, trans.execute,
                          ndbapi.Commit, ndbapi.AbortOnError)
        trans.close()


        dictionary.invalidateTable(self.tablename)

        trans = self.getWorkTrans()

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)

        trans.close()



test_ndb = unittest.TestLoader().loadTestsFromTestCase(InvalidSchemaObjectVersionTest)

if __name__=="__main__":
    unittest.TextTestRunner(verbosity=2).run(test_ndb)
