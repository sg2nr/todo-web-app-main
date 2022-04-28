package ch.cern.todo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Task {

  private String id;

  private String name;

  private String description;

  private ZonedDateTime deadline;

  private String category;
}
