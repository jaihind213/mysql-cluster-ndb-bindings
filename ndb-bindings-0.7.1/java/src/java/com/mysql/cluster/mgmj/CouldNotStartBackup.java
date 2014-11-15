package com.mysql.cluster.mgmj;

public class CouldNotStartBackup extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public CouldNotStartBackup(String message) {
		super(message);
	}

}
