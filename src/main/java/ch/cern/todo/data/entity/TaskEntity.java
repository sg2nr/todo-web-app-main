package ch.cern.todo.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "TASKS")
@Getter
@Setter
public class TaskEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "deadline", nullable = false)
  private ZonedDateTime deadline;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private TaskCategoryEntity category;
}
