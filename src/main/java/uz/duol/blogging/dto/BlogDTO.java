package uz.duol.blogging.dto;

import lombok.*;
import uz.duol.blogging.domain.BlogEntity;
import uz.duol.blogging.domain.enumeration.BlogStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO {
  private Long id;
  private ThemeDTO theme;
  private String title;
  private String text;
  private Long numberOfViews;
  private Boolean verified;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @ToString
  public static class BlogCreateDTO {
    private Long id;

    @NotNull(message = "message of blog must not be null")
    private ThemeDTO theme;

    @NotBlank(message = "title of blog must not be blank")
    private String title;

    @NotBlank(message = "text of blog must not be blank")
    private String text;

    public BlogEntity map2Entity() {
      return BlogEntity.builder()
          .status(BlogStatus.ACTIVE)
          .numberViews(0L)
          .text(this.getText())
          .title(this.getTitle())
          .verified(Boolean.FALSE)
          .build();
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BlogVerifiedDTO {
    private Long id;
    private Boolean verified;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BlogWithCommentsDTO {
    private Long id;
    private ThemeDTO theme;
    private String title;
    private String text;
    private Long numberOfViews;
    private Boolean verified;
    private List<CommentDTO> comments;
  }
}
