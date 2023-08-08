package uz.duol.blogging.dto;

import lombok.*;
import uz.duol.blogging.domain.CommentEntity;
import uz.duol.blogging.domain.enumeration.CommentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDTO {
  private Long id;
  private String content;
  private Integer usability;
  private Integer notUsability;
  private Boolean verified;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  @Builder
  public static class CommentCreateDTO {
    @NotBlank(message = "content of comment must not be null")
    private String content;

    @NotNull(message = "blogId of comment must not be null")
    private Long blogId;

    public CommentEntity map2Entity() {
      return CommentEntity.builder()
          .content(this.getContent())
          .status(CommentStatus.ACTIVE)
          .verified(Boolean.FALSE)
          .build();
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  @Builder
  public static class CommentUpdateDTO {
    @NotNull(message = "id must not be null")
    private Long id;

    private String content;
    private Boolean isUsability;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class CommentVerifiedUpdateDTO {
    @NotNull(message = "id must not be null")
    private Long id;

    @NotNull private Boolean verified;
  }
}
