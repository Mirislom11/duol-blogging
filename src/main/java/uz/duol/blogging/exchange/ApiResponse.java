package uz.duol.blogging.exchange;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse <T>{
    private T data;
    private String message;
    private String status;
}

