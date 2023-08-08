package uz.duol.blogging.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.duol.blogging.domain.enumeration.Category;
import uz.duol.blogging.dto.ThemeDTO;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "theme")
public class ThemeEntity extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private Category category;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "theme")
  private List<BlogEntity> blogs;

  public ThemeDTO map2DTO () {
    return ThemeDTO.builder()
            .id(this.getId())
            .name(this.getName())
            .category(this.getCategory())
            .build();
  }
}
