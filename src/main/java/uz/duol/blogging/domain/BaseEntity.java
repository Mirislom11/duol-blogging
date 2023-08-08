package uz.duol.blogging.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Column(name = "last_update_date")
  private LocalDateTime lastUpdatedDate;
}
