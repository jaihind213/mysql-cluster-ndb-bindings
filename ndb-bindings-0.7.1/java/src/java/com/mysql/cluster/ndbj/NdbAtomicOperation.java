package com.mysql.cluster.ndbj;

import java.math.BigInteger;

public interface NdbAtomicOperation extends NdbBaseOperationEquals {

	   public void increment(NdbColumn theColumn, long aValue) throws NdbApiException;
	   public void increment(NdbColumn theColumn, BigInteger aValue) throws NdbApiException;
	   public void increment(String aColumnName, long aValue) throws NdbApiException;
	   public void increment(String aColumnName, BigInteger aValue) throws NdbApiException;

	   public void decrement(NdbColumn theColumn, long aValue) throws NdbApiException;
	   public void decrement(NdbColumn theColumn, BigInteger aValue) throws NdbApiException;
	   public void decrement(String aColumnName, long aValue) throws NdbApiException;
	   public void decrement(String aColumnName, BigInteger aValue) throws NdbApiException;
}

