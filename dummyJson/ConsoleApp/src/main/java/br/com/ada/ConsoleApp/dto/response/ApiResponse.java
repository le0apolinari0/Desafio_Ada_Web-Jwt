package br.com.ada.ConsoleApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private Integer total;
    private Integer skip;
    private Integer limit;
    private String message;
    private Boolean success;
}
