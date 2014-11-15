# alchemy.py
# Copyright (C) 2007 MySQL, Inc. 
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or 
# (at your option) any later version.
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


import sys, StringIO, string, types, re, datetime

from sqlalchemy import sql,engine,schema,ansisql, util, logging
from sqlalchemy.engine import default,strategies,base

from sqlalchemy.databases import mysql as mysqldb
import sqlalchemy.types as sqltypes
import sqlalchemy.exceptions as exceptions
from array import array

#try:
    # namespace issues. I'll figure them out later
from mysql.cluster import ndbapi
    #import ndb.constants.CLIENT as CLIENT_FLAGS
#except:
    #mysql = None
    #CLIENT_FLAGS = None

def kw_colspec(self, spec):
    if self.unsigned:
        spec += ' UNSIGNED'
    if self.zerofill:
        spec += ' ZEROFILL'
    return spec
        

def descriptor():
    return {'name':'ndbapi',
    'description':'NdbApi',
    'arguments':[
        ('database',"Database Name",None),
        ('host',"Hostname", None),
        ('mysql_username',"SQL Node Username",None),
        ('port',"Port", 631),
        ('mysql_host',"SQL Node Hostname",None),
        ('mysql_password',"SQL Node Password",None),
    ]}




class NdbTransaction(base.Transaction):
    """Represent a Transaction in progress.

    The Transaction object is **not** threadsafe.
    """

    def __init__(self, connection, parent, ndb_transaction):
        self.logger = logging.instance_logger(self)
        self.logger.debug("Create NdbTransaction")
        self.__connection = connection
        self.__parent = parent or self
        self.__is_active = True
        self.__ndb_transaction=ndb_transaction
        self.logger.debug(self.__ndb_transaction)

    connection = property(lambda s:s.__connection, doc="The Connection object referenced by this Transaction")
    is_active = property(lambda s:s.__is_active)
    ndb_transaction= property(lambda s:s.__ndb_transaction)
    
    def rollback(self):
        self.logger.debug("rollback")
        if not self.__parent.__is_active:
            return
        #if self.__parent is self:
        #    # <FIXME>
        #    #self.__transaction.execute(ndbapi.Rollback)
        #    #self.__connection._rollback_impl()
        #    self.__is_active = False
        #else:
        #    self.__parent.rollback()
        self.logger.info("execute(ndbapi.Rollback)")
        self.__ndb_transaction.execute(ndbapi.Rollback)

    def commit(self):
        self.logger.debug("commit")
        if not self.__parent.__is_active:
            raise exceptions.InvalidRequestError("This transaction is inactive")
        #if self.__parent is self
        #    self.__connection._commit_impl()
        #    self.__is_active = False
        self.logger.info("execute(ndbapi.Commit)")
        self.__ndb_transaction.execute(ndbapi.Commit)


class NdbConnection(base.Connection):

    def __init__(self, engine, connection=None, **kwargs):
        self.logger = logging.instance_logger(self)
        #super(NdbConnection,self).__init__(engine,connection)
        self.__engine=engine
        self.__connection=connection
        self.__transaction=None
        self.logger.debug("Initiated an NdbConnection object")
        self.logger.debug(self)
        self.__ndb=None

    engine = property(lambda s:s.__engine, doc="The Engine with which this Connection is associated (read only)")
    dialect = property(lambda s:s.__engine.dialect)
    transaction = property(lambda s:s.__transaction)

    def getAutoIncrementValue(self,table,batch=0):
        self.logger.debug("getAutoIncrementValue %s %s" % (table,batch))
        return self.__connection.getAutoIncrementValue(table,batch)

    def close_open_cursors(self):
        self.logger.debug("close_open_cursors")
    
    is_valid=True
    def cursor(self):
        self.logger.debug("cursor")
        self.begin()
        return NdbCursor(self.transaction)


    def begin(self):
        if self.__transaction is None:
            self.__transaction = self._create_transaction(None)
            return self.__transaction
        else:
            return self._create_transaction(self.__transaction)


    def _create_transaction(self,parent):
        self.logger.debug("_create_transaction")
        self.logger.info("startTransaction()")
        ndb_transaction=self.__connection.startTransaction()
        self.logger.debug(ndb_transaction)
        return NdbTransaction(self,parent,ndb_transaction)
    
    def create(self, entity, **kwargs):
        print "In create"
        print entity, kwargs
        
    def drop(self, entity, **kwargs):
        print "In drop"
        print entity, kwargs



    def execute(self,object, *args, **kwargs):
        self.logger.debug("in execute")
        self.logger.debug(self.transaction)
        for c in type(object).__mro__:
            if c in NdbConnection.executors:
                return NdbConnection.executors[c](self, object, *args, **kwargs)


    def execute_function(self,statement,*args, **kwargs):
        self.logger.debug("execute_function")

    def execute_clauseelement(self, elem, *multiparams, **params):
        self.logger.debug("execute_clauseelement")
        executemany = len(multiparams) > 0
        if executemany:
            param = multiparams[0]
        else:
            param = params
        return self.execute_compiled(elem.compile(dialect=self.dialect, parameters=param), *multiparams, **params)

    def execute_compiled(self, compiled, *multiparams, **params):
        """Execute a sql.Compiled object."""
        self.logger.debug("execute_compiled")
        self.logger.debug(multiparams)
        self.logger.debug(params)
        self.logger.debug(compiled)
        return compiled
