package com.mysql.cluster.mgmj;

public interface NdbMgmReply {

	public abstract int getReturnCode();

	public abstract String getMessage();

}