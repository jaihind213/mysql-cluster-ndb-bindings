"""a basic Adjacency List model tree."""

from sqlalchemy import *
from sqlalchemy.util import OrderedDict

import logging
logging.basicConfig()
import mysql.cluster.alchemy
#logging.getLogger('sqlalchmey.engine').setLevel(logging.INFO)
#logging.getLogger('sqlalchemy.orm.unitofwork').setLevel(logging.DEBUG)
#logging.getLogger('mysql.cluster.alchemy').setLevel(logging.DEBUG)
logging.getLogger('mysql.cluster.alchemy').setLevel(logging.INFO)


metadata = BoundMetaData('mysql://root@127.0.0.1/test', strategy="ndbapi",echo=True, connectstring="127.0.0.1")

# this is just for doc purposes. You have to create this yourself right now

schema="""CREATE TABLE `testproject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster DEFAULT CHARSET=latin1;
"""

projects = Table('testproject',metadata,
  Column('id',Integer, primary_key=True), 
  Column('name',String(32)))


class Project(object):
    """a rich Tree class which includes path-based operations"""
    pass

mapper(Project, projects)

print "\n\n\n----------------------------"
print "Creating Tree Table:"
print "----------------------------"

session=create_session()

project = Project()
project.name="bar"


session.save(project)
session.flush()

print project.id, project

#session.clear()

#foos = session.query(Project).select()
#for foo in foos:
##    print foo.id, foo.director
