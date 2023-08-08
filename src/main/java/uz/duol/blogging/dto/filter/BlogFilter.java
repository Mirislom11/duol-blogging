package uz.duol.blogging.dto.filter;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogFilter {
    private Long themeId;
    private String title;
    private Boolean verified;
}
