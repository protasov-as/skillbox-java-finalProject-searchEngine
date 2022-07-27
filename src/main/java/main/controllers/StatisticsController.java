package main.controllers;

import main.models.statistics.Statistics;
import main.models.responses.StatisticsResponse;
import main.services.ConfigService;
import main.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class StatisticsController {

    @Autowired
    private ConfigService configService;
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    Statistics statistics;
    @Autowired
    StatisticsResponse statisticsResponse;

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatistics(){
        statistics = statisticsService.getStatistics();
        statisticsResponse.setResult(true);
        statisticsResponse.setStatistics(statistics);
        return ResponseEntity.ok (statisticsResponse);
    }
}
