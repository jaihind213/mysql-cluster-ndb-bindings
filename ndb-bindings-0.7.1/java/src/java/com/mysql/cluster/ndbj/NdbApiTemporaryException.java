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

/**
 * This is the NDB/J API package.
 */
package com.mysql.cluster.ndbj;

/**
 * Indicates that a temporary (transient) error has occured in the cluster (e.g., node failure)
 * and that if you wait for a short time (e.g., 1500ms), the error may have
 * gone away.
 * <br>Transactions that are aborted due to NdbApiTemporaryExceptions should be retried.
 *<br>
 * <code>
 *      boolean finished = false;<br>
 *      while (finished == false && numRetries-- > 0) {<br>
 *        try {<br>
 *        // Set up Transaction and create operations<br>
 *          trans.execute(NdbTransaction.ExecType.Commit, NdbTransaction.AbortOption.AbortOnError, true);<br>
 *          finished = true;<br> // transaction succeded, exit loop
 *        } <br>
 *        catch (NdbApiTemporaryException e) {<br>
 *          finished = false;<br> // transaction failed, retry if numRetries > 0
 *        }<br>
 *        catch (NdbApiUserAndPermanentException e) {<br>
 *          finished = true;<br> // transaction failed, do not retry
 *        }<br>
 *      }<br>
 * </code>
 * @see NdbTransaction
 */
public class NdbApiTemporaryException extends NdbApiException {
    protected static final long serialVersionUID=1L;

    /**
     * @param error
     */
    public NdbApiTemporaryException(String message, long error) {
        super(message, error);
    }

    /**
     * @param message
     */
    public NdbApiTemporaryException(String message) {
        super(message);
    }
}
