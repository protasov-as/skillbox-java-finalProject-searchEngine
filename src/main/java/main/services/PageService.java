package main.services;

import main.dao.PageDao;
import main.dao.PageDaoCrud;
import main.models.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PageService {

    @Autowired
    private PageDao pageDao;

    @Autowired
    private PageDaoCrud pageDaoCrud;

    public PageService() {
    }

    public Optional<Page> findPage(int id) {
        return pageDaoCrud.findById(id);
    }

    public Long countPages() {
        return pageDaoCrud.count();
    }

    public Long countPagesOnSite(int siteID)  {
        return pageDao.countPagesOnSite(siteID);
    }

    public Page findPageByName(String path) {
        return pageDaoCrud.findByPathEquals(path);
    }

    public Boolean checkIfPageExists(String path) {
        return pageDaoCrud.existsByPath(path);
    }

    public int savePage(Page page) {
        return pageDaoCrud.save(page).getId();
    }

    public void persistPage(Page page) {
        pageDao.persist(page);
    }

    public void deletePage(Page page) {
        pageDaoCrud.delete(page);
    }

    public void updatePage(Page page) {
        pageDao.update(page);
    }

    public List<Page> findAllPages() {
        return pageDaoCrud.findAll();
    }

    public List<Page> findAllBySiteId(int siteID) {return pageDao.findAllbySiteId(siteID);}

    public void deleteAllPages() {
        pageDaoCrud.deleteAll();
    }

    public void dropAndCreateTablePage() {
        pageDao.dropAndCreateTable();
    }
}
