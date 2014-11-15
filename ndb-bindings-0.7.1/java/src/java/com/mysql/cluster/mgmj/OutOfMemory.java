package com.mysql.cluster.mgmj;

public class OutOfMemory extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public OutOfMemory(String message) {
		super(message);
	}

}
