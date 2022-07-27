package main.models.responses;

import main.models.statistics.Statistics;
import org.springframework.stereotype.Component;

@Component
public class StatisticsResponse {
    boolean result;
    Statistics statistics;

    public StatisticsResponse(boolean result, Statistics statistics) {
        this.result = result;
        this.statistics = statistics;
    }

    public StatisticsResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