##         if not compiled.can_execute:
##             raise exceptions.ArgumentError("Not an executeable clause: %s" % (str(compiled)))
##         parameters = [compiled.construct_params(m) for m in self._params_to_listofdicts(*multiparams, **params)]
##         if len(parameters) == 1:
##             parameters = parameters[0]
##         context = self.create_execution_context(compiled=compiled, compiled_parameters=parameters)
##         context.pre_exec()
##         self._execute_raw(context)
##         context.post_exec()
##         return context.get_result_proxy()

    def execute_default(self,statement,*args, **kwargs):
        self.logger.debug("execute_text")
    def execute_text(self,statement,*args, **kwargs):
        self.logger.debug("execute_text")
    executors = {
        sql._Function : base.Connection.execute_function,
        sql.ClauseElement : execute_clauseelement,
        sql.ClauseVisitor : execute_compiled,
        schema.SchemaItem: base.Connection.execute_default,
        str.__mro__[-2] : base.Connection.execute_text
    }

    def create_execution_context(self,compiled=None,compiled_parameters=None):
        return NdbExecutionContext(self,compiled,compiled_parameters)

    def _get_engine(self):
        self.logger.debug("in _get_engine")
        return self

    def contextual_connect(self):
        self.logger.debug("in contextual_connect")
        return self

    engine=property(_get_engine)

class NdbDialect(engine.base.Dialect):

    def __init__(self, cache_identifiers = None, module = ndbapi, mysql_dialect = None, **kwargs):

        self.logger = logging.instance_logger(self)
        self.logger.debug("new NdbDialect()")
        self.logger.debug(kwargs)
        self.logger.debug(self)

        self.module = module

        #super(NdbDialect,self).__init__(**kwargs)
        self.identifier_preparer = self.preparer()
        self.cache_identifiers = cache_identifiers
        self.mysql_dialect = mysql_dialect
        self.ndb_connection = None
        self.logger = logging.instance_logger(self)
        self.__connection=None
        self.__engine=None

    paramstyle=""


    def _get_connection(self):
        self.logger.debug("_get_connection")
        return self.__connection
    connection=property(_get_connection)
    
    def _get_engine(self):
        self.logger.debug("_get_engine")
        return self.__engine

    def _set_engine(self,value):
        self.logger.debug("set engine")
        self.__engine=value

    engine=property(_get_engine,_set_engine)

    def drop(self, entity, **kwargs):

        print "dropping %s" % entity

    def create_cursor(self, connection):
        print "creating cursor", connection
        return connection.cursor()

    def _positional(self):
        return 0
    positional=property(_positional)
    def create_connect_args(self, url):
        opts = url.translate_connect_args(
            ['host', 'database', 'user','port'])
