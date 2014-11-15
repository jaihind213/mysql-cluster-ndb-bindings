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

import java.sql.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.mysql.cluster.ndbj.*;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import testsuite.BaseNdbjTestCase;

public class DateTimeTest extends BaseNdbjTestCase {

	String tablename = "t_datetime";
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Timestamp inTstamp = null;
	Date inDate = null;
	Time inTime = null;

	public DateTimeTest(String arg0) {
		super(arg0);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		createTable(tablename,
		"(id integer not null primary key, " +
		"tstamp TIMESTAMP, " +
		"dt DATE, " +
		"dtime DATETIME, " +
		"tm TIME) engine=ndbcluster");

		inDate = new Date(df.parse("2002/07/03 07:13:42").getTime());
		inTstamp = new Timestamp(inDate.getTime());
		inTime = new Time(inDate.getTime());
	}

	/**
	 * Basic test of reading and writing date and time values through NDBAPI.
	 */
	@SuppressWarnings("deprecation")
	public void testNdbReadInsertDate() throws SQLException {
		trans = ndb.startTransaction();

		// insert date values
		NdbOperation op = trans.getInsertOperation(tablename);

		op.equalInt("id", 1);
		op.setTimestamp("tstamp", inTstamp);
		op.setDate("dt", inDate);
		op.setDatetime("dtime", inTstamp);
		op.setTime("tm", inTime);

		trans.executeCommit();

		// read date values
		trans = ndb.startTransaction();
		op = trans.getSelectOperation(tablename);
		op.equalInt("id", 1);

		op.getValue("tstamp");
		op.getValue("dt");
		op.getValue("dtime");
		op.getValue("tm");

		rs = op.resultData();

		trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

		assertTrue(rs.next());

		// check all values
		Timestamp outTstamp = rs.getTimestamp("tstamp");
		assertEquals(inTstamp, outTstamp);
		assertEquals(inTstamp.getTime(), outTstamp.getTime());

		// SQL date only stores the 'date', so we only compare those fields
		Date outDate = rs.getDate("dt");
		assertEquals(2002, outDate.getYear()+1900);
		assertEquals(inDate.getMonth(), outDate.getMonth());
		assertEquals(3, outDate.getDay());

		Timestamp outDatetime = rs.getTimestamp("dtime");
		assertEquals(inTstamp.getTime(), outDatetime.getTime());

		// string comparison includes hh:mm:ss time fields
		Time outTime = rs.getTime("tm");
		assertEquals(inTime.toString(), outTime.toString());

		assertFalse(rs.next());
	}

	/**
	 * Basic test of reading date and time values written through a JDBC connection.
	 * Due to the fact that MySQL server will convert a "timestamp" value from
	 * 'system_time_zone' to UTC, we perform the opposite conversion before this. In
	 * effect, the exact value from our program will be stored (no transformation).
	 */
	@SuppressWarnings("deprecation")
	public void testJdbcWriteNdbReadDate() throws SQLException {
		// insert values through jdbc connection
		pstmt = conn.prepareStatement("INSERT INTO " + tablename + 
									  " (id, tstamp, dt, dtime, tm) " +
									  "VALUES (?, convert_tz(?, 'UTC', 'SYSTEM'), ?, ?, ?)");
		pstmt.setInt(1, 1);
		pstmt.setTimestamp(2, inTstamp);
		pstmt.setDate(3, inDate);
		pstmt.setTimestamp(4, inTstamp);
		pstmt.setTime(5, inTime);
		pstmt.execute();

		// retrieve values through ndb connection
		trans = ndb.startTransaction();

		NdbOperation op = trans.getSelectOperation(tablename,
												   LockMode.LM_Read);

		op.equalInt("id", 1);
		op.getValue("tstamp");
		op.getValue("dt");
		op.getValue("dtime");
		op.getValue("tm");

		rs = op.resultData();

		trans.executeCommit();

		// Check that we have the same values
		assertTrue(rs.next());
		Timestamp outTstamp = rs.getTimestamp("tstamp");
		assertEquals(inTstamp.getTime(), outTstamp.getTime());

		Date outDate = rs.getDate("dt");
		assertEquals(2002, outDate.getYear()+1900);
		assertEquals(inDate.getMonth(), outDate.getMonth());
		assertEquals(3, outDate.getDay());

		Timestamp outDatetime = rs.getTimestamp("dtime");
		assertEquals("datetime", inTstamp.getTime(), outDatetime.getTime());

		Time outTime = rs.getTime("tm");
		assertEquals("time", inTime.toString(), outTime.toString());

		assertFalse(rs.next());
	}

