package main.services;

import main.dao.SiteDao;
import main.models.Page;
import main.models.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SiteService {

    @Autowired
    private SiteDao siteDao;

    public Site findById(Integer id) {return siteDao.findById(id);}

    public int saveSite(Site site) {
        return siteDao.save(site);
    }

    public void updateSite(Site site) {
        siteDao.update(site);
    }

    public void dropAndCreateTableSite() {
        siteDao.dropAndCreateTable();
    }

    public long countSites() {
        return siteDao.countSites();
    }

    public Site findByName(String url) {return siteDao.findByName(url);}

    public Site findByExactName(String url) {return siteDao.findByExactName(url);}

    public Boolean checkIfSiteExists(String name) {return siteDao.checkIfSiteExists(name);}

    public Boolean checkIfSiteExistsByExactMatch(String name) {return siteDao.checkIfSiteExistsByExactMatch(name);}

    public void markAllSitesAsFailed(){ siteDao.markAllSitesAsFailed();}

    public void delete(Site site) {siteDao.delete(site);}

    public List<Site> findAllSites() {
        return siteDao.findAll();
    }
}
