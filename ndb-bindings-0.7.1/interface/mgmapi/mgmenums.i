/* -*- mode: c++; c-basic-offset: 2; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

enum BackupStartOption {
  DontWait = 0,
  WaitUntilBackupStarted = 1,
  WaitUntilBackupCompleted = 2
};


/**
 *   NDB Cluster node types
 */
// API: 1   MGM: 2  NDB: 0
enum ndb_mgm_node_type {
  NDB_MGM_NODE_TYPE_UNKNOWN = -1  /** Node type not known*/
  ,NDB_MGM_NODE_TYPE_NDB = NODE_TYPE_DB   /** A database node */
  ,NDB_MGM_NODE_TYPE_API = NODE_TYPE_API   /** An application (NdbApi) node */
  ,NDB_MGM_NODE_TYPE_MGM = NODE_TYPE_MGM   /** A management server node */
};

/**
 *   Database node status
 */
enum ndb_mgm_node_status {
  /** Node status not known*/
  NDB_MGM_NODE_STATUS_UNKNOWN       = 0,
  /** No contact with node*/
  NDB_MGM_NODE_STATUS_NO_CONTACT    = 1,
  /** Has not run starting protocol*/
  NDB_MGM_NODE_STATUS_NOT_STARTED   = 2,
  /** Is running starting protocol*/
  NDB_MGM_NODE_STATUS_STARTING      = 3,
  /** Running*/
  NDB_MGM_NODE_STATUS_STARTED       = 4,
  /** Is shutting down*/
  NDB_MGM_NODE_STATUS_SHUTTING_DOWN = 5,
  /** Is restarting*/
  NDB_MGM_NODE_STATUS_RESTARTING    = 6,
  /** Maintenance mode*/
  NDB_MGM_NODE_STATUS_SINGLEUSER    = 7,
  /** Resume mode*/
  NDB_MGM_NODE_STATUS_RESUME        = 8,
  /** connected node --> added in7.3.5*/
  NDB_MGM_NODE_STATUS_CONNECTED = 9
};

enum ndb_mgm_event_severity {
  NDB_MGM_ILLEGAL_EVENT_SEVERITY = -1,
  /*  Must be a nonnegative integer (used for array indexing) */
  /** Cluster log on */
  NDB_MGM_EVENT_SEVERITY_ON    = 0,
  /** Used in NDB Cluster developement */
  NDB_MGM_EVENT_SEVERITY_DEBUG = 1,
  /** Informational messages*/
  NDB_MGM_EVENT_SEVERITY_INFO = 2,
  /** Conditions that are not error condition, but might require handling.
   */
  NDB_MGM_EVENT_SEVERITY_WARNING = 3,
  /** Conditions that, while not fatal, should be corrected. */
  NDB_MGM_EVENT_SEVERITY_ERROR = 4,
  /** Critical conditions, like device errors or out of resources */
  NDB_MGM_EVENT_SEVERITY_CRITICAL = 5,
  /** A condition that should be corrected immediately,
   *  such as a corrupted system
   */
  NDB_MGM_EVENT_SEVERITY_ALERT = 6,
  /* must be next number, works as bound in loop */
  /** All severities */
  NDB_MGM_EVENT_SEVERITY_ALL = 7
};

/**
 *  Log event categories, used to set filter level on the log events using
 *  ndb_mgm_set_clusterlog_loglevel() and ndb_mgm_listen_event()
 */
enum ndb_mgm_event_category {
  /**
   * Invalid log event category
   */
  NDB_MGM_ILLEGAL_EVENT_CATEGORY = -1,
  /**
   * Log events during all kinds of startups
   */
  NDB_MGM_EVENT_CATEGORY_STARTUP = CFG_LOGLEVEL_STARTUP,
  /**
   * Log events during shutdown
   */
  NDB_MGM_EVENT_CATEGORY_SHUTDOWN = CFG_LOGLEVEL_SHUTDOWN,
  /**
   * Statistics log events
   */
  NDB_MGM_EVENT_CATEGORY_STATISTIC = CFG_LOGLEVEL_STATISTICS,
  /**
   * Log events related to checkpoints
   */
  NDB_MGM_EVENT_CATEGORY_CHECKPOINT = CFG_LOGLEVEL_CHECKPOINT,
  /**
   * Log events during node restart
   */
  NDB_MGM_EVENT_CATEGORY_NODE_RESTART = CFG_LOGLEVEL_NODERESTART,
  /**
   * Log events related to connections between cluster nodes
   */
  NDB_MGM_EVENT_CATEGORY_CONNECTION = CFG_LOGLEVEL_CONNECTION,
  /**
   * Backup related log events
   */
  NDB_MGM_EVENT_CATEGORY_BACKUP = CFG_LOGLEVEL_BACKUP,
  /**
   * Congestion related log events
   */
  NDB_MGM_EVENT_CATEGORY_CONGESTION = CFG_LOGLEVEL_CONGESTION,
  /**
   * Loglevel debug
   */
  NDB_MGM_EVENT_CATEGORY_DEBUG = CFG_LOGLEVEL_DEBUG,
  /**
   * Uncategorized log events (severity info)
   */
  NDB_MGM_EVENT_CATEGORY_INFO = CFG_LOGLEVEL_INFO,
  /**
   * Uncategorized log events (severity warning or higher)
   */
  NDB_MGM_EVENT_CATEGORY_ERROR = CFG_LOGLEVEL_ERROR,
  NDB_MGM_MIN_EVENT_CATEGORY = CFG_MIN_LOGLEVEL,
  NDB_MGM_MAX_EVENT_CATEGORY = CFG_MAX_LOGLEVEL


};

