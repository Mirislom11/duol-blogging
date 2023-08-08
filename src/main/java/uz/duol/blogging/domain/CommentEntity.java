package uz.duol.blogging.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.duol.blogging.domain.enumeration.CommentStatus;
import uz.duol.blogging.dto.CommentDTO;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "blog_id")
  private BlogEntity blog;

  @Column(name = "content")
  private String content;

  @Column(name = "usability", nullable = false)
  private Integer usability;

  @Column(name = "not_usability", nullable = false)
  private Integer notUsability;

  @Column(name = "verified")
  private Boolean verified;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private CommentStatus status;

  public CommentDTO map2DTO() {
    return CommentDTO.builder()
        .id(this.getId())
        .notUsability(this.getNotUsability())
        .usability(this.getUsability())
        .verified(this.getVerified())
        .content(this.getContent())
        .build();
  }
}
