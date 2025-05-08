package exceptions;
public class UserDoesntExistException extends Exception {
 private static final long serialVersionUID = 1L;

 public UserDoesntExistException()
  {
    super();
  }
  
  public UserDoesntExistException(String s)
  {
    super(s);
  }
}