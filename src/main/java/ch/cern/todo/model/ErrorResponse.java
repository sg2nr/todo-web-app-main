package ch.cern.todo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ErrorResponse {

  private String code;

  private String message;

  private ZonedDateTime timestamp;
}
