package main.models.responses;

import main.models.Result;
import main.models.search.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SearchResponse {
    boolean result;
    long count;
    ArrayList<Data> data;

    public SearchResponse(boolean result, long count, ArrayList<Data> data) {
        this.result = result;
        this.count = count;
        this.data = data;
    }

    public SearchResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
