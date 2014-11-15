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
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

public class NdbOperationsFailedTest extends BaseNdbjTestCase {

    private static final String tablename = "t_ops";

    private static final int NUM_INSERTS=10;

    private static final String col1 = "id";
    private static final String col2 = "name";

    public NdbOperationsFailedTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(`id` INT PRIMARY KEY, `name` VARCHAR(256)) ENGINE=ndb;");
    }

    public void testCountNumberOfCompletedOperations() throws NdbApiException {
        String name = "jd";
        int id = 0;
        NdbOperation op;

        trans = ndb.startTransaction();

        for (int j = 0; j < NUM_INSERTS; j++) {
            op = trans.getInsertOperation(tablename);

            op.equalInt(col1, id++);
            name = name.concat(Integer.toString(id));
            op.setString(col2, name);
            name = name.substring(0, name.length() - 1);
        }

        // Duplicate insert is also counted as a completed operation (even though it fails!)
        try {
            op = trans.getInsertOperation(tablename);
            op.equalInt(col1, id - 1);
            op.setString(col2, name);

            op = trans.getInsertOperation(tablename);
            op.equalInt(col1, id - 1);
            op.setString(col2, "jim ");

            trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

            fail("Exception should be thrown");
        } catch (NdbApiException e) {
            int opCount = 0;
            NdbOperation completedOp = trans.getNextCompletedOperation();
            do {
                ++opCount;
            } while ((completedOp = trans.getNextCompletedOperation(completedOp)) != null);

            assertEquals(opCount, NUM_INSERTS+2);
        } finally {
            trans.close();
        }

        try {
            trans = ndb.startTransaction();

            for (int j = 0; j < NUM_INSERTS; j++) {
                op = trans.getInsertOperation(tablename);
                op.equalInt(col1, id++);
                name = name.concat(Integer.toString(id));
                op.setString(col2, name);
                name = name.substring(0, name.length() - 1);
            }

            op = trans.getInsertOperation(tablename);
            op.equalInt(col1, id - 1);
            op.setString(col2, name);

            // This operation should fail - duplicate insert
            op = trans.getInsertOperation(tablename);
            op.equalInt(col1, id - 1);
            op.setString(col2, "jim ");

            trans.execute(ExecType.NoCommit,
                          AbortOption.AbortOnError, true);

            // shouldn't get this far
            fail("Exception should be thrown");
        } catch (NdbApiException e) {
            // TODO: getNextFailedOperation()
            assertTrue("Commented out section with getNextFailedOperation - doesn't exist yet",1==1);
            /*
              int failedOps=0;
              NdbOperation failedOp = trans.getNextFailedOperation(null);
              if (failedOp != null) {
              do {
              failedOps++;
              System.out.println("Failed operation error was:\t"
              + failedOp.getNdbError().getMessage());
              } while ((failedOp = trans.getNextFailedOperation(failedOp)) != null);
              }
              assertEquals(failedOps, NUM_FAILED_INSERTS);
            */
        } finally {
            if (trans != null)
                trans.close();
        }
    }

    /**
     * @todo enable this test
     */
    //public void testSelectMissingTupleGetNextFailedOperation() {
    //	try {
    //		trans = ndb.startTransaction();

    //		NdbOperationRef op = trans.getNdbOperation(tablename);
    //		op.committedRead();
    //		op.equal(col1, 1000);
    //		op.getValue(col2);
    //		NdbResultSet rs = op.resultData();

    //		trans.execute(NdbTransactionRef.ExecType.NoCommit,
    //		NdbTransactionRef.AbortOption.AbortOnError, 1);

    //		// shouldn't get this far
    //		fail("Exception should be thrown");
    //	}
    //	catch (NdbApiException e) {
    //		int opCount = 0;

    //		NdbOperationRef failedOp = trans.getNextFailedOperation(null);
    //		if (failedOp != null) {
    //			do {
    //				++opCount;
    //				System.out.println("Failed operation error was:\t"
    //					+ failedOp.getNdbError().getMessage());
    //			} while ((failedOp = trans.getNextFailedOperation(failedOp)) != null);
    //			assertEquals(opCount, 1);
    //		}
    //	}
    //	finally {
    //		if (trans != null)
    //			trans.close();
    //	}
    //}
}
