package com.mysql.cluster.mgmj;

public class CouldNotAbortBackup extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public CouldNotAbortBackup(String message) {
		super(message);
	}

}
