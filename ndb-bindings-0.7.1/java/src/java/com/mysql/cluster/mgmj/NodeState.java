package com.mysql.cluster.mgmj;

public interface NodeState {

  public abstract String getConnectAddress();

  public abstract int getConnectCount();

  public abstract int getDynamicID();

  public abstract int getNodeGroup();

  public abstract int getNodeID();

  public abstract NodeStatus getNodeStatus();

  public abstract NodeType getNodeType();

  public abstract int getStartPhase();

  public abstract int getVersion();

  public abstract String getStartupPhase();

  public abstract int getMysqlVersion();

}