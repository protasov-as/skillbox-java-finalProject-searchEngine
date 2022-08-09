package main.repository;

import main.models.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SiteRepository extends CrudRepository<Site, Integer> {

    List<Site> findAll();

    Site findByUrlEquals(String url);

    Site findByUrlLike(String url);

    boolean existsByUrl(String url);

}
