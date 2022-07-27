package main.services;

import main.dao.PageDao;
import main.models.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageService {

    @Autowired
    private PageDao pageDao;

    public PageService() {
    }

    public Page findPage(int id) {
        return pageDao.findById(id);
    }

    public Long countPages() {
        return pageDao.countPages();
    }

    public Long countPagesOnSite(int siteID)  {
        return pageDao.countPagesOnSite(siteID);
    }

    public Page findPageByName(String name) {
        return pageDao.findByName(name);
    }

    public Boolean checkIfPageExists(String name) {
        return pageDao.checkIfPageExists(name);
    }

    public int savePage(Page page) {
        return pageDao.save(page);
    }

    public void persistPage(Page page) {
        pageDao.persist(page);
    }

    public void deletePage(Page page) {
        pageDao.delete(page);
    }

    public void updatePage(Page page) {
        pageDao.update(page);
    }

    public List<Page> findAllPages() {
        return pageDao.findAll();
    }

    public List<Page> findAllBySiteId(int siteID) {return pageDao.findAllbySiteId(siteID);}

    public void deleteAllPages() {
        pageDao.deleteAll();
    }

    public void dropAndCreateTablePage() {
        pageDao.dropAndCreateTable();
    }
}
