package pl.pacinho.muonlinewebtrader.tools.pageable.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pl.pacinho.muonlinewebtrader.tools.pageable.Paging;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paged<T> {

    private List<T> page;
    private Paging paging;

}