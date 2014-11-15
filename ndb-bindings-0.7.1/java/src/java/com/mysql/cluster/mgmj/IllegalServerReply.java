package com.mysql.cluster.mgmj;

public class IllegalServerReply extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public IllegalServerReply(String message) {
		super(message);
	}

}