enum Ndb_logevent_type {

  NDB_LE_ILLEGAL_TYPE = -1,

  /** NDB_MGM_EVENT_CATEGORY_CONNECTION */
  NDB_LE_Connected = 0,
  /** NDB_MGM_EVENT_CATEGORY_CONNECTION */
  NDB_LE_Disconnected = 1,
  /** NDB_MGM_EVENT_CATEGORY_CONNECTION */
  NDB_LE_CommunicationClosed = 2,
  /** NDB_MGM_EVENT_CATEGORY_CONNECTION */
  NDB_LE_CommunicationOpened = 3,
  /** NDB_MGM_EVENT_CATEGORY_CONNECTION */
  NDB_LE_ConnectedApiVersion = 51,

  /** NDB_MGM_EVENT_CATEGORY_CHECKPOINT */
  NDB_LE_GlobalCheckpointStarted = 4,
  /** NDB_MGM_EVENT_CATEGORY_CHECKPOINT */
  NDB_LE_GlobalCheckpointCompleted = 5,
  /** NDB_MGM_EVENT_CATEGORY_CHECKPOINT */
  NDB_LE_LocalCheckpointStarted = 6,
  /** NDB_MGM_EVENT_CATEGORY_CHECKPOINT */
  NDB_LE_LocalCheckpointCompleted = 7,
  /** NDB_MGM_EVENT_CATEGORY_CHECKPOINT */
  NDB_LE_LCPStoppedInCalcKeepGci = 8,
  /** NDB_MGM_EVENT_CATEGORY_CHECKPOINT */
  NDB_LE_LCPFragmentCompleted = 9,

  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_NDBStartStarted = 10,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_NDBStartCompleted = 11,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_STTORRYRecieved = 12,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_StartPhaseCompleted = 13,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_CM_REGCONF = 14,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_CM_REGREF = 15,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_FIND_NEIGHBOURS = 16,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_NDBStopStarted = 17,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_NDBStopCompleted = 53,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_NDBStopForced = 59,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_NDBStopAborted = 18,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_StartREDOLog = 19,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_StartLog = 20,
  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_UNDORecordsExecuted = 21,

  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NR_CopyDict = 22,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NR_CopyDistr = 23,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NR_CopyFragsStarted = 24,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NR_CopyFragDone = 25,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NR_CopyFragsCompleted = 26,

  /* NODEFAIL */
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NodeFailCompleted = 27,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_NODE_FAILREP = 28,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_ArbitState = 29,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_ArbitResult = 30,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_GCP_TakeoverStarted = 31,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_GCP_TakeoverCompleted = 32,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_LCP_TakeoverStarted = 33,
  /** NDB_MGM_EVENT_CATEGORY_NODE_RESTART */
  NDB_LE_LCP_TakeoverCompleted = 34,

  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_TransReportCounters = 35,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_OperationReportCounters = 36,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_TableCreated = 37,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_UndoLogBlocked = 38,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_JobStatistic = 39,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_SendBytesStatistic = 40,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_ReceiveBytesStatistic = 41,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
  NDB_LE_MemoryUsage = 50,
  /** NDB_MGM_EVENT_CATEGORY_STATISTIC */
#if defined(CGE63)
  NDB_LE_ThreadConfigLoop = 68,
#endif

  /** NDB_MGM_EVENT_CATEGORY_ERROR */
  NDB_LE_TransporterError = 42,
  /** NDB_MGM_EVENT_CATEGORY_ERROR */
  NDB_LE_TransporterWarning = 43,
  /** NDB_MGM_EVENT_CATEGORY_ERROR */
  NDB_LE_MissedHeartbeat = 44,
  /** NDB_MGM_EVENT_CATEGORY_ERROR */
  NDB_LE_DeadDueToHeartbeat = 45,
  /** NDB_MGM_EVENT_CATEGORY_ERROR */
  NDB_LE_WarningEvent = 46,

  /** NDB_MGM_EVENT_CATEGORY_INFO */
  NDB_LE_SentHeartbeat = 47,
  /** NDB_MGM_EVENT_CATEGORY_INFO */
  NDB_LE_CreateLogBytes = 48,
  /** NDB_MGM_EVENT_CATEGORY_INFO */
  NDB_LE_InfoEvent = 49,

  /* 50 used */
  /* 51 used */

  /* SINGLE USER */
  NDB_LE_SingleUser = 52,
  /* 53 used */

  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_BackupStarted = 54,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_BackupFailedToStart = 55,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_BackupStatus = 62,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_BackupCompleted = 56,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_BackupAborted = 57,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_RestoreMetaData = 63,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_RestoreData = 64,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_RestoreLog = 65,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_RestoreStarted = 66,
  /** NDB_MGM_EVENT_CATEGORY_BACKUP */
  NDB_LE_RestoreCompleted = 67,

  /** NDB_MGM_EVENT_CATEGORY_INFO */
  NDB_LE_EventBufferStatus = 58,

  /* 59 used */

  /** NDB_MGM_EVENT_CATEGORY_STARTUP */
  NDB_LE_StartReport = 60

  /* 61 (used in upcoming patch) */
  /* 62-68 used */
  /* 69 unused */

};
