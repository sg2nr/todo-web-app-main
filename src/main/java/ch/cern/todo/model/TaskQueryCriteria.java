package ch.cern.todo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class TaskQueryCriteria {

  private List<String> categories;

  private ZonedDateTime beforeDeadline;
}
