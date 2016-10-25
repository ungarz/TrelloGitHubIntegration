package trellogitintegration.exec;

public abstract class OperationResult {

  private boolean success;
  
  public OperationResult(boolean isSuccess) {
    this.success = isSuccess;
  }
  
  public boolean isSuccessful() {
    return this.success;
  }
  
  /**
   * @return the message explaining the result
   */
  public abstract String getMessage();
  
}
