package main.services;

import main.dao.PageDao;
import main.repository.PageRepository;
import main.models.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PageService {

    private PageDao pageDao;
    private PageRepository pageRepository;

    @Autowired
    public PageService(PageDao pageDao, PageRepository pageRepository) {
        this.pageDao = pageDao;
        this.pageRepository = pageRepository;
    }

    public PageService() {
    }

    public Optional<Page> findPage(int id) {
        return pageRepository.findById(id);
    }

    public Long countPages() {
        return pageRepository.count();
    }

    public Long countPagesOnSite(int siteID)  {
        return pageDao.countPagesOnSite(siteID);
    }

    public Page findPageByName(String path) {
        return pageRepository.findByPathEquals(path);
    }

    public Boolean checkIfPageExists(String path) {
        return pageRepository.existsByPath(path);
    }

    public int savePage(Page page) {
        return pageRepository.save(page).getId();
    }

    public void persistPage(Page page) {
        pageDao.persist(page);
    }

    public void deletePage(Page page) {
        pageRepository.delete(page);
    }

    public void updatePage(Page page) {
        pageDao.update(page);
    }

    public List<Page> findAllPages() {
        return pageRepository.findAll();
    }

    public List<Page> findAllBySiteId(int siteID) {return pageDao.findAllbySiteId(siteID);}

    public void deleteAllPages() {
        pageRepository.deleteAll();
    }

    public void dropAndCreateTablePage() {
        pageDao.dropAndCreateTable();
    }
}
