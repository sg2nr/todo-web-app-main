package ch.cern.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

  @JsonProperty(required = true)
  private String name;

  private String description;
}
