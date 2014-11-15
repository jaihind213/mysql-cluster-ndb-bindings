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

package testsuite.mgmj;

import testsuite.BaseMgmjTestCase;

import com.mysql.cluster.mgmj.NdbMgmException;
import com.mysql.cluster.mgmj.BackupStartOption;

public class BackupRestoreTest extends BaseMgmjTestCase {

    private static final BackupStartOption  WAIT_TYPE = BackupStartOption.WaitUntilBackupCompleted;
    private static final int BACKUP_ID =1;

    public BackupRestoreTest(String arg0) {
        super(arg0);
    }

    /*
     * Test method for 'com.mysql.mgmapi.NdbMgm.startBackup(int, int)'
     */
    public void testStartBackup() {

        long backupID;
        try {
            backupID = mgm.startBackup(WAIT_TYPE);
            System.out.println("Backup started with id="+backupID);
            assertTrue(true);
        }
        catch (NdbMgmException e) {
            System.err.println("Backup Failed:\t" + e.toString());
            assertFalse(true);
        }
    }

    /*
     * Test method for 'com.mysql.mgmapi.NdbMgm.abortBackup(int)'
     */
    public void testAbortBackup() {
        try {
            mgm.abortBackup(BACKUP_ID);
            assertTrue(true);
        }
        catch (NdbMgmException e) {
            System.err.println("Backup Failed");
            assertFalse(true);
        }
    }

}
