package com.mysql.cluster.ndbj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

public class NdbAtomicOperationImpl implements NdbAtomicOperation {
	
	NdbOperationImpl theOp = null;
	
	protected NdbAtomicOperationImpl(NdbOperationImpl op) {
		theOp = op;
	}

	public void decrement(NdbColumn theColumn, BigInteger aValue) throws NdbApiException {
		theOp.decrement(NdbColumn.getCPtr(theColumn), aValue);
	}

	public void decrement(NdbColumn theColumn, long aValue) throws NdbApiException {
		theOp.decrement(NdbColumn.getCPtr(theColumn), aValue);
	}

	public void decrement(String aColumnName, BigInteger aValue) throws NdbApiException {
		theOp.decrement(aColumnName, aValue);
	}

	public void decrement(String aColumnName, long aValue) throws NdbApiException {
		theOp.decrement(aColumnName, aValue);
	}

	public void equal(long columnId, BigInteger val) throws NdbApiException {
		theOp.equal(columnId, val);
	}

	public void equal(long columnId, byte[] val) throws NdbApiException {
		theOp.equal(columnId, val);
	}

	public void equal(long columnId, int val) throws NdbApiException {
		theOp.equal(columnId, val);
	}

	public void equal(long columnId, long val) throws NdbApiException {
		theOp.equal(columnId, val);
	}

	public void equal(long columnId, String val) throws NdbApiException {
		theOp.equal(columnId, val);
	}

	public void equal(long columnId, Timestamp val) throws NdbApiException {
		theOp.equal(columnId, val);
	}

	public void equal(String columnName, BigInteger val) throws NdbApiException {
		theOp.equal(columnName, val);
	}

	public void equal(String columnName, byte[] val) throws NdbApiException {
		theOp.equal(columnName, val);
	}

	public void equal(String columnName, int val) throws NdbApiException {
		theOp.equal(columnName, val);
	}

	public void equal(String columnName, long val) throws NdbApiException {
		theOp.equal(columnName, val);
	}

	public void equal(String columnName, String val) throws NdbApiException {
		theOp.equal(columnName, val);
	}

	public void equal(String columnName, Timestamp val) throws NdbApiException {
		theOp.equal(columnName, val);
	}

	public void equalBytes(long anAttrId, byte[] BYTE) throws NdbApiException {
		theOp.equalBytes(anAttrId, BYTE);
	}

	public void equalBytes(NdbColumn theColumn, byte[] BYTE) throws NdbApiException {
		theOp.equalBytes(theColumn, BYTE);
	}

	public void equalBytes(String anAttrName, byte[] BYTE) throws NdbApiException {
		theOp.equalBytes(anAttrName, BYTE);
	}

	public void equalDatetime(long anAttrId, Timestamp anInputDateTime) throws NdbApiException {
		theOp.equalDatetime(anAttrId, anInputDateTime);
	}

	public void equalDatetime(NdbColumn theColumn, Timestamp anInputDateTime) throws NdbApiException {
		theOp.equalDatetime(theColumn, anInputDateTime);
	}

	public void equalDatetime(String anAttrName, Timestamp anInputDateTime) throws NdbApiException {
		theOp.equalDatetime(anAttrName, anInputDateTime);
	}

	public void equalDecimal(long anAttrId, BigDecimal decVal) throws NdbApiException {
		theOp.equalDecimal(anAttrId, decVal);
	}

	public void equalDecimal(NdbColumn theColumn, BigDecimal decVal) throws NdbApiException {
		theOp.equalDecimal(theColumn, decVal);
	}

	public void equalDecimal(String anAttrName, BigDecimal decVal) throws NdbApiException {
		theOp.equalDecimal(anAttrName, decVal);
	}

	public void equalInt(long anAttrId, int aValue) throws NdbApiException {
		theOp.equalInt(anAttrId, aValue);
	}

	public void equalInt(NdbColumn theColumn, int theValue) throws NdbApiException {
		theOp.equalInt(theColumn, theValue);
	}

	public void equalInt(String anAttrName, int aValue) throws NdbApiException {
		theOp.equalInt(anAttrName, aValue);
	}

	public void equalLong(long anAttrId, long aValue) throws NdbApiException {
		theOp.equalLong(anAttrId, aValue);
	}

	public void equalLong(NdbColumn theColumn, long theValue) throws NdbApiException {
		theOp.equalLong(theColumn, theValue);
	}

