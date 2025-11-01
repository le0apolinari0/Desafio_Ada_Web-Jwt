package br.com.ada.ConsoleApp.dto.response;

import br.com.ada.ConsoleApp.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchtResponse {
    private List<Product> products;
    private Integer total;
    private Integer skip;
    private Integer limit;
}
