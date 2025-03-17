package exceptions;
public class UserDoesntExistException extends Exception {
 private static final long serialVersionUID = 1L;

 public UserDoesntExistException()
  {
    super();
  }
  /**This exception is triggered if the question already exists
  *@param s String of the exception
  */
  public UserDoesntExistException(String s)
  {
    super(s);
  }
}