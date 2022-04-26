package ch.cern.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Task {

  @JsonProperty(required = true)
  private String name;

  private String description;

  @JsonProperty(required = true)
  private ZonedDateTime deadline;

  @JsonProperty(required = true)
  private String category;
}
