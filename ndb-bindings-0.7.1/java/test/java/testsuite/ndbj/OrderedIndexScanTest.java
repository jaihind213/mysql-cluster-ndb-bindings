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
import com.mysql.cluster.ndbj.NdbIndexScanOperation;
import java.sql.SQLException;

import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

public class OrderedIndexScanTest extends BaseNdbjTestCase {

    private static final String col1="id";
    private static final String col2="name";
    private static final String theTableName="t_oi_scan";

    public OrderedIndexScanTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.createTable(theTableName,
                         "(`"+col1+"` INT, `"+col2+"` int, primary key (`"+col1+"`,`"+col2+"`)) ENGINE=ndbcluster;");
        this.stmt.executeUpdate("insert into " + theTableName + " values (1,1)");
        this.stmt.executeUpdate("insert into " + theTableName + " values (1,2)");
        this.stmt.executeUpdate("insert into " + theTableName + " values (1,3)");
        this.stmt.executeUpdate("insert into " + theTableName + " values (1,4)");
        this.stmt.executeUpdate("insert into " + theTableName + " values (1,5)");
    }

    public void testOIScan() throws NdbApiException, SQLException {
        trans = ndb.startTransaction();
        NdbIndexScanOperation iop=
            trans.getSelectIndexScanOperation("PRIMARY", theTableName,
                                              LockMode.LM_CommittedRead,NdbIndexScanOperation.ScanFlag.ORDER_BY,0,0);

        iop.equalInt(1,1);

        assertEquals(NdbIndexScanOperation.BoundType.BoundEQ.type,4);
        /*
          iop.setBoundInt(col1,
          NdbIndexScanOperation.BoundType.BoundEQ,
          1);
        */

        iop.getValue(col1);
        iop.getValue(col2);

        rs = iop.resultData();
        trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

        int count=1;
        while(rs.next())
        {
            int col1Val = rs.getInt(col1);
            int col2Val = rs.getInt(col2);
            assertEquals(col1Val,1);
            assertEquals(col2Val,count);

            count++;
        }
        trans.close();
    }
}
