package exceptions;
public class RideAlreadyExistException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public RideAlreadyExistException()
  {
    super();
  }
  
  public RideAlreadyExistException(String s)
  {
    super(s);
  }
}