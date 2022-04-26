package ch.cern.todo.controller;

import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ToDoControllerAdvice {

  @ExceptionHandler(BadInputException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleBadInputError(BadInputException e) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setCode("001");
    errorResponse.setMessage(e.getMessage());
    errorResponse.setTimestamp(ZonedDateTime.now());

    return errorResponse;
  }
}
