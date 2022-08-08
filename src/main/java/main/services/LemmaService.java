package main.services;

import main.dao.LemmaDao;
import main.dao.LemmaDaoCrud;
import main.models.Lemma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LemmaService {
    @Autowired
    private LemmaDao lemmaDao;

    @Autowired
    private LemmaDaoCrud lemmaDaoCrud;

    public LemmaService() {
    }

    public Optional<Lemma> findLemma(Integer id) {
        return lemmaDaoCrud.findById(id);
    }

    public List<Lemma> findLemmaByName(String lemma) {
        return lemmaDaoCrud.findAllByLemma(lemma);
//        return lemmaDao.findByName(lemma);
    }

    public Lemma findByNameAndSiteID(String lemma, int siteID) {
        return lemmaDaoCrud.findByLemmaAndSiteID(lemma, siteID);
//        return lemmaDao.findByNameAndSiteID(lemma, siteID);
    }

    public boolean isLemmaTooFrequent(Lemma lemma) {
        return lemmaDao.isLemmaTooFrequent(lemma);
    }

    public void saveLemma(Lemma lemma) {
        lemmaDaoCrud.save(lemma);
    }

    public int saveOrUpdateLemma(Lemma lemma) {
       return lemmaDao.saveOrUpdateLemma(lemma);
    }

    public void persistLemma(Lemma lemma) {
        lemmaDao.persist(lemma);
    }

    public void deleteLemma(Lemma lemma) {
        lemmaDaoCrud.delete(lemma);
    }

    public void updateLemma(Lemma lemma) {
        lemmaDao.update(lemma);
    }

    public List<Lemma> findAllLemmas() {
        return lemmaDaoCrud.findAll();
    }

    public List<Lemma> findAllBySiteID(int siteID) {
        return lemmaDaoCrud.findAllBySiteID(siteID);
//        return lemmaDao.findAllBySiteID(siteID);
    }

    public void deleteAllLemmas() {
        lemmaDaoCrud.deleteAll();
    }

    public long countlemmas() {
        return lemmaDaoCrud.count();
    }

    public Long countLemmasOnSite(int siteID)  {
        return lemmaDao.countLemmasOnSite(siteID);
    }

    public void dropAndCreateTableLemma() {
        lemmaDao.dropAndCreateTable();
    }
}
