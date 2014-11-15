package com.mysql.cluster.mgmj;

public class UsageError extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public UsageError(String message) {
		super(message);
	}

}