	/**
	 * Basic test of writing date and time values and reading them through a
	 * JDBC connection. See inverse test for details about timestamp.
	 */
	@SuppressWarnings("deprecation")
	public void testNdbWriteJdbcReadDate() throws Exception {
		trans = ndb.startTransaction();
		NdbOperation op = trans.getInsertOperation(tablename);
		op.equalInt("id", 1);
		op.setTimestamp("tstamp", inTstamp);
		op.setDate("dt", inDate);
		op.setDatetime("dtime", inTstamp);
		op.setTime("tm", inTime);
		trans.executeCommit();

		ResultSet rs = conn.createStatement()
			.executeQuery("select convert_tz(tstamp, 'SYSTEM', 'UTC') as tstamp" +
						  ", dt, dtime, tm from " + tablename);

		assertTrue(rs.next());
		Timestamp outTstamp = rs.getTimestamp("tstamp");
		assertEquals(inTstamp.getTime(), outTstamp.getTime());

		Date outDate = rs.getDate("dt");
		assertEquals(2002, outDate.getYear()+1900);
		assertEquals(inDate.getMonth(), outDate.getMonth());
		assertEquals(3, outDate.getDay());

		Timestamp outDatetime = rs.getTimestamp("dtime");
		assertEquals("datetime", inTstamp.getTime(), outDatetime.getTime());

		Time outTime = rs.getTime("tm");
		assertEquals("time", inTime.toString(), outTime.toString());

		assertFalse(rs.next());
	}

	/**
	 * Test timestamp and datetime fields when using different timezones.
	 */
	public void testTimestampsTimezones() throws Exception {
		// init Timestamps
		Timestamp inEdt; /* US/Eastern */
		TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
		Calendar cal = Calendar.getInstance();//TimeZone.getTimeZone("US/Eastern"));
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(2002, 06, 03, 07, 13, 42);
		inEdt = new Timestamp(cal.getTime().getTime());
		assertEquals("2002-07-03 07:13:42.0", inEdt.toString());

		Timestamp inEest; /* Europe/Helsinki */
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Helsinki"));
		cal = Calendar.getInstance();//TimeZone.getTimeZone("Europe/Helsinki"));
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(2002, 06, 03, 07, 13, 42);
		inEest = new Timestamp(cal.getTime().getTime());
		assertEquals("2002-07-03 07:13:42.0", inEest.toString());

		// main test
		Timestamp inTSs[] = {inEdt, inEest};
		String clientTZs[] = {"US/Pacific", "US/Eastern", "Europe/London", "Europe/Moscow"};
		for(Timestamp ts : inTSs)
		{
			for(String tzname : clientTZs)
			{
				// write value
				TimeZone.setDefault(TimeZone.getTimeZone(tzname));
				trans = ndb.startTransaction();
				NdbOperation op = trans.getInsertOperation(tablename);
				op.equalInt("id", 1);
				op.setTimestamp("tstamp", ts);
				op.setDatetime("dtime", ts);
				trans.executeCommit();
				trans.close();

				// read value and verify
				trans = ndb.startTransaction();
				op = trans.getSelectOperation(tablename);
				op.equalInt("id", 1);
				op.getValue("tstamp");
				op.getValue("dtime");
				rs = op.resultData();
				trans.executeCommit();
				Timestamp outTstamp = rs.getTimestamp("tstamp");
				// exact times differ due to differing timezone, but same
				// values when printed as local time
				assertEquals(ts.toString(), outTstamp.toString());
				Timestamp outDatetime = rs.getTimestamp("dtime");
				assertEquals(ts.toString(), outDatetime.toString());
				trans.close();

				conn.createStatement().executeUpdate("delete from " + tablename);
			}
		}
	}
}
