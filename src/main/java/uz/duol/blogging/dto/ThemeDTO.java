package uz.duol.blogging.dto;

import lombok.*;
import uz.duol.blogging.domain.ThemeEntity;
import uz.duol.blogging.domain.enumeration.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ThemeDTO {
    private Long id;
    @NotBlank(message = "name of theme must not be blank")
    private String name;
    @NotNull(message = "category of theme must not be null")
    private Category category;

    public ThemeEntity map2Entity() {
        return ThemeEntity.builder()
                .category(this.getCategory())
                .name(this.getName())
                .build();
    }
}

