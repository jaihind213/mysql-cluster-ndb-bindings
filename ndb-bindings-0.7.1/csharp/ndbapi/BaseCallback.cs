using System;
using System.Runtime.InteropServices;

namespace MySql.Cluster.NdbApi {

    public delegate void AsynchCallbackDelegate(int result, IntPtr theTrans);

	public abstract class BaseCallback { 

	  public long currentTime {
	    get { 
	      return ndbapiPINVOKE.getMicroTime();
			}
	  }

	  public long elapsedTime { 
			get {
	    return this.currentTime - this.create_time;
			}
	  }

	  public long executeTime { 
			get {
	    return this.end_time - this.create_time;
			}
	  }

	  public long callbackTime {
	    get {
	      return this.end_time - this.start_time;
		}
	  } 
	  protected long create_time;
	  protected long start_time;
	  protected long end_time;
	  protected NdbTransaction theTrans = null;

	  internal AsynchCallbackDelegate theCallback = null; 
		
	  public BaseCallback() { 
	    create_time = 0;
	    start_time = 0;
	    end_time = 0;
	    theCallback = new AsynchCallbackDelegate(cli_call_callback);
	  }

	  internal AsynchCallbackDelegate registerTransactionHook(NdbTransaction trans) {
			theTrans=trans;
			return theCallback;
		}
		
	  protected void cli_call_callback(int result, IntPtr transPtr) {
	   
	    this.start_time = this.currentTime;
	    if (NdbTransaction.getCPtr(theTrans).Equals(transPtr)) {
	        this.callback(result);
	    } else {
	    	// Something is horrible wrong! The transaction we were passed is not the same as the 
	    	// transaction we are storing. TODO: How do we deal with this?
	    	this.callback(result);
	    }	
	    this.end_time = this.currentTime;
	  }
	  
	  public abstract void callback(int result);

	  public NdbError ndbError { 
			get {
		  return theTrans.getNdbError();
			}
	  }

	}

}
