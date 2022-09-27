package main.services;

import main.models.Lemma;
import main.models.Page;
import main.models.Site;
import main.models.Status;
import main.utils.DBSaver;
import main.utils.UrlRecursiveParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class IndexingService {

    SiteService siteService;
    UrlRecursiveParser urlRecursiveParser;
    PageService pageService;
    IndexService indexService;
    LemmaService lemmaService;
    ConfigService configService;
    DBSaver dbSaver;

    @Autowired
    public IndexingService(SiteService siteService, UrlRecursiveParser urlRecursiveParser, PageService pageService, IndexService indexService, LemmaService lemmaService, ConfigService configService, DBSaver dbSaver) {
        this.siteService = siteService;
        this.urlRecursiveParser = urlRecursiveParser;
        this.pageService = pageService;
        this.indexService = indexService;
        this.lemmaService = lemmaService;
        this.configService = configService;
        this.dbSaver = dbSaver;
    }

    public IndexingService() {
    }

    public boolean checkIfSiteIsIndexed(String searchURL) {
        String siteHost = "%" + getHostFromLink(searchURL) + "%";
        return siteService.checkIfSiteExists(siteHost);
    }

    public boolean checkIfSiteIsIndexedByFullName(String searchURL) {
        return siteService.checkIfSiteExistsByExactMatch(searchURL);
    }

    public void removePageAndIndexes(String url) {
        String path = getPathFromLink(url);
        if(pageService.checkIfPageExists(path)) {
            Page page = pageService.findPageByName(path);
            indexService.deleteByPageId(page.getId());
            pageService.deletePage(page);
        }
    }

    public void removeSiteAndDependingPagesLemmasIndexes(String searchURL) {
        if(checkIfSiteIsIndexed(searchURL)) {
            Site site = siteService.findByName(searchURL);
            List<Page> pagesList = pageService.findAllBySiteId(site.getId());
            List<Lemma> lemmaList = lemmaService.findAllBySiteID(site.getId());
            pagesList.forEach(p -> {
                indexService.deleteByPageId(p.getId());
                pageService.deletePage(p);
            });
            lemmaList.forEach(l -> lemmaService.deleteLemma(l));
            siteService.delete(site);
        } else if(checkIfSiteIsIndexedByFullName(searchURL)) {
            Site site = siteService.findByExactName(searchURL);
            List<Page> pagesList = pageService.findAllBySiteId(site.getId());
            List<Lemma> lemmaList = lemmaService.findAllBySiteID(site.getId());
            pagesList.forEach(p -> {
                indexService.deleteByPageId(p.getId());
                pageService.deletePage(p);
            });
            lemmaList.forEach(l -> lemmaService.deleteLemma(l));
            siteService.delete(site);
        }
        else System.out.println("NOT FOUND! " + searchURL);
    }

    public void indexOnePage(String url) throws IOException {
//        urlRecursiveParser.addPagesToDBviaHibernate(url);
        dbSaver.addPagesToDBviaHibernate(url);
    }

    private String getHostFromLink(String link) {
        return dbSaver.getHostFromLink(link);
    }

    private String getPathFromLink(String link) {
        return dbSaver.getPathFromLink(link);
    }

    public HashMap<String, String> getSitesFromConfig() {
        HashMap<String, String> siteMap = new HashMap<>();
        ArrayList<HashMap<String, String>> mapList = configService.getSites();
        for (HashMap<String, String> map : mapList) {
            String name = "";
            String url = "";
            for (Map.Entry<String, String> siteInfo : map.entrySet()) {
                if (siteInfo.getKey().equals("name")) {
                    name = siteInfo.getValue();
                }
                if (siteInfo.getKey().equals("url")) {
                    url = siteInfo.getValue();
                }
                siteMap.put(name, url);
            }
        }
        return siteMap;
    }

    public void indexPagesFromMap(HashMap<String, String> siteList){
        siteList.forEach((k, v) -> {
            Site site = new Site();
            site.setName(k);
            site.setUrl(v);
            site.setStatus(Status.INDEXING);
            site.setStatusTime(new Date());
            site.setId(siteService.saveSite(site));

            urlRecursiveParser.startForkJoinPool(v);
            site.setStatus(Status.INDEXED);
            siteService.updateSite(site);
        });
    }
}
