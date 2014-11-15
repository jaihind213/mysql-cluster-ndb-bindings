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
import com.mysql.cluster.ndbj.NdbScanFilter;
import com.mysql.cluster.ndbj.NdbScanOperation;
import com.mysql.cluster.ndbj.NdbTransaction;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbScanFilter.BinaryCondition;
import com.mysql.cluster.ndbj.NdbScanFilter.Group;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class NdbScanFilterTest extends BaseNdbjTestCase
{
    private static final int NUM_ENTRIES = 10000;
    private static final String tablename = "t_scan_filter";
    private static final String col1 = "id";
    private static final String col2 = "utf8LongVarChar";
    private static final String col3 = "reference";
    private static final String col4 = "long_num";
    private static final String col5 = "latin1VarChar";
    private static final String col6 = "char";
    private static final String col7 = "LongVarBinary";

    private static final int strSize = 100;

    public NdbScanFilterTest(String arg0)
        {
            super(arg0);
        }

    @Override
    public void setUp() throws Exception
        {
            super.setUp();
            createTable(tablename,
                        "(`"+col1+"` INT PRIMARY KEY, `"+col2+"` VARCHAR(256) CHARSET utf8, `"
                        +col3+"` INT, `"+col4+"` BIGINT, `"+col5+"` VARCHAR("+strSize+"), `" +col6
                        + "` CHAR(20), `" + col7 + "` VARBINARY(256)"
                        + ") ENGINE=ndb;"); //CHARSET utf8
        }

    private static interface ScanDetails
    {
        public void defineFiltering(NdbScanOperation scan) throws SQLException;
    }

    private static ScanDetails noFilter = new ScanDetails() {
            public void defineFiltering(NdbScanOperation scan) { }
        };

    private void generalScan(String testName, ScanDetails sd, int resultsExpected) throws SQLException
        {
            NdbTransaction scanTrans = ndb.startTransaction();

            NdbScanOperation scan = scanTrans.getSelectScanOperation(tablename,LockMode.LM_CommittedRead);

            sd.defineFiltering(scan);

            rs = scan.resultData();

            scanTrans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

            int tupleCount=0;
            while (rs.next())
            {
                tupleCount++;
            }
            assertEquals(testName, resultsExpected, tupleCount);

            scanTrans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

            scan.close(true, true);
            scanTrans.close();
        }

    private class colNIntMatchFilter implements ScanDetails
    {
        int value;
        int column;

        public colNIntMatchFilter(int _column, int _value)
            {
                value=_value;
                column=_column;
            }

        public void defineFiltering(NdbScanOperation scan) throws SQLException
            {
                NdbScanFilter sf=scan.getNdbScanFilter();

                sf.begin(Group.AND);
                sf.eq(column, value);
                sf.end();
            }
    }

    private class colNLongMatchFilter implements ScanDetails
    {
        long value;
        int column;

        public colNLongMatchFilter(int _column, long _value)
            {
                value=_value;
                column=_column;
            }

        public void defineFiltering(NdbScanOperation scan) throws SQLException
            {
                NdbScanFilter sf=scan.getNdbScanFilter();

                sf.begin(Group.AND);
                sf.eq(column,value);
                sf.end();
            }
    }

    private class colNStringMatchFilter implements ScanDetails
    {
        String value;
        int column;

        public colNStringMatchFilter(int _column, String _value)
            {
                value=_value;
                column=_column;
            }

        public void defineFiltering(NdbScanOperation scan) throws NdbApiException
            {
                NdbScanFilter sf=scan.getNdbScanFilter();
                sf.begin();
                sf.eq(column, value);
                sf.end();
            }
    }

    private class colNLikeMatchFilter implements ScanDetails
    {
        String value;
        int column;

        public colNLikeMatchFilter(int _column, String _value)
            {
                value=_value;
                column=_column;
            }

        public void defineFiltering(NdbScanOperation scan) throws SQLException
            {
                NdbScanFilter sf=scan.getNdbScanFilter();

                sf.begin(Group.AND);
                // TODO: Fix naming issue
                sf.cmpString(BinaryCondition.COND_LIKE, column, value);
                sf.end();
            }
    }

    public void testScan() throws SQLException
        {
            NdbOperation op;

            // First, datafill the table with appropriate data
            trans=ndb.startTransaction();

            for (int i=0; i<NUM_ENTRIES-1; i++)
            {
                op = trans.getInsertOperation(tablename);

                op.setInt(col1, i);
                op.setString(col2, "the same");
                op.setInt(col3, 12);
                op.setLong(col4, 12);
                op.setString(col5, Integer.toString(12));
                op.setString(col6, Integer.toString(12));
                op.setString(col7, Integer.toString(12));
            }
            trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
            trans.close();

            // Add a non-matching entry...
            trans=ndb.startTransaction();

            op = trans.getInsertOperation(tablename);

            op.setInt(col1, NUM_ENTRIES);
            op.setString(col2, "different");
            op.setInt(col3, 13);
            op.setLong(col4, 13);
            op.setString(col5, Integer.toString(13));
            op.setString(col6, Integer.toString(13));
            op.setString(col7, Integer.toString(13));

            trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
            trans.close();

            generalScan("Open scan - no filter", noFilter, NUM_ENTRIES);

            generalScan("Match most entries on int", new colNIntMatchFilter(2, 12), NUM_ENTRIES-1);
            generalScan("Match one entry on int", new colNIntMatchFilter(2, 13), 1);
            generalScan("Match zero entries on int", new colNIntMatchFilter(2, 14), 0);

            generalScan("Match most entries on int", new colNLongMatchFilter(2, 12), NUM_ENTRIES-1);
            generalScan("Match one entry on int", new colNLongMatchFilter(2, 13), 1);
            generalScan("Match zero entries on int", new colNLongMatchFilter(2, 14), 0);

            generalScan("Match most entries on string (EQUALS)", new colNStringMatchFilter(4, "12"), NUM_ENTRIES-1);
            generalScan("Match one entry on string (EQUALS)", new colNStringMatchFilter(4, "13"), 1);
            generalScan("Match zero entries on a string (EQUALS)", new colNStringMatchFilter(4, "14"), 0);

            generalScan("Match most entries on string compare (LIKE)", new colNLikeMatchFilter(4, "12"), NUM_ENTRIES-1);
            generalScan("Match one entry on string (LIKE)", new colNLikeMatchFilter(4, "13"), 1);
            generalScan("Match zero entries on a string (LIKE)", new colNLikeMatchFilter(4, "14"), 0);

            generalScan("Match most entries on char (EQUALS)", new colNStringMatchFilter(5, "12"), NUM_ENTRIES-1);
            generalScan("Match one entry on char (EQUALS)", new colNStringMatchFilter(5, "13"), 1);
            generalScan("Match zero entries on a char (EQUALS)", new colNStringMatchFilter(5, "14"), 0);

            generalScan("Match most entries on char compare (LIKE)", new colNLikeMatchFilter(5, "12"), NUM_ENTRIES-1);
            generalScan("Match one entry on char (LIKE)", new colNLikeMatchFilter(5, "13"), 1);
            generalScan("Match zero entries on a char (LIKE)", new colNLikeMatchFilter(5, "14"), 0);

            generalScan("Match most entries on varbinary (EQUALS)", new colNStringMatchFilter(6, "12"), NUM_ENTRIES-1);
            generalScan("Match one entry on varbinary (EQUALS)", new colNStringMatchFilter(6, "13"), 1);
            generalScan("Match zero entries on a varbinary (EQUALS)", new colNStringMatchFilter(6, "14"), 0);

            generalScan("Match most entries on VARBINARY compare (LIKE)", new colNLikeMatchFilter(5, "12"), NUM_ENTRIES-1);
            generalScan("Match one entry on VARBINARY (LIKE)", new colNLikeMatchFilter(5, "13"), 1);
            generalScan("Match zero entries on a VARBINARY (LIKE)", new colNLikeMatchFilter(5, "14"), 0);
        }
}
