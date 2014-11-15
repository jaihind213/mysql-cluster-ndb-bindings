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

import com.mysql.cluster.mgmj.events.TransReportCounters;
import com.mysql.cluster.mgmj.listeners.TransReportCountersTypeListener;

public class TransactionReportListener extends TransReportCountersTypeListener {

    private static int counter=0;

    @Override
    public void handleEvent(TransReportCounters event) {


        if (event == null)
            return;

        TransactionReportListener.counter += event.getReadCount();
        System.out.println("Node "
                           + event.getSourceNodeId() +
                           "\tTransaction Count: "
                           + TransactionReportListener.counter);

    }

    public int getReadCount() {
        return counter;
    }

}
