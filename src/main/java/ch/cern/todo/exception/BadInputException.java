package ch.cern.todo.exception;

public class BadInputException extends RuntimeException {

  public BadInputException(String message) {
    super(message);
  }
}
