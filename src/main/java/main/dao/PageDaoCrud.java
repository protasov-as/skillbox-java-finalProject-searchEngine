package main.dao;

import main.models.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PageDaoCrud extends CrudRepository<Page, Integer> {

    List<Page> findAll();

    boolean existsByPath(String path);

    Page findByPathEquals(String path);
}
