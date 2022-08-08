package main.dao;

import main.models.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SiteDaoCrud extends CrudRepository<Site, Integer> {

    List<Site> findAll();

    Site findByUrlEquals(String url);

    Site findByUrlLike(String url);

    boolean existsByUrl(String url);

}
