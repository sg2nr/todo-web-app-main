package ch.cern.todo.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "TASK_CATEGORIES")
@Getter
@Setter
public class TaskCategory {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private long id;

  @Column(name = "name", nullable = false, unique=true)
  private String name;

  @Column(name = "description")
  private String description;
}
