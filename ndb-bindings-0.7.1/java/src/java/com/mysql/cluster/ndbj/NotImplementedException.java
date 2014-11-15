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

package com.mysql.cluster.ndbj;

import java.sql.SQLException;


public class NotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotImplementedException() {
        super();
    }

    public NotImplementedException(Object[] args) {
        super("args: " + args == null ? null : args.toString());
    }

    public NotImplementedException(Object arg0) {
        this(new Object[] { arg0 });
    }

    public NotImplementedException(Object arg0, Object arg1) {
        this(new Object[] { arg0, arg1 });
    }

    public NotImplementedException(Object arg0, Object arg1, Object arg2) {
        this(new Object[] { arg0, arg1, arg2 });
    }

    static class AsSQLException extends SQLException {
        private static final long serialVersionUID = 1L;

        public AsSQLException(Throwable t) {
            super(t.getMessage());
            initCause(t);
        }
    }

    public SQLException asSQL() {
        return new AsSQLException(this);
    }
}
