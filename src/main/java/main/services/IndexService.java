package main.services;

import main.dao.IndexDao;
import main.models.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexService {

    @Autowired
    private IndexDao indexDao;

    public IndexService(){
    }

    public Index findIndex(int id) {
        return indexDao.findById(id);
    }

    public int saveIndex(Index index) {
        return indexDao.save(index);
    }

    public Boolean checkIfIndexExists(int page_id, int lemma_id) {
        return indexDao.checkIfIndexExists(page_id, lemma_id);
    }

    public void deleteByPageId(int page_id) {indexDao.deleteByPageId(page_id);
    }

    public int saveOrUpdateIndex(Index index) {
        return indexDao.saveOrUpdateIndex(index);
    }

    public List<Index> findByLemmaId(int lemma_id) {
        return indexDao.findByLemmaId(lemma_id);
    }

    public Index findByIdPair(int page_id, int lemma_id) {
        return indexDao.findByIdPair(page_id, lemma_id);
    }

    public void persistIndex(Index index) {
        indexDao.persist(index);
    }

    public void deleteIndex(Index index) {
        indexDao.delete(index);
    }

    public void updateIndex(Index index) {
        indexDao.update(index);
    }

    public List<Index> findAllIndexes() {
        return indexDao.findAll();
    }

    public void deleteAllIndexes() {
        indexDao.deleteAll();
    }

    public void dropAndCreateTableIndex() {
        indexDao.dropAndCreateTable();
    }

}