##              'no_retries','retry_delay_in_seconds',
##              'verbose','timeout_for_first_alive',
##              'timeout_after_first_alive'])
        opts.update(url.query)

        return [[], opts]

    def convert_compiled_params(self, parameters):
        print "ccp:",self, parameters
        sys.exit(0)
        # FIXME: This is a hack
        return []

    def _begin_impl(self):
        self.logger.info("BEGIN")
        self.do_begin(self.connection)

    def do_begin(self,connection, **kwargs):
        connection.begin()


    def type_descriptor(self, typeobj):
        return sqltypes.adapt_type(typeobj, colspecs)

    def supports_sane_rowcount(self):
        return True

    def compiler(self, statement, bindparams, **kwargs):
        return NdbCompiler(self, statement, bindparams, engine=self.engine, **kwargs)

    def schemagenerator(self, *args, **kwargs):
        return MySQLSchemaGenerator(*args, **kwargs)

    def schemadropper(self, *args, **kwargs):
        return MySQLSchemaDropper(*args, **kwargs)

    def preparer(self):
        return MySQLIdentifierPreparer(self)

    def do_executemany(self, cursor, statement, parameters, context=None, **kwargs):
        try:
            rowcount = cursor.executemany(statement, parameters)
            if context is not None:
                context._rowcount = rowcount
        except mysqldb.OperationalError, o:
            if o.args[0] == 2006 or o.args[0] == 2014:
                cursor.invalidate()
            raise o
    def do_execute(self, cursor, statement, parameters, **kwargs):
        print "do_execute: ",cursor, statement, parameters
        #for stmt in statement.values():
        #    stmt.execute(ndbapi.NoCommit)
        print "after execute"

    def create_result_proxy_args(self, cursor, *args):
        print "crpa: " , cursor, args
        return {}


    def get_engine(self):
        self.logger.debug("get_engine")
        return self.engine
    
    def get_default_schema_name(self):
        if not hasattr(self, '_default_schema_name'):
            self._default_schema_name = text("select database()", self).scalar()
        return self._default_schema_name


    def dbapi(self):
        print "getting dbapi!"
        if self.ndb_connection is None:
            self.ndb_connection = NdbConnection(mysql_dialect=self.mysql_dialect)
        return self.ndb_connection

    def has_table(self, connection, table_name, schema=None):
        cursor = connection.execute("show table status like '" + table_name + "'")
        return bool( not not cursor.rowcount )



