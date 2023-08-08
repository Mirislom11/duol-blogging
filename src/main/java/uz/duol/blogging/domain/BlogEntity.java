package uz.duol.blogging.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.duol.blogging.domain.enumeration.BlogStatus;
import uz.duol.blogging.dto.BlogDTO;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "blog")
public class BlogEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "theme_id")
  private ThemeEntity theme;

  @Column(name = "title")
  private String title;

  @Column(name = "text")
  private String text;

  @Column(name = "number_views")
  private Long numberViews;

  @Column(name = "verified")
  private Boolean verified;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", columnDefinition = "enum('ACTIVE','REMOVED')")
  private BlogStatus status;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST,mappedBy = "blog")
  private List<CommentEntity> comments;

  public BlogDTO map2DTO() {
    return BlogDTO.builder()
        .id(this.getId())
        .theme(this.getTheme().map2DTO())
        .numberOfViews(this.getNumberViews())
        .title(this.getTitle())
        .text(this.getText())
        .verified(this.getVerified())
        .build();
  }

  public BlogDTO.BlogWithCommentsDTO map2WithCommentDTO() {
    return BlogDTO.BlogWithCommentsDTO.builder()
        .id(this.getId())
        .theme(this.getTheme().map2DTO())
        .numberOfViews(this.getNumberViews())
        .title(this.getTitle())
        .text(this.getText())
        .verified(this.getVerified())
        .comments(
            this.getComments().stream().map(CommentEntity::map2DTO).collect(Collectors.toList()))
        .build();
  }

  public BlogDTO.BlogVerifiedDTO map2VerifiedDTO () {
    return BlogDTO.BlogVerifiedDTO.builder()
            .id(this.getId())
            .verified(this.getVerified())
            .build();
  }
}
