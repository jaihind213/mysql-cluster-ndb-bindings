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

import com.mysql.cluster.ndbj.*;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class Utf8InsertReadTest extends BaseNdbjTestCase {

    protected static final String col1 = "id";

    protected static final String col2 = "name";

    protected static final String col2Val = "jimµ blah blah";

    protected static final int NUM_INSERTS = 2;

    protected static final String tablename = "t_utf8";

    public Utf8InsertReadTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(`id` INT PRIMARY KEY, `name` VARCHAR(256) CHARSET utf8) ENGINE=ndb;");
    }

    /**
     * Basic test of handling Unicode data.
     */
    public void testUtf8InsertRead() throws SQLException {
        // first insert two identical records
        trans = ndb.startTransaction();
        for (int i = 0; i < NUM_INSERTS; i++) {
            NdbOperation op = trans.getInsertOperation(tablename);
            op.equalInt(col1, i);
            op.setString(col2, col2Val);
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // check that we can retrieve as stored
        trans = ndb.startTransaction();

        NdbResultSet[] theResults = new NdbResultSet[NUM_INSERTS];
        for (int i = 0; i < NUM_INSERTS; i++) {
            NdbOperation op = trans.getSelectOperation(tablename);
            op.equalInt(col1, i);
            op.getValue(col2);
            theResults[i] = op.resultData();
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        for (NdbResultSet r : theResults)
            while (r.next())
                assertEquals(r.getString(col2), col2Val);
        trans.close();

        // read the data through the mysql server + jdbc
        String val = (String)getSingleIndexedValueWithQuery(conn, 1,
                                                            "select " + col2 + " from " + tablename);
        assertEquals(val, col2Val);

        // test retrieving the data with NdbScanFilter
        trans = ndb.startTransaction();
        NdbScanOperation scanop = trans.getSelectScanOperation(tablename);
        NdbScanFilter scanFilter = scanop.getNdbScanFilter();
        scanFilter.begin();
        scanFilter.eq(col2, col2Val);
        scanFilter.end();
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        int i = 0;
        for(; scanop.nextResult(true) == 0; ++i);
        assertEquals("Should've read 2 matching rows", 2, i);
        trans.close();
    }
}