class NdbCompiler(ansisql.ANSICompiler):



    def __init__(self, *args, **kwargs):
        self.strings={}
        self.typemap={}
        self.columns={}
        self.inserted_ids=[]
        self.logger = logging.instance_logger(self)
        self.logger.debug(args)
        self.logger.debug(kwargs)
        ansisql.ANSICompiler.__init__(self,*args,**kwargs)


    def last_inserted_ids(self):
        return self.inserted_ids
    def last_inserted_params(self):
        return {}
    def lastrow_has_defaults(self):
        return False
    
    def compile(self):
        self.statement.accept_visitor(self)
        self.after_compile()

    def after_compile(self):
        # this re will search for params like :param
        # it has a negative lookbehind for an extra ':' so that it doesnt match
        # postgres '::text' tokens
        pass
    
    def get_from_text(self, obj):
        return self.froms.get(obj, None)

    def get_final_qyery(self,obj):
        return self.strings[obj]
    
    def get_str(self, obj):
        print "get_str: requested %s" % obj
        return "NdbTransaction: %s" % self.strings[obj]

    def get_whereclause(self, obj):
        return self.wheres.get(obj, None)

    def get_params(self, **params):
        """returns a structure of bind parameters for this compiled object.
        This includes bind parameters that might be compiled in via the "values"
        argument of an Insert or Update statement object, and also the given **params.
        The keys inside of **params can be any key that matches the BindParameterClause
        objects compiled within this object.  The output is dependent on the paramstyle
        of the DBAPI being used; if a named style, the return result will be a dictionary
        with keynames matching the compiled statement.  If a positional style, the output
        will be a list, with an iterator that will return parameter 
        values in an order corresponding to the bind positions in the compiled statement.
        
        for an executemany style of call, this method should be called for each element
        in the list of parameter groups that will ultimately be executed.
        """
        pass
    
    def default_from(self):
        """called when a SELECT statement has no froms, and no FROM clause is to be appended.  
        gives Oracle a chance to tack on a "FROM DUAL" to the string output. """
        return ""

    def visit_label(self, label):
        if len(self.select_stack):
            self.typemap.setdefault(label.name.lower(), label.obj.type)
        self.strings[label] = self.strings[label.obj] + " AS "  + self.preparer.format_label(label)
        
    def visit_column(self, column):

        if len(self.select_stack):
            # if we are within a visit to a Select, set up the "typemap"
            # for this column which is used to translate result set values
            self.typemap.setdefault(column.name.lower(), column.type)
            self.columns.setdefault(column.key, column)
        if column.table is None or not column.table.named_with_column():
            self.strings[column] = self.preparer.format_column(column)
        else:
            if column.table.oid_column is column:
                n = self.dialect.oid_column_name(column)
                if n is not None:
                    self.strings[column] = "%s.%s" % (self.preparer.format_table(column.table, use_schema=False), n)
                elif len(column.table.primary_key) != 0:
                    self.strings[column] = self.preparer.format_column_with_table(list(column.table.primary_key)[0])
                else:
                    self.strings[column] = None
            else:
                self.strings[column] = self.preparer.format_column_with_table(column)

    def visit_fromclause(self, fromclause):
        self.froms[fromclause] = fromclause.name

    def visit_index(self, index):
        self.strings[index] = index.name
    
    def visit_typeclause(self, typeclause):
        self.strings[typeclause] = typeclause.type.dialect_impl(self.dialect).get_col_spec()
            
    def visit_textclause(self, textclause):
        if textclause.parens and len(textclause.text):
            self.strings[textclause] = "(" + textclause.text + ")"
        else:
            self.strings[textclause] = textclause.text
        self.froms[textclause] = textclause.text
        if textclause.typemap is not None:
            self.typemap.update(textclause.typemap)
        
    def visit_null(self, null):
        self.strings[null] = 'NULL'
       
    def visit_compound(self, compound):
        if compound.operator is None:
            sep = " "
        else:
            sep = " " + compound.operator + " "
        
        s = string.join([self.get_str(c) for c in compound.clauses], sep)
        if compound.parens:
            self.strings[compound] = "(" + s + ")"
        else:
            self.strings[compound] = s
        
    def visit_clauselist(self, list):
        if list.parens:
            self.strings[list] = "(" + string.join([s for s in [self.get_str(c) for c in list.clauses] if s is not None], ', ') + ")"
        else:
            self.strings[list] = string.join([s for s in [self.get_str(c) for c in list.clauses] if s is not None], ', ')

    def apply_function_parens(self, func):
        return func.name.upper() not in ANSI_FUNCS or len(func.clauses) > 0

    def visit_calculatedclause(self, list):
        if list.parens:
            self.strings[list] = "(" + string.join([self.get_str(c) for c in list.clauses], ' ') + ")"
        else:
            self.strings[list] = string.join([self.get_str(c) for c in list.clauses], ' ')
      
    def visit_cast(self, cast):
        if len(self.select_stack):
            # not sure if we want to set the typemap here...
            self.typemap.setdefault("CAST", cast.type)
        self.strings[cast] = "CAST(%s AS %s)" % (self.strings[cast.clause],self.strings[cast.typeclause])
         
    def visit_function(self, func):
        if len(self.select_stack):
            self.typemap.setdefault(func.name, func.type)
        if not self.apply_function_parens(func):
            self.strings[func] = ".".join(func.packagenames + [func.name])
            self.froms[func] = self.strings[func]
        else:
            self.strings[func] = ".".join(func.packagenames + [func.name]) + "(" + string.join([self.get_str(c) for c in func.clauses], ', ') + ")"
            self.froms[func] = self.strings[func]
        
    def visit_compound_select(self, cs):
        text = string.join([self.get_str(c) for c in cs.selects], " " + cs.keyword + " ")
        group_by = self.get_str(cs.group_by_clause)
        if group_by:
            text += " GROUP BY " + group_by
        order_by = self.get_str(cs.order_by_clause)
        if order_by:
            text += " ORDER BY " + order_by
        text += self.visit_select_postclauses(cs)
        if cs.parens:
            self.strings[cs] = "(" + text + ")"
        else:
            self.strings[cs] = text
        self.froms[cs] = "(" + text + ")"

    def visit_binary(self, binary):
        result = self.get_str(binary.left)
        if binary.operator is not None:
            result += " " + self.binary_operator_string(binary)
        result += " " + self.get_str(binary.right)
        if binary.parens:
            result = "(" + result + ")"
        self.strings[binary] = result

    def binary_operator_string(self, binary):
        return binary.operator

    def visit_bindparam(self, bindparam):
        if bindparam.shortname != bindparam.key:
            self.binds.setdefault(bindparam.shortname, bindparam)
        count = 1
        key = bindparam.key

        # redefine the generated name of the bind param in the case
        # that we have multiple conflicting bind parameters.
        while self.binds.setdefault(key, bindparam) is not bindparam:
            # ensure the name doesn't expand the length of the string
            # in case we're at the edge of max identifier length
            tag = "_%d" % count
            key = bindparam.key[0 : len(bindparam.key) - len(tag)] + tag
            count += 1
        bindparam.key = key
        self.strings[bindparam] = self.bindparam_string(key)

    def bindparam_string(self, name):
        return self.bindtemplate % name
        
    def visit_alias(self, alias):
        self.froms[alias] = self.get_from_text(alias.original) + " AS " + self.preparer.format_alias(alias)
        self.strings[alias] = self.get_str(alias.original)

    def visit_select(self, select):
        
        # the actual list of columns to print in the SELECT column list.
        inner_columns = util.OrderedDict()

        self.select_stack.append(select)
        for c in select._raw_columns:
            if isinstance(c, sql.Select) and c.is_scalar:
                c.accept_visitor(self)
                inner_columns[self.get_str(c)] = c
                continue
            try:
                s = c._selectable()
            except AttributeError:
                c.accept_visitor(self)
                inner_columns[self.get_str(c)] = c
                continue
            for co in s.columns:
                if select.use_labels:
                    l = co.label(co._label)
                    l.accept_visitor(self)
                    inner_columns[co._label] = l
                # TODO: figure this out, a ColumnClause with a select as a parent
                # is different from any other kind of parent
                elif select.is_subquery and isinstance(co, sql._ColumnClause) and co.table is not None and not isinstance(co.table, sql.Select):
                    # SQLite doesnt like selecting from a subquery where the column
                    # names look like table.colname, so add a label synonomous with
                    # the column name
                    l = co.label(co.name)
                    l.accept_visitor(self)
                    inner_columns[self.get_str(l.obj)] = l
                else:
                    co.accept_visitor(self)
                    inner_columns[self.get_str(co)] = co
        self.select_stack.pop(-1)
        
        collist = string.join([self.get_str(v) for v in inner_columns.values()], ', ')

        text = "SELECT "
        text += self.visit_select_precolumns(select)
        text += collist
        
        whereclause = select.whereclause
        
        froms = []
        for f in select.froms:

            if self.parameters is not None:
                # look at our own parameters, see if they
                # are all present in the form of BindParamClauses.  if
                # not, then append to the above whereclause column conditions
                # matching those keys
                for c in f.columns:
                    if sql.is_column(c) and self.parameters.has_key(c.key) and not self.binds.has_key(c.key):
                        value = self.parameters[c.key]
                    else:
                        continue
                    clause = c==value
                    clause.accept_visitor(self)
                    whereclause = sql.and_(clause, whereclause)
                    self.visit_compound(whereclause)

            # special thingy used by oracle to redefine a join
            w = self.get_whereclause(f)
            if w is not None:
                # TODO: move this more into the oracle module
                whereclause = sql.and_(w, whereclause)
                self.visit_compound(whereclause)
                
            t = self.get_from_text(f)
            if t is not None:
                froms.append(t)
        
        if len(froms):
            text += " \nFROM "
            text += string.join(froms, ', ')
        else:
            text += self.default_from()
            
        if whereclause is not None:
            t = self.get_str(whereclause)
            if t:
                text += " \nWHERE " + t

        group_by = self.get_str(select.group_by_clause)
        if group_by:
            text += " GROUP BY " + group_by

        if select.having is not None:
            t = self.get_str(select.having)
            if t:
                text += " \nHAVING " + t

        order_by = self.get_str(select.order_by_clause)
        if order_by:
            text += " ORDER BY " + order_by

        text += self.visit_select_postclauses(select)

        text += self.for_update_clause(select)

        if getattr(select, 'parens', False):
            self.strings[select] = "(" + text + ")"
        else:
            self.strings[select] = text
        self.froms[select] = "(" + text + ")"

    def visit_select_precolumns(self, select):
        """ called when building a SELECT statment, position is just before column list """
        return select.distinct and "DISTINCT " or ""

    def visit_select_postclauses(self, select):
        """ called when building a SELECT statement, position is after all other SELECT clauses. Most DB syntaxes put LIMIT/OFFSET here """
        return (select.limit or select.offset) and self.limit_clause(select) or ""

    def for_update_clause(self, select):
        if select.for_update:
            return " FOR UPDATE"
        else:
            return ""

    def limit_clause(self, select):
        text = ""
        if select.limit is not None:
            text +=  " \n LIMIT " + str(select.limit)
        if select.offset is not None:
            if select.limit is None:
                text += " \n LIMIT -1"
            text += " OFFSET " + str(select.offset)
        return text

    def visit_table(self, table):
        self.froms[table] = self.preparer.format_table(table)
        self.strings[table] = ""

    def visit_join(self, join):
        righttext = self.get_from_text(join.right)
        if join.right._group_parenthesized():
            righttext = "(" + righttext + ")"
        if join.isouter:
            self.froms[join] = (self.get_from_text(join.left) + " LEFT OUTER JOIN " + righttext + 
            " ON " + self.get_str(join.onclause))
        else:
            self.froms[join] = (self.get_from_text(join.left) + " JOIN " + righttext +
            " ON " + self.get_str(join.onclause))
        self.strings[join] = self.froms[join]

    def visit_insert_column_default(self, column, default, parameters):
        """called when visiting an Insert statement, for each column in the table that
        contains a ColumnDefault object.  adds a blank 'placeholder' parameter so the 
        Insert gets compiled with this column's name in its column and VALUES clauses."""
        parameters.setdefault(column.key, None)

    def visit_update_column_default(self, column, default, parameters):
        """called when visiting an Update statement, for each column in the table that
        contains a ColumnDefault object as an onupdate. adds a blank 'placeholder' parameter so the 
        Update gets compiled with this column's name as one of its SET clauses."""
        parameters.setdefault(column.key, None)
        
    def visit_insert_sequence(self, column, sequence, parameters):
        """called when visiting an Insert statement, for each column in the table that
        contains a Sequence object.  Overridden by compilers that support sequences to place
        a blank 'placeholder' parameter, so the Insert gets compiled with this column's
        name in its column and VALUES clauses."""
        pass
    
    def visit_insert_column(self, column, parameters):
        """called when visiting an Insert statement, for each column in the table
        that is a NULL insert into the table.  Overridden by compilers who disallow
        NULL columns being set in an Insert where there is a default value on the column
        (i.e. postgres), to remove the column from the parameter list."""
        pass
        
    def visit_insert(self, insert_stmt):
        # scan the table's columns for defaults that have to be pre-set for an INSERT
        # add these columns to the parameter list via visit_insert_XXX methods
        self.logger.debug("visit_insert")
        default_params = {}
        class DefaultVisitor(schema.SchemaVisitor):
            def visit_column(s, c):
                self.visit_insert_column(c, default_params)
            def visit_column_default(s, cd):
                self.visit_insert_column_default(c, cd, default_params)
            def visit_sequence(s, seq):
                self.visit_insert_sequence(c, seq, default_params)
        vis = DefaultVisitor()
        for c in insert_stmt.table.c:
            if (isinstance(c, schema.SchemaItem) and (self.parameters is None or self.parameters.get(c.key, None) is None)):
                vis.traverse(c)
        
        self.isinsert = True
        colparams = self._get_colparams(insert_stmt, default_params)

        self.inline_params = util.Set()
        def create_param(col, p):
            if isinstance(p, sql._BindParamClause):
                self.binds[p.key] = p
                if p.shortname is not None:
                    self.binds[p.shortname] = p
                return self.bindparam_string(p.key)
            else:
                self.inline_params.add(col)
                p.accept_visitor(self)
                if isinstance(p, sql.ClauseElement) and not isinstance(p, sql.ColumnElement):
                    return "(" + self.get_str(p) + ")"
                else:
                    return self.get_str(p)

        pk = insert_stmt.table.primary_key.keys()[0]
        table_name=str(insert_stmt.table)

        engine=self.dialect.get_engine()
        connection=engine.raw_connection()
        trans=connection.transaction
        ndb_trans=trans.ndb_transaction
        
        op = ndb_trans.getNdbOperation(table_name)
        self.logger.info("getNdbOperation(%s)" % table_name)

        op.insertTuple()
        self.logger.info("\tinsertTuple()")

        auto_id = connection.getAutoIncrementValue(table_name,10)
        self.logger.info("\tgetAutoIncrementValue(%s)" % table_name)
        self.inserted_ids.append(auto_id)
        op.equal(pk,auto_id)
        self.logger.info("\tequal(%s,%s)" % (pk,auto_id))
 
        for c in colparams:
            op.setValue(c[0].name, c[1].value)
            self.logger.info("\tsetValue(%s,%s)" % (c[0].name,c[1].value))
        self.strings[insert_stmt]=trans
        self.trans=trans


    def visit_update(self, update_stmt):
        # scan the table's columns for onupdates that have to be pre-set for an UPDATE
        # add these columns to the parameter list via visit_update_XXX methods
        default_params = {}
        class OnUpdateVisitor(schema.SchemaVisitor):
            def visit_column_onupdate(s, cd):
                self.visit_update_column_default(c, cd, default_params)
        vis = OnUpdateVisitor()
        for c in update_stmt.table.c:
            if (isinstance(c, schema.SchemaItem) and (self.parameters is None or self.parameters.get(c.key, None) is None)):
                c.accept_schema_visitor(vis)

        self.isupdate = True
        colparams = self._get_colparams(update_stmt, default_params)

        self.inline_params = util.Set()
        def create_param(col, p):
            if isinstance(p, sql._BindParamClause):
                self.binds[p.key] = p
                self.binds[p.shortname] = p
                return self.bindparam_string(p.key)
            else:
                p.accept_visitor(self)
                self.inline_params.add(col)
                if isinstance(p, sql.ClauseElement) and not isinstance(p, sql.ColumnElement):
                    return "(" + self.get_str(p) + ")"
                else:
                    return self.get_str(p)
                
        text = "UPDATE " + self.preparer.format_table(update_stmt.table) + " SET " + string.join(["%s=%s" % (self.preparer.format_column(c[0]), create_param(*c)) for c in colparams], ', ')
        
        if update_stmt.whereclause:
            text += " WHERE " + self.get_str(update_stmt.whereclause)
         
        self.strings[update_stmt] = text


    def _get_colparams(self, stmt, default_params):
        """organize UPDATE/INSERT SET/VALUES parameters into a list of tuples, 
        each tuple containing the Column and a ClauseElement representing the
        value to be set (usually a _BindParamClause, but could also be other
        SQL expressions.)

        the list of tuples will determine the columns that are actually rendered
        into the SET/VALUES clause of the rendered UPDATE/INSERT statement.  It will
        also determine how to generate the list/dictionary of bind parameters at 
        execution time (i.e. get_params()).
        
        this list takes into account the "values" keyword specified to the statement,
        the parameters sent to this Compiled instance, and the default bind parameter
        values corresponding to the dialect's behavior for otherwise unspecified 
        primary key columns.
        """
        # no parameters in the statement, no parameters in the 
        # compiled params - return binds for all columns
        if self.parameters is None and stmt.parameters is None:
            return [(c, sql.bindparam(c.key, type=c.type)) for c in stmt.table.columns]

        def to_col(key):
            if not isinstance(key, sql._ColumnClause):
                return stmt.table.columns.get(str(key), key)
            else:
                return key
                
        # if we have statement parameters - set defaults in the 
        # compiled params
        if self.parameters is None:
            parameters = {}
        else:
            parameters = dict([(to_col(k), v) for k, v in self.parameters.iteritems()])

        if stmt.parameters is not None:
            for k, v in stmt.parameters.iteritems():
                parameters.setdefault(to_col(k), v)

        for k, v in default_params.iteritems():
            parameters.setdefault(to_col(k), v)

        # create a list of column assignment clauses as tuples
        values = []
        for c in stmt.table.columns:
            if parameters.has_key(c):
                value = parameters[c]
                if sql._is_literal(value):
                    value = sql.bindparam(c.key, value, type=c.type)
                values.append((c, value))
        return values

    def visit_delete(self, delete_stmt):
        text = "DELETE FROM " + self.preparer.format_table(delete_stmt.table)
        
        if delete_stmt.whereclause:
            text += " WHERE " + self.get_str(delete_stmt.whereclause)
         
        self.strings[delete_stmt] = text
        
    def __str__(self):
        return "Compiled! %s " % (self.strings,)



    def visit_cast(self, cast):
        """hey ho MySQL supports almost no types at all for CAST"""
        if (isinstance(cast.type, sqltypes.Date) or isinstance(cast.type, sqltypes.Time) or isinstance(cast.type, sqltypes.DateTime)):
            return super(MySQLCompiler, self).visit_cast(cast)
        else:
            # so just skip the CAST altogether for now.
            # TODO: put whatever MySQL does for CAST here.
            self.strings[cast] = self.strings[cast.clause]

    def for_update_clause(self, select):
        if select.for_update == 'read':
             return ' LOCK IN SHARE MODE'
        else:
            return super(MySQLCompiler, self).for_update_clause(select)

    def limit_clause(self, select):
        text = ""
        if select.limit is not None:
            text +=  " \n LIMIT " + str(select.limit)
        if select.offset is not None:
            if select.limit is None:
                # striaght from the MySQL docs, I kid you not
                text += " \n LIMIT 18446744073709551615"
            text += " OFFSET " + str(select.offset)
        return text
        

