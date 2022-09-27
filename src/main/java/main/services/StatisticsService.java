package main.services;

import main.models.Site;
import main.models.Status;
import main.models.statistics.Statistics;
import main.models.statistics.Detailed;
import main.models.statistics.Total;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsService {

    private SiteService siteService;
    private PageService pageService;
    private LemmaService lemmaService;

    @Autowired
    public StatisticsService(SiteService siteService, PageService pageService, LemmaService lemmaService) {
        this.siteService = siteService;
        this.pageService = pageService;
        this.lemmaService = lemmaService;
    }

    public Statistics getStatistics(){
        Total total = getTotal();
        List<Site> siteList = siteService.findAllSites();
        Detailed[] detailed = new Detailed[siteList.size()];
        for (int i = 0; i < siteList.size(); i++) {
            detailed[i] = getDetailed(siteList.get(i));
        }
        return new Statistics(total, detailed);
    }

    private Detailed getDetailed(Site site){
        String url = site.getUrl();
        String name = site.getName();
        Status status = site.getStatus();
        long statusTime = site.getStatusTime().getTime();
        String error = site.getLastError();
        long pages = pageService.countPagesOnSite(site.getId());
        long lemmas = lemmaService.countLemmasOnSite(site.getId());
        return new Detailed(url, name, status, statusTime, error, pages, lemmas);
    }

    private Total getTotal(){
        long siteNumber = siteService.countSites();
        long lemmaNumber = lemmaService.countlemmas();
        long pageNumber = pageService.countPages();
        boolean isIndexing = isIndexingInProcess();
        return new Total(siteNumber, pageNumber, lemmaNumber, isIndexing);
    }

    public boolean isIndexingInProcess(){
        boolean isIndexing = false;
        for(Site s : siteService.findAllSites()){
            if(s.getStatus().equals(Status.INDEXING)){
                isIndexing = true;
                break;
            }
        }
        return isIndexing;
    }
}
