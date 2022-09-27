package main.services;

import main.dao.LemmaDao;
import main.repository.LemmaRepository;
import main.models.Lemma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LemmaService {

    private LemmaDao lemmaDao;
    private LemmaRepository lemmaRepository;

    @Autowired
    public LemmaService(LemmaDao lemmaDao, LemmaRepository lemmaRepository) {
        this.lemmaDao = lemmaDao;
        this.lemmaRepository = lemmaRepository;
    }

    public LemmaService() {
    }

    public Optional<Lemma> findLemma(Integer id) {
        return lemmaRepository.findById(id);
    }

    public List<Lemma> findLemmaByName(String lemma) {
        return lemmaRepository.findAllByLemma(lemma);
//        return lemmaDao.findByName(lemma);
    }

    public Lemma findByNameAndSiteID(String lemma, int siteID) {
        return lemmaRepository.findByLemmaAndSiteID(lemma, siteID);
//        return lemmaDao.findByNameAndSiteID(lemma, siteID);
    }

    public boolean isLemmaTooFrequent(Lemma lemma) {
        return lemmaDao.isLemmaTooFrequent(lemma);
    }

    public void saveLemma(Lemma lemma) {
        lemmaRepository.save(lemma);
    }

    public int saveOrUpdateLemma(Lemma lemma) {
       return lemmaDao.saveOrUpdateLemma(lemma);
    }

    public void persistLemma(Lemma lemma) {
        lemmaDao.persist(lemma);
    }

    public void deleteLemma(Lemma lemma) {
        lemmaRepository.delete(lemma);
    }

    public void updateLemma(Lemma lemma) {
        lemmaDao.update(lemma);
    }

    public List<Lemma> findAllLemmas() {
        return lemmaRepository.findAll();
    }

    public List<Lemma> findAllBySiteID(int siteID) {
        return lemmaRepository.findAllBySiteID(siteID);
//        return lemmaDao.findAllBySiteID(siteID);
    }

    public void deleteAllLemmas() {
        lemmaRepository.deleteAll();
    }

    public long countlemmas() {
        return lemmaRepository.count();
    }

    public Long countLemmasOnSite(int siteID)  {
        return lemmaDao.countLemmasOnSite(siteID);
    }

    public void dropAndCreateTableLemma() {
        lemmaDao.dropAndCreateTable();
    }
}
