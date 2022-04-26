package ch.cern.todo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ErrorResponse {

  private String error;

  private String details;

  private ZonedDateTime timestamp;
}
