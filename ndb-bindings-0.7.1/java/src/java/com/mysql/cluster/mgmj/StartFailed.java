package com.mysql.cluster.mgmj;

public class StartFailed extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public StartFailed(String message) {
		super(message);
	}

}
