package main.controllers;

import main.models.responses.ErrorResponse;
import main.models.responses.SearchResponse;
import main.models.search.Data;
import main.services.SearcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class SearchController {

    private SearcherService searcherService;

    @Autowired
    public SearchController(SearcherService searcherService) {
        this.searcherService = searcherService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name="query", required=false, defaultValue="") String query,
            @RequestParam(name="site", required=false, defaultValue="") String site,
            @RequestParam(name="offset", required=false, defaultValue="0") int offset,
            @RequestParam(name="limit", required=false, defaultValue="0") int limit)
            throws IOException {
        if(query.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(false,"Задан пустой поисковый запрос!");
            return ResponseEntity.ok (errorResponse);
        } else {
            ArrayList<Data> dataArray = searcherService.getDataFromSearchInput(query, site);
            SearchResponse searchResponse = new SearchResponse(true, dataArray.size(), dataArray);
            return ResponseEntity.ok(searchResponse);
        }
    }
}
