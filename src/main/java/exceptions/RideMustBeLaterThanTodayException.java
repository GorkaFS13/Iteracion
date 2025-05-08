package exceptions;
public class RideMustBeLaterThanTodayException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public RideMustBeLaterThanTodayException()
  {
    super();
  }
  
  public RideMustBeLaterThanTodayException(String s)
  {
    super(s);
  }
}