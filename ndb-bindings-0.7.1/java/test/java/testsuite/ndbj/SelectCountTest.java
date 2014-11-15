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

import java.util.Random;

import testsuite.BaseNdbjTestCase;

import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

public class SelectCountTest extends BaseNdbjTestCase {

    private static final String col1 = "id";
    private static final String col2 = "name";
    private static final String theTableName = "t_select_count";
    // SELECT_COUNT must be a multiple of BATCH_SIZE
    private static final int SELECT_COUNT = 12000;
    private static final int BATCH_SIZE = 1000;

    public SelectCountTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.createTable(theTableName,
                         "(`"+col1+"` INT PRIMARY KEY, `"+col2+"` VARCHAR(256)) ENGINE=ndbcluster;");
    }

    /**
     * Test for NdbTransaction.selectCount().
     */
    public void testCount() throws NdbApiException {
        long count;

        // insert SELECT_COUNT rows, BATCH_SIZE at a time
        for (int total=0; total < SELECT_COUNT; total+=BATCH_SIZE) {
            trans = ndb.startTransaction();
            for (int i=0; i < BATCH_SIZE; i++) {
                NdbOperation op = trans.getInsertOperation(theTableName);
                op.equalInt(col1, total + i);
                String input = "jim" + (total + i);
                op.setString(col2, input);
            }
            trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
            trans.close();
        }

        // check that the correct number of rows were inserted
        count = ndb.selectCount(theTableName);
        assertEquals(SELECT_COUNT, count);

        // delete a random number of rows
        int rows = 100 + new Random().nextInt(SELECT_COUNT / 2);

        trans = ndb.startTransaction();

        for (int i = 0; i < rows; ++i) {
            NdbOperation op = trans.getDeleteOperation(theTableName);
            op.equalInt(col1,i);
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // check count again
        count = ndb.selectCount(theTableName);
        assertEquals(SELECT_COUNT - rows, count);
    }
}
