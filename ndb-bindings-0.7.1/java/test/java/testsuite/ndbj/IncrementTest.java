/* -*- mode: java; c-basic-offset: 4; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=4:tabstop=4:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package testsuite.ndbj;

import testsuite.BaseNdbjTestCase;

import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbAtomicOperation;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbResultSet;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class IncrementTest extends BaseNdbjTestCase {
    private static final String tablename = "t_increment";

    public IncrementTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.createTable(tablename, "(id int, name varchar(32), age int unsigned not null, primary key(id)) engine=ndbcluster");
    }

    /**
     * Simple test for inserting a string with NdbOperation.
     */
    public void testSimpleIncrement() throws NdbApiException,SQLException {
        trans = ndb.startTransaction();
        NdbOperation op = trans.getInsertOperation(tablename);
        op.equalInt("id", 1);
        op.setString("name", "Monty");
        op.setInt("age", 30);
        trans.execute(ExecType.Commit,AbortOption.AbortOnError,true);
        trans.close();

        trans = ndb.startTransaction();
        NdbAtomicOperation aOp = trans.getAtomicUpdateOperation(tablename);
        aOp.equalInt("id", 1);
        aOp.increment("age", 2);
        trans.executeCommit();
        trans.close();
        
        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename, LockMode.LM_Read);
        op.equalInt("id", 1);
        op.getValue("age");

        NdbResultSet r = op.resultData();
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        r.next();
        assertEquals(32,r.getInt("age"));
    }

  
}
