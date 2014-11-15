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

import java.sql.SQLException;

import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;
import com.mysql.cluster.ndbj.NdbResultSet;

public class Latin1InsertReadTest extends BaseNdbjTestCase {
    private static final String tablename = "t_latin1";
    private static final String col1 = "id";
    private static final String col2 = "name";
    private static final String col2Val = "jimµ";

    public Latin1InsertReadTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        createTable(tablename, "(`id` VARCHAR(100) PRIMARY KEY, " +
                    " `name` VARCHAR(256)) ENGINE=ndb default charset=latin1");
    }

    /**
     * Test inserting / retrieving a latin1 string.
     *
     * @todo This is not stored correctly.
     */
    public void testLatin1() throws SQLException {
        NdbOperation op;

        // insert a row into a latin1 column
        trans = ndb.startTransaction();
        op = trans.getInsertOperation(tablename);
        op.equalString(col1, "1");
        op.setString(col2, col2Val);
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // retrieve the row and check the value
        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename, LockMode.LM_Read);
        op.equalString(col1, "1");
        op.getValue(col2);

        NdbResultSet r = op.resultData();
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        r.next();
        assertEquals(col2Val, r.getString(col2));
        trans.close();

        /*// also check retrieval via jdbc
        String val = (String)getSingleValue(tablename, col2, null);
        // TODO this doesn't work until further charset support is implemented
        assertEquals(col2Val, val);
        */
    }
}
