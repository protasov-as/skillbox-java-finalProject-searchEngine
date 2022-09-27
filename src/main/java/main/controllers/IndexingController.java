package main.controllers;

import main.models.responses.EmptyTrueResponse;
import main.models.responses.ErrorResponse;
import main.services.ConfigService;
import main.services.IndexingService;
import main.services.SiteService;
import main.services.StatisticsService;
import main.utils.UrlRecursiveParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class IndexingController {

    private StatisticsService statisticsService;
    private IndexingService indexingService;
    private SiteService siteService;
    private ConfigService configService;

    @Autowired
    public IndexingController(StatisticsService statisticsService, IndexingService indexingService, SiteService siteService, ConfigService configService) {
        this.statisticsService = statisticsService;
        this.indexingService = indexingService;
        this.siteService = siteService;
        this.configService = configService;
    }


    @GetMapping("/startIndexing")
    public ResponseEntity<Object> startIndexingAll() {
        if(statisticsService.isIndexingInProcess()) {
            ErrorResponse errorResponse = new ErrorResponse(false,"Индексация уже запущена!");
            return ResponseEntity.ok (errorResponse);
        }
        else {
            HashMap<String, String> sitesMap = indexingService.getSitesFromConfig();
            sitesMap.forEach((k,v) -> indexingService.removeSiteAndDependingPagesLemmasIndexes(v));
            indexingService.indexPagesFromMap(sitesMap);
            return ResponseEntity.ok (new EmptyTrueResponse(true));
        }
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Object> stopIndexingAll() {
        if(!statisticsService.isIndexingInProcess()) {
            ErrorResponse errorResponse = new ErrorResponse(false,"Индексация не запущена!");
            return ResponseEntity.ok (errorResponse);
        }
        else {
            UrlRecursiveParser.pool.shutdownNow();
            siteService.markAllSitesAsFailed();
            return ResponseEntity.ok (new EmptyTrueResponse(true));
        }
    }

    @PostMapping("/indexPage")
    public ResponseEntity<Object> startIndexingOne(
            @RequestParam(name="url", required=false, defaultValue=" ") String url) throws IOException {
        if(!indexingService.checkIfSiteIsIndexed(url)) {
            ErrorResponse errorResponse = new ErrorResponse(false,"Данная страница находится за пределами сайтов, указанных в конфигурационном файле!");
            return ResponseEntity.ok (errorResponse);
        } else {
            indexingService.removePageAndIndexes(url);
            indexingService.indexOnePage(url);
            return ResponseEntity.ok(new EmptyTrueResponse(true));
        }
    }
}
