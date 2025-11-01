package br.com.ada.ConsoleApp.dto.response;

import br.com.ada.ConsoleApp.domain.Todo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodoResponse extends ApiResponse<List<Todo>> {
    private List<Todo> todos;
}


