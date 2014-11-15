// -*- mode: c++ -*- 
/*  ndb-bindings: Bindings for the NDB API
    Copyright (C) 2006 MySQL, Inc.
    
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or 
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

#ifndef events_h
#define events_h

#include "libmgmpp.h"

class BaseEvent { 
  Ndb_logevent_type theType;
  int theSourceNodeId; 
  Uint32 theEventTime;
  ndb_mgm_event_category theCategory; 
  ndb_mgm_event_severity theSeverity;
protected: 
  BaseEvent();
public: 
  ndb_logevent event; 
  explicit BaseEvent(const ndb_logevent & theEvent);
  Ndb_logevent_type getType();
  int getSourceNodeId();
  Uint32 getEventTime();
  ndb_mgm_event_category getEventCategory();
  ndb_mgm_event_severity getEventSeverity();
};



class Connected : public BaseEvent { 
public:
  explicit Connected(const ndb_logevent & theEvent);
  unsigned node;
};

class Disconnected : public BaseEvent { 
public:
  explicit Disconnected(const ndb_logevent & theEvent);
  unsigned node;
};

class CommunicationOpened : public BaseEvent { 
public:
  explicit CommunicationOpened(const ndb_logevent & theEvent);
  unsigned node;
};

class CommunicationClosed : public BaseEvent { 
public:
  explicit CommunicationClosed(const ndb_logevent & theEvent);
  unsigned node; 
  };

class ConnectedApiVersion : public BaseEvent { 
public:
  explicit ConnectedApiVersion(const ndb_logevent & theEvent);
  unsigned node;
  unsigned version;
};
  

class GlobalCheckpointStarted : public BaseEvent { 
public:
  
  unsigned gci;
  
  explicit GlobalCheckpointStarted(const ndb_logevent & theEvent);
};

  class GlobalCheckpointCompleted : public BaseEvent { 
  public:
    
    unsigned gci;

    explicit GlobalCheckpointCompleted(const ndb_logevent & theEvent);
  };


  class LocalCheckpointStarted : public BaseEvent { 
  public:

    unsigned lci;
    unsigned keepGci;
    unsigned restoreGci;
    
    explicit LocalCheckpointStarted(const ndb_logevent & theEvent);
  };

  class LocalCheckpointCompleted : public BaseEvent { 
  public:

    unsigned lci;

    explicit LocalCheckpointCompleted(const ndb_logevent & theEvent);
  };

  class LCPStoppedInCalcKeepGci : public BaseEvent {
  public:
    unsigned data;

    explicit LCPStoppedInCalcKeepGci(const ndb_logevent & theEvent);
  };

  class LCPFragmentCompleted : public BaseEvent { 
  public:
    unsigned node;
    unsigned tableId;
    unsigned fragmentId;

    explicit LCPFragmentCompleted(const ndb_logevent & theEvent);
  };

  class UndoLogBlocked : public BaseEvent { 
  public:
    unsigned accCount;
    unsigned tupCount;
    
    explicit UndoLogBlocked(const ndb_logevent & theEvent);
  };

  class NdbStartStarted : public BaseEvent { 
  public:
    unsigned version;

    explicit NdbStartStarted(const ndb_logevent & theEvent);
  };

  class NdbStartCompleted : public BaseEvent { 
  public:
    unsigned version;

    explicit NdbStartCompleted(const ndb_logevent & theEvent);
  };

  class STTORRYRecieved : public BaseEvent { 
  public:
    explicit STTORRYRecieved(const ndb_logevent & theEvent);
  };

  class StartPhaseCompleted : public BaseEvent { 
  public:
    unsigned phase;
    unsigned startType;

    explicit StartPhaseCompleted(const ndb_logevent & theEvent);
  };

  class CmRegConf : public BaseEvent { 
  public:
    unsigned ownId;
    unsigned presidentId;
    unsigned dynamicId;

    explicit CmRegConf(const ndb_logevent & theEvent);
  };

  class CmRegRef : public BaseEvent { 
  public:
    unsigned ownId;
    unsigned otherId;
    unsigned cause;

    explicit CmRegRef(const ndb_logevent & theEvent);
  };

  class FindNeighbours : public BaseEvent { 
  public:
    unsigned ownId;
    unsigned leftId;
    unsigned rightId;
    unsigned dynamicId;

    explicit FindNeighbours(const ndb_logevent & theEvent);
  };

  class NdbStopStarted : public BaseEvent { 
  public:
    unsigned stopType;

    explicit NdbStopStarted(const ndb_logevent & theEvent);
  };

  class NdbStopCompleted : public BaseEvent { 
  public:
    unsigned action;
    unsigned sigNum;

    explicit NdbStopCompleted(const ndb_logevent & theEvent);
  };

  class NdbStopForced : public BaseEvent { 
  public:
    unsigned action;
    unsigned signum;
    unsigned error;
    unsigned sphase;
    unsigned extra;
    
    explicit NdbStopForced(const ndb_logevent & theEvent);
  };

  class NdbStopAborted : public BaseEvent { 
  public:

    explicit NdbStopAborted(const ndb_logevent & theEvent);
  };

  class StartREDOLog : public BaseEvent { 
  public:
    unsigned node;
    unsigned keepGci;
    unsigned completedGci;
    unsigned restorableGci;
    
    explicit StartREDOLog(const ndb_logevent & theEvent);
  };

  class StartLog : public BaseEvent { 
  public:
    unsigned logPart;
    unsigned startMb;
    unsigned stopMb;
    unsigned gci;

    explicit StartLog(const ndb_logevent & theEvent);
  };

  class UNDORecordsExecuted : public BaseEvent { 
  public:
    unsigned block;
    unsigned data1;
    unsigned data2;
    unsigned data3;
    unsigned data4;
    unsigned data5;
    unsigned data6;
    unsigned data7;
    unsigned data8;
    unsigned data9;
    unsigned data10;
  
    explicit UNDORecordsExecuted(const ndb_logevent & theEvent);
  };

  class NRCopyDict : public BaseEvent { 
  public:
  
   explicit  NRCopyDict(const ndb_logevent & theEvent);
  };

  class NRCopyDistr : public BaseEvent { 
  public:
  
    explicit NRCopyDistr(const ndb_logevent & theEvent);
  };

  class NRCopyFragsStarted : public BaseEvent { 
  public:
    unsigned destNode;

    explicit NRCopyFragsStarted(const ndb_logevent & theEvent);
  };

  class NRCopyFragDone : public BaseEvent { 
  public:
    unsigned destNode;
    unsigned tableId;
    unsigned fragmentId;

    explicit NRCopyFragDone(const ndb_logevent & theEvent);

  };

  class NRCopyFragsCompleted : public BaseEvent { 
  public:
    unsigned destNode;

    explicit NRCopyFragsCompleted(const ndb_logevent & theEvent);

  };

  class NodeFailCompleted : public BaseEvent { 
  public:
    unsigned block; /* 0 = all */
    unsigned failedNode;
    unsigned completingNode; /* 0 = all */

    explicit NodeFailCompleted(const ndb_logevent & theEvent);

  };

  class NodeFailReported : public BaseEvent { 
  public:
    unsigned failedNode;
    unsigned failureState;

    explicit NodeFailReported(const ndb_logevent & theEvent);

  };

  class ArbitState : public BaseEvent { 
  public:

    unsigned code;                /* const code & state & << 16 ; */
    unsigned arbitNode;
    unsigned ticket0;
    unsigned ticket1;

    explicit ArbitState(const ndb_logevent & theEvent);
    
  };

  class ArbitResult : public BaseEvent { 
  public:
    unsigned code;                /* code & state << 16 */
    unsigned arbitNode;
    unsigned ticket0;
    unsigned ticket1;

    explicit ArbitResult(const ndb_logevent & theEvent);

  };

  class GCPTakeoverStarted : public BaseEvent { 
  public:

    explicit GCPTakeoverStarted(const ndb_logevent & theEvent);
  };

  class GCPTakeoverCompleted : public BaseEvent { 
  public:

    explicit GCPTakeoverCompleted(const ndb_logevent & theEvent);
  };

  class LCPTakeoverStarted : public BaseEvent { 
  public:

    explicit LCPTakeoverStarted(const ndb_logevent & theEvent);
  };

  class LCPTakeoverCompleted : public BaseEvent { 
  public:
    unsigned state;

    explicit LCPTakeoverCompleted(const ndb_logevent & theEvent);
  };

  class TransReportCounters : public BaseEvent { 
  public:
    unsigned transCount;
    unsigned commitCount;
    unsigned readCount;
    unsigned simpleReadCount;
    unsigned writeCount;
    unsigned attrinfoCount;
    unsigned concOpCount;
    unsigned abortCount;
    unsigned scanCount;
    unsigned rangeScanCount;
  
    explicit TransReportCounters(const ndb_logevent & theEvent);

  };

  class OperationReportCounters : public BaseEvent { 
  public:
    unsigned ops;

    explicit OperationReportCounters(const ndb_logevent & theEvent);

  };

  class TableCreated : public BaseEvent { 
  public:
    unsigned tableId;

    explicit TableCreated(const ndb_logevent & theEvent);
  };

  class JobStatistic : public BaseEvent { 
  public:
    unsigned meanLoopCount;

    explicit JobStatistic(const ndb_logevent & theEvent);
  };

  class SendBytesStatistic : public BaseEvent { 
  public:
    unsigned toNode;
    unsigned meanSentBytes;

    explicit SendBytesStatistic(const ndb_logevent & theEvent);
  };

  class ReceiveBytesStatistic : public BaseEvent { 
  public:
    unsigned fromNode;
    unsigned meanReceivedBytes;

    explicit ReceiveBytesStatistic(const ndb_logevent & theEvent);
  };

  class MemoryUsage : public BaseEvent { 
  public:
    int      gth;
    unsigned pageSizeKb;
    unsigned pagesUsed;
    unsigned pagesTotal;
    unsigned block;

    explicit MemoryUsage(const ndb_logevent & theEvent);
  };

  class TransporterError : public BaseEvent { 
  public:
    unsigned toNode;
    unsigned code;

    explicit TransporterError(const ndb_logevent & theEvent);
  };

  class TransporterWarning : public BaseEvent { 
  public:
    unsigned toNode;
    unsigned code;

    explicit TransporterWarning(const ndb_logevent & theEvent);
  };

  class MissedHeartbeat : public BaseEvent { 
  public:
    unsigned node;
    unsigned count;

    explicit MissedHeartbeat(const ndb_logevent & theEvent);
  };

  class DeadDueToHeartbeat : public BaseEvent { 
  public:
    unsigned node;

    explicit DeadDueToHeartbeat(const ndb_logevent & theEvent);
  };

  class Warning : public BaseEvent { 
  public:

    explicit Warning(const ndb_logevent & theEvent);
  };

  class SentHeartbeat : public BaseEvent { 
  public:
    unsigned node;

    explicit SentHeartbeat(const ndb_logevent & theEvent);
  };

  class CreateLogBytes : public BaseEvent { 
  public:
    unsigned node;

    explicit CreateLogBytes(const ndb_logevent & theEvent);
  };

  class InfoEvent : public BaseEvent { 
  public:

    explicit InfoEvent(const ndb_logevent & theEvent);
  };

  class EventBufferStatus : public BaseEvent { 
  public:
    unsigned usage;
    unsigned alloc;
    unsigned max;
    unsigned applyGciL;
    unsigned applyGciH;
    unsigned latestGciL;
    unsigned latestGciH;

    explicit EventBufferStatus(const ndb_logevent & theEvent);
  };


  class BackupStarted : public BaseEvent { 
  public:
    unsigned startingNode;
    unsigned backupId;

    explicit BackupStarted(const ndb_logevent & theEvent);
  };

  class BackupFailedToStart : public BaseEvent { 
  public:
    unsigned startingNode;
    unsigned error;

    explicit BackupFailedToStart(const ndb_logevent & theEvent);
  };

  class BackupCompleted : public BaseEvent { 
  public:
    unsigned startingNode;
    unsigned backupId; 
    unsigned startGci;
    unsigned stopGci;
    unsigned numRecords; 
    unsigned numLogRecords;
    unsigned numBytes;
    unsigned numLogBytes;

    explicit BackupCompleted(const ndb_logevent & theEvent);
  };

  class BackupAborted : public BaseEvent { 
  public:
    unsigned startingNode;
    unsigned backupId;
    unsigned error;

    explicit BackupAborted(const ndb_logevent & theEvent);
  };

  class SingleUser : public BaseEvent { 
  public: 
    unsigned eventType;
    unsigned nodeId;

    explicit SingleUser(const ndb_logevent & theEvent);
  };

  class StartReport : public BaseEvent { 
  public:
    unsigned reportType;
    unsigned remainingTime;
    unsigned bitmaskSize;
    //unsigned bitmaskData[1];

    explicit StartReport(const ndb_logevent & theEvent);
  };


#endif
