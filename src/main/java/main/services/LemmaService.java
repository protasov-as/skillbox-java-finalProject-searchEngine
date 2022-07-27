package main.services;

import main.dao.LemmaDao;
import main.models.Lemma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LemmaService {
    @Autowired
    private LemmaDao lemmaDao;

    public LemmaService() {
    }

    public Lemma findLemma(Integer id) {
        return lemmaDao.findById(id);
    }

    public List<Lemma> findLemmaByName(String lemma) {
        return lemmaDao.findByName(lemma);
    }

    public Lemma findByNameAndSiteID(String name, int siteID) {return lemmaDao.findByNameAndSiteID(name, siteID);}

    public boolean isLemmaTooFrequent(Lemma lemma) {
        return lemmaDao.isLemmaTooFrequent(lemma);
    };

    public void saveLemma(Lemma lemma) {
        lemmaDao.save(lemma);
    }

    public int saveOrUpdateLemma(Lemma lemma) {
       return lemmaDao.saveOrUpdateLemma(lemma);
    }

    public void persistLemma(Lemma lemma) {
        lemmaDao.persist(lemma);
    }

    public void deleteLemma(Lemma lemma) {
        lemmaDao.delete(lemma);
    }

    public void updateLemma(Lemma lemma) {
        lemmaDao.update(lemma);
    }

    public List<Lemma> findAllLemmas() {
        return lemmaDao.findAll();
    }

    public List<Lemma> findAllBySiteID(int siteID) {return lemmaDao.findAllBySiteID(siteID);}

    public void deleteAllLemmas() {
        lemmaDao.deleteAll();
    }

    public long countlemmas() {
        return lemmaDao.countLemmas();
    }

    public Long countLemmasOnSite(int siteID)  {
        return lemmaDao.countLemmasOnSite(siteID);
    }

    public void dropAndCreateTableLemma() {
        lemmaDao.dropAndCreateTable();
    }
}
