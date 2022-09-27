package main.services;
import main.models.search.Data;
import main.utils.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SearcherService {

    private Searcher searcher;
    private SiteService siteService;

    @Autowired
    public SearcherService(Searcher searcher, SiteService siteService) {
        this.searcher = searcher;
        this.siteService = siteService;
    }

    public SearcherService() {
    }

    public ArrayList<Data> getDataFromSearchInput(String searchInput, String siteURL){
        ArrayList<Data> dataArrayList = searcher.searchStringToDataArray(searchInput, siteURL);
        return dataArrayList;
    }
}
