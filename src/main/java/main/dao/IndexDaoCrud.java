package main.dao;

import main.models.Index;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IndexDaoCrud extends CrudRepository<Index, Integer> {

    List<Index> findAll();

    boolean existsByPageIdAndLemmaId(int page_id, int lemma_id);

}
