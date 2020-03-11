package io.vividcode.happyride.dispatchservice;

public class DispatchVerificationException extends RuntimeException {

  public DispatchVerificationException() {
  }

  public DispatchVerificationException(String message) {
    super(message);
  }

  public DispatchVerificationException(String message, Throwable cause) {
    super(message, cause);
  }
}
