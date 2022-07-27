package main.models.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Statistics {

    @Autowired
    Total total;

    @Autowired
    Detailed[] detailed;

    public Statistics(Total total, Detailed[] detailed) {
        this.total = total;
        this.detailed = detailed;
    }

    public Statistics() {
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public Detailed[] getDetailed() {
        return detailed;
    }

    public void setDetailed(Detailed[] detailed) {
        this.detailed = detailed;
    }
}
