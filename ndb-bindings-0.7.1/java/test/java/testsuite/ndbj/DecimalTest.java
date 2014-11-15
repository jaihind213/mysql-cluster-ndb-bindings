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

import java.math.BigDecimal;
import java.sql.SQLException;

import testsuite.BaseNdbjTestCase;

import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;
import com.mysql.cluster.ndbj.NdbResultSet;

public class DecimalTest extends BaseNdbjTestCase {

    private static final String tablename = "t_decimal";
    private static final String col1 = "id";
    private static final String col2 = "val";
    private static final BigDecimal col2Val = new BigDecimal("10.50");
    private static final int NUM_INSERTS = 2;

    public DecimalTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        createTable(tablename,	"(`id` int PRIMARY KEY, " +
                    " `val` decimal(5,2)) ENGINE=ndb");
    }

    /**
     * Basic tests for BigDecimal/decimal support.
     */
    public void testDecimal() throws SQLException {
        // insert a few rows
        trans = ndb.startTransaction();
        for (int i = 1; i <= NUM_INSERTS; i++) {
            NdbOperation op = trans.getInsertOperation(tablename);
            op.equalInt(col1, i);
            op.setDecimal(col2, col2Val);
        }
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // retrieve the rows
        trans = ndb.startTransaction();

        NdbResultSet[] theResult = new NdbResultSet[NUM_INSERTS];

        for (int i = 1; i <= NUM_INSERTS; i++) {
            NdbOperation op = trans.getSelectOperation(tablename, LockMode.LM_Read);

            op.equalInt(col1, i);
            op.getValue(col2);
            theResult[i-1] = op.resultData();
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        // make sure we have the same values
        for (NdbResultSet r : theResult) {
            while (r.next()) {
                BigDecimal val = r.getDecimal(col2);
                assertEquals(col2Val, val);
            }
        }

        trans.close();

        // verify the same value is retrieved via jdbc/mysqld
        BigDecimal bdval = (BigDecimal)getSingleValue(tablename, col2,
                                                      "where id = 1");
        assertEquals(col2Val, bdval);
    }
}
