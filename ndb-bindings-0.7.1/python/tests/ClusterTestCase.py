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

import unittest
import MySQLdb
from mysql.cluster import ndbapi
import shared_cluster_connection


class ClusterTestCase(unittest.TestCase):

  MYSQLD_HOST=""
  NDB_MGMD_CONNECTSTRING=""
  DB_NAME="test"
  LIB_PATH = ""
  USERNAME  = "root"
  PASSWORD = ""
  CLUSTER_LOG=""
  MYSQL_CONN=None


  def createTable(self, tablename, schemaDefinition):


    cur=self.MYSQL_CONN.cursor()

    cur.execute("drop table if exists %s" % tablename)

    cur.execute("create table %s %s " % (tablename,schemaDefinition))
    #self.ndb.getDictionary().getTable(tablename)
    self.ndb.getDictionary().invalidateTable(tablename)

  def setUp(self):

    self.MYSQL_CONN=MySQLdb.connect(
      host=self.MYSQLD_HOST,
      user=self.USERNAME,
      passwd=self.PASSWORD,
      db=self.DB_NAME,
      read_default_group="client")

    self.ndb = shared_cluster_connection.conn.createNdb(self.DB_NAME)
    self.ndb.init(1000)

  def getRowCount(self, tablename):
    cur = self.MYSQL_CONN.cursor()
    cur.execute("select count(*) from %s" % tablename)

    row = cur.fetchone()
    if row is None or len(row) != 1:
      return -1
    return row[0]

  def getSingleValue(self, tablename, value, condition):
    cur = self.MYSQL_CONN.cursor()
    cur.execute("select %s from %s %s" % (value, tablename, condition))
    row = cur.fetchone()
    if row is None or len(row) != 1:
      return None
    return row[0]

  def tearDown(self):
    del self.ndb
