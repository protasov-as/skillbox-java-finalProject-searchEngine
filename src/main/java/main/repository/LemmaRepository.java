package main.repository;

import main.models.Lemma;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LemmaRepository extends CrudRepository<Lemma, Integer> {


    List<Lemma> findAllByLemma(String lemma);

    List<Lemma> findAll();

    List<Lemma> findAllBySiteID(int siteID);

    Lemma findByLemmaAndSiteID(String lemma, int siteID);

}
