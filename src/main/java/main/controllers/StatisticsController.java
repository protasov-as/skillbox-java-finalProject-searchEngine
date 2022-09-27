package main.controllers;

import main.models.statistics.Statistics;
import main.models.responses.StatisticsResponse;
import main.services.ConfigService;
import main.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private ConfigService configService;
    private StatisticsService statisticsService;
    private Statistics statistics;
    private StatisticsResponse statisticsResponse;

    @Autowired
    public StatisticsController(ConfigService configService, StatisticsService statisticsService, Statistics statistics, StatisticsResponse statisticsResponse) {
        this.configService = configService;
        this.statisticsService = statisticsService;
        this.statistics = statistics;
        this.statisticsResponse = statisticsResponse;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatistics(){
        statistics = statisticsService.getStatistics();
        statisticsResponse.setResult(true);
        statisticsResponse.setStatistics(statistics);
        return ResponseEntity.ok (statisticsResponse);
    }
}
