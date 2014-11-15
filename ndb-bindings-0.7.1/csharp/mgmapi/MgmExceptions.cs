
namespace MySql.Cluster.MgmApi {

using System; 

  public class NdbApiException : System.ApplicationException {
    public NdbApiException(string message) 
      : base(message) {
    }
  }

  public class BlobUndefinedException : NdbApiException { 
    public BlobUndefinedException(string message)
      : base(message) {
    }
  }


  public class NdbApiPermanentException : NdbApiException { 
    public NdbApiPermanentException(string message)
      : base(message) {
    }
  }


  public class NdbApiRuntimeException : NdbApiException { 
    public NdbApiRuntimeException(string message)
      : base(message) {
    }
  }


  public class NdbApiTemporaryException : NdbApiException { 
    public NdbApiTemporaryException(string message)
      : base(message) {
    }
  }


  public class NdbApiTimeStampOutOfBoundsException : NdbApiException { 
    public NdbApiTimeStampOutOfBoundsException(string message)
      : base(message) {
    }
  }


  public class NdbApiUserErrorPermanentException : NdbApiPermanentException { 
    public NdbApiUserErrorPermanentException(string message)
      : base(message) {
    }
  }


  public class NdbClusterConnectionPermanentException : NdbApiPermanentException { 
    public NdbClusterConnectionPermanentException(string message)
      : base(message) {
    }
  }


  public class NdbClusterConnectionTemporaryException : NdbApiTemporaryException { 
    public NdbClusterConnectionTemporaryException(string message)
      : base(message) {
    }
  }


  public class NoSuchColumnException : NdbApiException { 
    public NoSuchColumnException(string message)
      : base(message) {
    }
  }


  public class NoSuchIndexException : NdbApiException { 
    public NoSuchIndexException(string message)
      : base(message) {
    }
  }


  public class NoSuchTableException : NdbApiException { 
    public NoSuchTableException(string message)
      : base(message) {
    }
  }



}
