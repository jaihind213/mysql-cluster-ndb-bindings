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

class TempLockTest(ClusterTestCase.ClusterTestCase):

    tablename = "t_temp_lock"
    col1 = "id"
    col2 = "name"
    col2val = "monty"
    NUM_INSERTS = 2

    theResults = None

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

        self.theResults = list()

        for i in range(0,self.NUM_INSERTS):

            op = trans.getSelectOperation(self.tablename,
                                          ndbapi.NdbOperation.LM_Read)

            op.equal(self.col1,i)
            self.theResults.append(op.getValue(self.col2))

        return trans

    def testTemporaryLock(self):
        "Trigger NdbApiTemporaryException"

        for i in range(0,self.NUM_INSERTS):
            cur=self.MYSQL_CONN.cursor()

            # Not using bind variables because the % escaping gets ugly
            cur.execute("insert into %s (id,name) values (%d,'%s')" %
                         (self.tablename, i,"%s%d" % (self.col2val,i)))
            # don't commit, this will cause the lock

        trans = self.getWorkTrans()

        self.assertRaises(ndbapi.NdbApiTemporaryException, trans.execute,
                          ndbapi.Commit, ndbapi.AbortOnError)
        trans.close()

        self.MYSQL_CONN.commit()

        trans = self.getWorkTrans()

        trans.execute(ndbapi.Commit, ndbapi.AbortOnError)

        for i in range(0,self.NUM_INSERTS):
            val = self.theResults[i].value
            self.assertEquals("%s%d" % (self.col2val,i),val)

        trans.close()



test_ndb = unittest.TestLoader().loadTestsFromTestCase(TempLockTest)

if __name__=="__main__":
    unittest.TextTestRunner(verbosity=2).run(test_ndb)
