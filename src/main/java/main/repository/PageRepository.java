package main.repository;

import main.models.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PageRepository extends CrudRepository<Page, Integer> {

    List<Page> findAll();

    boolean existsByPath(String path);

    Page findByPathEquals(String path);
}