dialect = NdbDialect

class MySQLSchemaGenerator(ansisql.ANSISchemaGenerator):
    def get_column_specification(self, column, override_pk=False, first_pk=False):
        t = column.type.engine_impl(self.engine)
        colspec = self.preparer.format_column(column) + " " + column.type.engine_impl(self.engine).get_col_spec()
        default = self.get_column_default_string(column)
        if default is not None:
            colspec += " DEFAULT " + default

        if not column.nullable:
            colspec += " NOT NULL"
        if column.primary_key:
            if len(column.foreign_keys)==0 and first_pk and column.autoincrement and isinstance(column.type, sqltypes.Integer):
                colspec += " AUTO_INCREMENT"
        return colspec

    def post_create_table(self, table):
        args = ""
        for k in table.kwargs:
            if k.startswith('mysql_'):
                opt = k[6:]
                args += " %s=%s" % (opt.upper(), table.kwargs[k])
        return args

class MySQLSchemaDropper(ansisql.ANSISchemaDropper):
    def visit_index(self, index):
        self.append("\nDROP INDEX " + index.name + " ON " + index.table.name)
        self.execute()
    def drop_foreignkey(self, constraint):
        self.append("ALTER TABLE %s DROP FOREIGN KEY %s" % (self.preparer.format_table(constraint.table), constraint.name))
        self.execute()

