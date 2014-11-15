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

/**
 * Various helper methods for type conversions
 */

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class Util {

    protected final static Calendar timeToCalendar(Time input) {
        java.util.Calendar output = Calendar.getInstance();
        output.setTimeInMillis(input.getTime());
        return output;
    }

    protected final static Calendar dateToCalendar(Date input) {
        java.util.Calendar output = Calendar.getInstance();
        output.setTime(input);
        return output;
    }

    protected final static Calendar timestampToCalendar(Timestamp input) {
        java.util.Calendar output = Calendar.getInstance();
        output.setTimeInMillis(input.getTime());
        return output;
    }

    /**
     * Convert a timestamp to seconds. The timezone is ignored and the current
     * date and time are taken to be relative to GMT. For example if
     * <code>2007-01-01 13:00:00 CDT</code> (CDT = GMT-5:00), we convert this
     * into seconds as if calling getTime() on <code>2007-01-01 13:00:00 GMT</code>.
     *
     * @param ts Timestamp in any timezone.
     * @return Seconds since epoch with timezone ignored.
     */
    static long timestampToLong(Timestamp ts)
    {
        /* It's easier to use getTime() and reverse instead of just ignore it. */
        return (ts.getTime() / 1000) - (ts.getTimezoneOffset() * 60);
    }

    static Timestamp timestampFromLong(long t)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(t * 1000);
        /* TODO what happens if the diff between GMT and the local timezone crosses DST? */
        return new Timestamp(cal.getTime().getTime() - TimeZone.getDefault().getOffset(t * 1000));
    }
}