	public void equalLong(String anAttrName, long aValue) throws NdbApiException {
		theOp.equalLong(anAttrName, aValue);
	}

	public void equalNull(long anAttrId) throws NdbApiException {
		theOp.equalNull(anAttrId);
	}

	public void equalNull(NdbColumn theColumn) throws NdbApiException {
		theOp.equalNull(theColumn);
	}

	public void equalNull(String anAttrName) throws NdbApiException {
		theOp.equalNull(anAttrName);
	}

	public boolean equals(Object obj) {
		return theOp.equals(obj);
	}

	public void equalShort(long anAttrId, short theValue) throws NdbApiException {
		theOp.equalShort(anAttrId, theValue);
	}

	public void equalShort(NdbColumn theColumn, short theValue) throws NdbApiException {
		theOp.equalShort(theColumn, theValue);
	}

	public void equalShort(String anAttrName, short theValue) throws NdbApiException {
		theOp.equalShort(anAttrName, theValue);
	}

	public void equalString(long anAttrId, String anInputString) throws NdbApiException {
		theOp.equalString(anAttrId, anInputString);
	}

	public void equalString(String anAttrName, String anInputString) throws NdbApiException {
		theOp.equalString(anAttrName, anInputString);
	}

	public void equalTimestamp(long anAttrId, Timestamp anInputTimestamp) throws NdbApiException {
		theOp.equalTimestamp(anAttrId, anInputTimestamp);
	}

	public void equalTimestamp(NdbColumn theColumn, Timestamp anInputTimestamp) throws NdbApiException {
		theOp.equalTimestamp(theColumn, anInputTimestamp);
	}

	public void equalTimestamp(String anAttrName, Timestamp anInputTimestamp) throws NdbApiException {
		theOp.equalTimestamp(anAttrName, anInputTimestamp);
	}

	public void equalUint(long anAttrId, long aValue) throws NdbApiException {
		theOp.equalUint(anAttrId, aValue);
	}

	public void equalUint(NdbColumn theColumn, long theValue) throws NdbApiException {
		theOp.equalUint(theColumn, theValue);
	}

	public void equalUint(String anAttrName, long aValue) throws NdbApiException {
		theOp.equalUint(anAttrName, aValue);
	}

	public void equalUlong(long anAttrId, BigInteger aValue) throws NdbApiException {
		theOp.equalUlong(anAttrId, aValue);
	}

	public void equalUlong(NdbColumn theColumn, BigInteger theValue) throws NdbApiException {
		theOp.equalUlong(theColumn, theValue);
	}

	public void equalUlong(String anAttrName, BigInteger aValue) throws NdbApiException {
		theOp.equalUlong(anAttrName, aValue);
	}

	public void getBlob(long columnId, int length) throws NdbApiException {
		theOp.getBlob(columnId, length);
	}

	public void getBlob(long columnId) throws NdbApiException {
		theOp.getBlob(columnId);
	}

	public void getBlob(String columnName, int length) throws NdbApiException {
		theOp.getBlob(columnName, length);
	}

	public void getBlob(String columnName) throws NdbApiException {
		theOp.getBlob(columnName);
	}

	public NdbBlobImpl getBlobHandle(long anAttrId) throws NdbApiException {
		return theOp.getBlobHandle(anAttrId);
	}

	public NdbBlobImpl getBlobHandle(String anAttrName) throws NdbApiException {
		return theOp.getBlobHandle(anAttrName);
	}

	public void getValue(long columnId) throws NdbApiException {
		theOp.getValue(columnId);
	}

	public void getValue(NdbColumn theColumn) throws NdbApiException {
		theOp.getValue(theColumn);
	}

	public void getValue(String columnName) throws NdbApiException {
		theOp.getValue(columnName);
	}

	public void increment(NdbColumn theColumn, BigInteger aValue) throws NdbApiException {
		theOp.increment(NdbColumn.getCPtr(theColumn), aValue);
	}

	public void increment(NdbColumn theColumn, long aValue) throws NdbApiException {
		theOp.increment(NdbColumn.getCPtr(theColumn), aValue);
	}

	public void increment(String aColumnName, BigInteger aValue) throws NdbApiException {
		theOp.increment(aColumnName, aValue);
	}

	public void increment(String aColumnName, long aValue) throws NdbApiException {
		theOp.increment(aColumnName, aValue);
	}

	public NdbResultSet resultData() {
		return theOp.resultData();
	}

}
