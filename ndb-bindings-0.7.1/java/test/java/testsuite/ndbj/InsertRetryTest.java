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

import java.sql.SQLException;

import testsuite.BaseNdbjTestCase;

import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;
import com.mysql.cluster.ndbj.NdbResultSet;

public class InsertRetryTest extends BaseNdbjTestCase {

    private static final String tablename = "t_insert_retry";
    private static final String col1 = "id";
    private static final String col2 = "name";
    private static final String col2Val = "montyµ";
    private static final int NUM_INSERTS = 2;

    public InsertRetryTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename, "(id int, name varchar(56), primary key(id)) " +
                    "engine=ndbcluster default charset=utf8");
    }

    /**
     * Basic insert/retrieve test.
     */
    public void testInsert() throws java.sql.SQLException, NdbApiException {
        // insert some rows
        trans = ndb.startTransaction();
        for (int i = 0; i < NUM_INSERTS; i++) {
            NdbOperation op = trans.getInsertOperation(tablename);
            op.equalInt(col1, i);
            op.setString(col2, col2Val + i);
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // verify the data inserted
        for (int i = 0; i < NUM_INSERTS; i++) {
            String nameVal = (String)getSingleValue(tablename, "name",
                                                    "where id=" + i);
            assertEquals(col2Val + i, nameVal);
        }
    }

    /**
     * Utility method to insert some rows.
     */
    private void jdbcInsert() throws SQLException {
        for (int i = 0; i < NUM_INSERTS ; i++)
            stmt.execute("insert into " + tablename +
                         "(id, name) values (" + i + ", '" + col2Val + i + "')");
    }

    /**
     * Basic test of read and NdbResultSet.
     */
    public void testRead() throws SQLException, NdbApiException {
        // insert some rows
        jdbcInsert();

        // read the rows by id
        trans = ndb.startTransaction();

        NdbResultSet[] theResult = new NdbResultSet[NUM_INSERTS];
        for (int i = 0; i < NUM_INSERTS; i++) {
            NdbOperation op = trans.getSelectOperation(tablename,
                                                       LockMode.LM_Read);

            theResult[i] = op.resultData();
            op.equalInt(col1, i);
            op.getValue(col2);
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        // verify the read data
        int j = 0;
        for (NdbResultSet r : theResult) {
            while (r.next()) {
                String val = r.getString(col2);
                assertEquals(val, col2Val + j);
            }
            j++;
        }

        trans.close();
    }

    /**
     * Basic test of delete operation.
     */
    public void testDelete() throws java.sql.SQLException, NdbApiException {
        // insert rows
        jdbcInsert();

        // delete by id
        trans = ndb.startTransaction();

        for (int i = 0; i < NUM_INSERTS; i++) {
            NdbOperation op = trans.getDeleteOperation(tablename);
            op.equalInt(col1, i);
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // make sure all rows have been deleted
        int rowcount = getRowCount(tablename);
        assertEquals(rowcount, 0);
    }
}
