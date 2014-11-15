
namespace MySql.Cluster.NdbApi {

using System; 

  public class NdbApiException : System.ApplicationException {
    public NdbApiException(string message) 
      : base(message) {
    }
  }

  public class NdbApiPermanentException : NdbApiException {
      public NdbApiPermanentException(string message)
      : base(message) {
      }
  }
  public class NdbApiTemporaryException : NdbApiException {
      public NdbApiTemporaryException(string message)
      : base(message) {
      }
  }
  public class NdbApiUnknownResult : NdbApiException {
      public NdbApiUnknownResult(string message)
      : base(message) {
      }
  }
  public class ApplicationError : NdbApiPermanentException {
      public ApplicationError(string message)
      : base(message) {
      }
  }
  public class NoDataFound : NdbApiPermanentException {
      public NoDataFound(string message)
      : base(message) {
      }
  }
  public class ConstraintViolation : NdbApiPermanentException {
      public ConstraintViolation(string message)
      : base(message) {
      }
  }
  public class SchemaError : NdbApiPermanentException {
      public SchemaError(string message)
      : base(message) {
      }
  }
  public class UserDefinedError : NdbApiPermanentException {
      public UserDefinedError(string message)
      : base(message) {
      }
  }
  public class InsufficientSpace : NdbApiTemporaryException {
      public InsufficientSpace(string message)
      : base(message) {
      }
  }
  public class TemporaryResourceError : NdbApiTemporaryException {
      public TemporaryResourceError(string message)
      : base(message) {
      }
  }
  public class NodeRecoveryError : NdbApiTemporaryException {
      public NodeRecoveryError(string message)
      : base(message) {
      }
  }
  public class OverloadError : NdbApiTemporaryException {
      public OverloadError(string message)
      : base(message) {
      }
  }
  public class TimeoutExpired : NdbApiTemporaryException {
      public TimeoutExpired(string message)
      : base(message) {
      }
  }
  public class UnknownResultError : NdbApiUnknownResult {
      public UnknownResultError(string message)
      : base(message) {
      }
  }
  public class InternalError : NdbApiPermanentException {
      public InternalError(string message)
      : base(message) {
      }
  }
  public class FunctionNotImplemented : NdbApiPermanentException {
      public FunctionNotImplemented(string message)
      : base(message) {
      }
  }
  public class UnknownErrorCode : NdbApiUnknownResult {
      public UnknownErrorCode(string message)
      : base(message) {
      }
  }
  public class NodeShutdown : NdbApiTemporaryException {
      public NodeShutdown(string message)
      : base(message) {
      }
  }
  public class SchemaObjectExists : NdbApiPermanentException {
      public SchemaObjectExists(string message)
      : base(message) {
      }
  }
  public class InternalTemporary : NdbApiTemporaryException {
      public InternalTemporary(string message)
      : base(message) {
      }
  }




}
