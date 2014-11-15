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
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class NullSetTest extends BaseNdbjTestCase {

    private static final String tablename = "t_nulls";

    private static final String col1="id";
    private static final String col2="name";
    private static final String col3="addr";
    private static final String col4="size";

    public NullSetTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(id VARBINARY(256) PRIMARY KEY, name VARBINARY(256), addr VARCHAR(256), size BIGINT) ENGINE=ndb;");
    }

    /*
     * Test method for 'com.mysql.cluster.ndbj.NdbOperation.setNull(String)'
     */
    public final void testNullSet() throws NdbApiException, SQLException {
        NdbOperation op;
        byte pkID[];

        trans = ndb.startTransaction();
        op = trans.getInsertOperation(tablename);

        pkID = "jim".getBytes();
        op.equalBytes("id",pkID);
        op.setNull(col2);
        op.setNull(col3);
        op.setNull(col4);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename,LockMode.LM_Exclusive);

        op.equalBytes("id",pkID);
        op.getValue(col2);
        op.getValue(col3);
        op.getValue(col4);

        rs = op.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        while(rs.next())
        {
            rs.getBytes(col2);
            assertTrue(rs.wasNull());

            rs.getString(col3);
            assertTrue(rs.wasNull());

            rs.getLong(col4);
            assertTrue(rs.wasNull());
        }
        trans.close();

        trans = ndb.startTransaction();

        op = trans.getInsertOperation(tablename);

        pkID = "tim".getBytes();
        op.equalBytes("id",pkID);
        op.setNull(col2);
        op.setNull(col3);
        op.setNull(col4);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename,LockMode.LM_Exclusive);

        op.equalBytes(col1,pkID);
        op.getValue(col2);
        op.getValue(col3);
        op.getValue(col4);

        rs = op.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        while(rs.next())
        {
            rs.getBytes(col2);
            assertTrue(rs.wasNull());
            rs.getString(col3);
            assertTrue(rs.wasNull());
            rs.getInt(col4);
            assertTrue(rs.wasNull());
        }
        trans.close();
    }
}
