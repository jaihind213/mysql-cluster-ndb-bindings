#include <stdio.h>
#include <ndbapi/NdbApi.hpp>

int main() { 

  ndb_init();

  Ndb_cluster_connection * conn = new Ndb_cluster_connection("127.0.0.1");

  conn->connect(5,3,1);

  conn->wait_until_ready(5,5);

  delete conn;

  return 0;
}