class MySQLIdentifierPreparer(ansisql.ANSIIdentifierPreparer):
    def __init__(self, dialect):
        super(MySQLIdentifierPreparer, self).__init__(dialect, initial_quote='`')
    def _escape_identifier(self, value):
        #TODO: determin MySQL's escaping rules
        return value
    def _fold_identifier_case(self, value):
        #TODO: determin MySQL's case folding rules
        return value

class NdbConnectionProvider(base.ConnectionProvider):

    def __init__(self,connectionstring,database):
        self.connectionstring=connectionstring
        self.database=database
        self.ndb_cluster_connection=None
        self.connection=None
        self.ndb=None
        self.logger = logging.instance_logger(self)

    def get_connection(self,engine):
        self.logger.debug("Connecting!")
        if self.ndb_cluster_connection is None:
            self.logger.debug("Connecting to %s "%self.connectionstring)
            self.ndb_cluster_connection=ndbapi.connect(self.connectionstring)
        if self.ndb is None:
            self.logger.debug("getting database object")
            self.ndb=self.ndb_cluster_connection.getNdb(self.database)
            self.ndb.init(4)
        if self.connection is None:
            self.connection=NdbConnection(engine,self.ndb)
        return self.connection
            
            

class NdbEngine(base.Engine):

    def __init__(self, connection_provider, dialect, echo=None,**kwargs):
        self._dialect=dialect
        self._dialect.engine=self
        self.connection_provider=connection_provider
        self.echo = echo
        self.logger = logging.instance_logger(self)

    def connect(self, **kwargs):
        connection=NdbConnection(self,**kwargs)
        self.logger.debug(connection)
        self.logger.debug(self.dialect)
        self.dialect.__connection=connection
        self.dialect.__engine=self
        return connection

    def raw_connection(self):
        """Return a DBAPI connection."""

        return self.connection_provider.get_connection(self)



    def contextual_connect(self):
        self.logger.debug("in contextual_connect")
        return self.raw_connection()



class NdbEngineStrategy(strategies.EngineStrategy):
    def __init__(self):
        self.logger = logging.instance_logger(self)
        strategies.EngineStrategy.__init__(self, 'ndbapi')
        
    def create(self, name_or_url, *args, **kwargs):
        self.logger.debug("create")
        self.logger.debug(args)
        self.logger.debug(kwargs)
        
        u = engine.url.make_url(name_or_url)
        mydialect_cls = u.get_dialect()
        self.logger.debug(u)
        if "connectstring" in kwargs:
             connectstring=kwargs.pop("connectstring")
        mydialect=mydialect_cls(*args,**kwargs)

        dialect=NdbDialect(mysql_dialect=mydialect,*args,**kwargs)
        (cargs, cparams) = dialect.create_connect_args(u)

        self.logger.debug(cargs)
        self.logger.debug(cparams)
        cp=NdbConnectionProvider(connectstring,database=cparams['database'])
        return NdbEngine(cp,dialect)

# register
NdbEngineStrategy() 
