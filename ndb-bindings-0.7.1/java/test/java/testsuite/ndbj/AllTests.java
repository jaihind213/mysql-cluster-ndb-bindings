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


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {

    	/*
        String dbugString = (System.getProperty("MYSQL_DEBUG") == null)
            ? System.getProperty("MYSQL_DEBUG")
            : "d,info,error,query,general,where:O,/tmp/ndbj.trace";

        ndbj.dbugPush(dbugString);
        */
        TestSuite suite = new TestSuite("Test for testsuite.ndbj");
        //$JUnit-BEGIN$
        suite.addTestSuite(InvalidSchemaObjectVersionTest.class);
        suite.addTestSuite(DecimalTest.class);
        suite.addTestSuite(NdbOperationByteLongTest.class);
        suite.addTestSuite(NdbOperationMultiPartPrimaryKeyTest.class);
        suite.addTestSuite(NdbOperationsFailedTest.class);
        suite.addTestSuite(SelectCountTest.class);
        suite.addTestSuite(Latin1InsertReadTest.class);
        suite.addTestSuite(ImmediatePollTimeoutTest.class);
        suite.addTestSuite(OrderedIndexScanTest.class);
        suite.addTestSuite(NullSetTest.class);
        //suite.addTestSuite(OutOfConnectionProblemTest.class);
        suite.addTestSuite(NdbScanTest.class);
        suite.addTestSuite(AsyncTest.class);
        suite.addTestSuite(BasicTest.class);
        suite.addTestSuite(MultiPartPrimaryKeyStringTest.class);
        //suite.addTestSuite(QuickBFTest.class);
        suite.addTestSuite(NdbOperationIDTest.class);
        suite.addTestSuite(InsertRetryTest.class);
        suite.addTestSuite(Utf8InsertReadTest.class);
        suite.addTestSuite(NdbOperationTest.class);
        suite.addTestSuite(NdbBlobTest.class);
        suite.addTestSuite(NdbScanFilterTest.class);
        suite.addTestSuite(MultipleAsyncTest.class);
        suite.addTestSuite(IncrementTest.class);
        //$JUnit-END$
        return suite;
    }

}
