package testsuite;

import com.mysql.cluster.ndbj.BaseCallback;
import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbError;
import com.mysql.cluster.ndbj.NdbResultSet;
import com.mysql.cluster.ndbj.NdbTransaction;
import com.mysql.cluster.ndbj.NdbTransactionImpl;

public class TestCallback extends BaseCallback {

	public static final int ERR_UNKOWN = 19999;

	int          myCallbackNum;
	Ndb          myNdb;
	NdbResultSet myResults;
	String		 myTablename = "";
	NdbTransaction myTrans;
	public NdbError error;
	public boolean throwOnError = true;
	public int result = ERR_UNKOWN;

	public TestCallback(String tablename, int theCallbackNum, Ndb theNdb, NdbTransaction theTrans, NdbResultSet theResults) {
		super(theTrans);
		if(theTrans == null)
			throw new RuntimeException("Transaction cannot be null");
		myCallbackNum = theCallbackNum;
		myNdb         = theNdb;
		myResults     = theResults;
		myTablename	  = tablename;
		myTrans       = theTrans;
	}

	public TestCallback(int theCallbackNum, Ndb theNdb, NdbTransaction theTrans, NdbResultSet theResults) {
		this("", theCallbackNum, theNdb, theTrans, theResults);
	}

	@Override
	public void callback(int result) {
		this.result = result;
		if(result != 0)
		{
			error = ((NdbTransactionImpl)myTrans).getNdbError();
			String errmsg = String.format("Result = %d in callback %d (code=%d): %s",
										  result, myCallbackNum,
										  error.getCode(), error.getMessage());
			if(throwOnError)
				throw new RuntimeException(errmsg);
		}
		// TODO: in finally?
		theTrans.close();
	}
}
