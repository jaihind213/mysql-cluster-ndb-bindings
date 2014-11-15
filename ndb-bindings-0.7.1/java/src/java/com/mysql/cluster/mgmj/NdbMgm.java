package com.mysql.cluster.mgmj;

import java.util.List;

public interface NdbMgm {

	public abstract int getNdbMgmErrorCode();

	public abstract int getLatestErrorCode();

	public abstract String getNdbMgmErrorMsg();

	public abstract String getLatestErrorMsg();

	public abstract String getLatestErrorDesc();

	public abstract void setName(String name);

	public abstract int setConnectstring(String connect_string)
			throws NdbMgmException;

	public abstract int setBindaddress(String arg) throws NdbMgmException;

	public abstract int setConnectTimeout(long seconds) throws NdbMgmException;

	public abstract int connect(String connectString, int no_retries,
			int retry_delay_in_seconds, boolean verbose) throws NdbMgmException;

	public abstract int connect(int no_retries, int retry_delay_in_seconds,
			boolean verbose) throws NdbMgmException;

	public abstract int disconnect() throws NdbMgmException;

	public abstract int getConfigurationNodeid() throws NdbMgmException;

	public abstract int getConnectedPort() throws NdbMgmException;

	public abstract boolean isConnected();

	public abstract String getConnectedHost() throws NdbMgmException;

	public abstract long startBackup(BackupStartOption wait_completed)
			throws NdbMgmException;

	public abstract NdbMgmReply abortBackup(long backup_id)
			throws NdbMgmException;

	public abstract ClusterState getStatus() throws NdbMgmException;

	public abstract NdbMgmReply enterSingleUserMode(long nodeId)
			throws NdbMgmException;

	public abstract NdbMgmReply exitSingleUserMode() throws NdbMgmException;

	public abstract int stop(int[] no_of_nodes) throws NdbMgmException;

	public abstract int stop(int[] no_of_nodes, boolean abort)
			throws NdbMgmException;

	public abstract int stop(int[] no_of_nodes, boolean abort, int[] disconnect)
			throws NdbMgmException;

	public abstract int restart(int[] no_of_nodes) throws NdbMgmException;

	public abstract int restart(int[] no_of_nodes, boolean initial,
			boolean nostart, boolean abort) throws NdbMgmException;

	public abstract int restart(int[] no_of_nodes, boolean initial, boolean nostart,
			boolean abort, int[] disconnect) throws NdbMgmException;

	public abstract int start(int[] no_of_nodes) throws NdbMgmException;

	public abstract int setClusterlogSeverityFilter(
			NdbLogEventSeverity severity, int enable) throws NdbMgmException;

	public abstract int getClusterlogLoglevel(int nodeId,
			NdbLogEventCategory category, int level, NdbMgmReplyImpl reply)
			throws NdbMgmException;

	public abstract NdbMgmSeverity getClusterLogSeverityFilter()
			throws NdbMgmException;

	public abstract NdbMgmLoglevel getClusterlogLoglevel()
			throws NdbMgmException;

  @Deprecated
	public abstract NdbLogEventManagerImpl createNdbLogEventManager(
			NdbFilterList filter) throws NdbMgmException;

	public abstract NdbLogEventManagerImpl createNdbLogEventManager(int[] filter)
			throws NdbMgmException;

	public abstract NdbMgmReply dumpState(int nodeId, int[] args,
			int num_args) throws NdbMgmException;

	public abstract int dumpState(int nodeId, int theState)
			throws NdbMgmException;

  public int stopNode(int node_id, boolean abort) throws NdbMgmException;

  public int stopNode(int node_id) throws NdbMgmException;

  public int stopNode(int node_id, boolean abort, int[] disconnect) throws NdbMgmException;

  public int restartNode(int node_id, boolean initial, boolean nostart, boolean abort) throws NdbMgmException;

  public int restartNode(int node_id, boolean initial, boolean nostart) throws NdbMgmException;

  public int restartNode(int node_id, boolean initial) throws NdbMgmException;

  public int restartNode(int node_id) throws NdbMgmException;

  public int restartNode(int node_id, boolean initial, boolean nostart, boolean abort, int[] disconnect) throws NdbMgmException;

  public int startNode(int node_id) throws NdbMgmException;

  public NdbLogEventManager createNdbLogEventManager(List<NdbFilterItem> filterList) throws NdbMgmException;

}