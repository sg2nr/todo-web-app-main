package ch.cern.todo.controller;

import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.exception.OperationNotPossibleException;
import ch.cern.todo.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ToDoControllerAdvice {

  private static final String INVALID_INPUT_DATA = "INVALID INPUT DATA";
  private static final String OPERATION_NOT_POSSIBLE = "OPERATION NOT POSSIBLE";

  @ExceptionHandler(BadInputException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleBadInputError(BadInputException e) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setError(INVALID_INPUT_DATA);
    errorResponse.setDetails(e.getMessage());
    errorResponse.setTimestamp(ZonedDateTime.now());

    return errorResponse;
  }

  @ExceptionHandler(OperationNotPossibleException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleOperationNotPossibleError(OperationNotPossibleException e) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setError(OPERATION_NOT_POSSIBLE);
    errorResponse.setDetails(e.getMessage());
    errorResponse.setTimestamp(ZonedDateTime.now());

    return errorResponse;
  }
}
