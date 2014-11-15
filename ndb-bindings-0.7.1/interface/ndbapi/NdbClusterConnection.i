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

%newobject Ndb_cluster_connection::create;
%newobject Ndb_cluster_connection::realCreateNdb;
%delobject Ndb_cluster_connection::close;

class Ndb_cluster_connection {



public:

  /* NdbClusterConnection.create should be used instead in 
   * Java and C#. 
   */
  %ndbexception("NdbApiException") {
    $action
      if (result == NULL) {
        const char * msg = "Couldn't create NdbClusterConnection";
        NDB_exception(NdbApiException,msg);
      }
  }
  Ndb_cluster_connection(const char * connectstring = 0);

#if !defined(SWIG_RUBY_AUTORENAME)
  %rename("setName") set_name;
  %rename("waitUntilReady") wait_until_ready;
  %rename("setTimeout") set_timeout;
#endif

  %ndbnoexception;

  void set_name(const char* name);

  %ndbexception("NdbApiException") {
    $action
      if (result > 0) {
        const char * msg = "Setting timeout failed";
        NDB_exception(NdbApiException,msg);
      }
  }

  voidint set_timeout(int timeout_ms);


  %ndbexception("NdbApiException,NdbApiPermanentException,"
                "NdbApiTemporaryException") {
    $action
      if (result > 0) {
        const char * msg = "Connect to management server failed. "
          "No nodes are live.";
        NDB_exception(NdbApiPermanentException,msg);
      } else if (result < 0) {
        const char * msg = "Temporary Problem connecting. Some but not all "
          "nodes are live.";
        NDB_exception(NdbApiTemporaryException,msg);
      }
  }
  %typemap(check) int retry_delay_in_seconds {
    if ($1 < 0) {
      NDB_exception(NdbApiException,
                    "Delay must be >= 0.");
    }
  }
  int connect(int no_retries, int retry_delay_in_seconds=1,
              bool verbose=false);

  %ndbexception("NdbApiException") {
    $action
      if (result) {
        const char * msg = "Cluster was not ready";
        NDB_exception(NdbApiException,msg);
      }
  }
  %typemap(check) int timeoutForFirstAlive {
    if ($1 < 0) {
      NDB_exception(NdbApiException,
                    "Timeout must be >= 0.");
    }
  }
  %typemap(check) int timeoutAfterFirstAlive {
    if ($1 < 0) {
      NDB_exception(NdbApiException,
                    "Timeout must be >= 0.");
    }
  }
  voidint wait_until_ready(int timeoutForFirstAlive,
                       int timeoutAfterFirstAlive);

};

%extend Ndb_cluster_connection {

  %ndbexception("NdbApiPermanentException,"
                "NdbApiTemporaryException") {
    $action
      if (result > 0) {
        const char * msg = "Connect to management server failed";
        NDB_exception(NdbApiPermanentException,msg);
      } else if (result < 0) {
        const char * msg = "Temporary Problem connecting to management server";
        NDB_exception(NdbApiTemporaryException,msg);
      }
  }
  int connect() {
    return self->connect(0,1,false);
  }

  %ndbnoexception;

  void close() {
    if (self != NULL)
      delete self; 
  }

public:
  %ndbexception("NdbApiException") createNdb {
    $action
      if (result==NULL) {
        NDB_exception(NdbApiException,"Couldn't allocate an Ndb object");
      }
  }

  %typemap(check) int initThreads {
    if ($1 < 0) {
      NDB_exception(NdbApiException,
                    "initThreads must be >= 0.");
    }
  }

  Ndb* realCreateNdb(const char* aCatalogName, Int32 maxThreads = 4) {
    Ndb * theNdb = new Ndb(self,aCatalogName);
    if (theNdb!=NULL) {
      int ret = theNdb->init(maxThreads);
      if ( ret ) {
        delete theNdb;
        return NULL;
      }
    }
    return theNdb;
  }

  %ndbexception("NdbApiException") {
    $action
      if (result == NULL) {
        const char * msg = "Couldn't create NdbClusterConnection";
        NDB_exception(NdbApiException,msg);
      }
  }
  static Ndb_cluster_connection * create(const char * connectString=0)
  {
    return new Ndb_cluster_connection(connectString);
  }


  %ndbnoexception;

}
