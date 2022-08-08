package main.services;

import main.dao.IndexDao;
import main.dao.IndexDaoCrud;
import main.models.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class IndexService {

    @Autowired
    private IndexDao indexDao;

    @Autowired
    private IndexDaoCrud indexDaoCrud;

    public IndexService(){
    }

    public Optional<Index> findIndex(int id) {
        return indexDaoCrud.findById(id);
    }

    public int saveIndex(Index index) {
        return indexDaoCrud.save(index).getId();
    }

    public Boolean checkIfIndexExists(int page_id, int lemma_id) {
        return indexDaoCrud.existsByPageIdAndLemmaId(page_id, lemma_id);
//        return indexDao.checkIfIndexExists(page_id, lemma_id);
    }

    public void deleteByPageId(int page_id) {
        indexDao.deleteByPageId(page_id);
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
        indexDaoCrud.delete(index);
    }

    public void updateIndex(Index index) {
        indexDao.update(index);
    }

    public List<Index> findAllIndexes() {
        return indexDaoCrud.findAll();
    }

    public void deleteAllIndexes() {
        indexDaoCrud.deleteAll();
    }

    public void dropAndCreateTableIndex() {
        indexDao.dropAndCreateTable();
    }

}
