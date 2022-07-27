package main.models.responses;

import org.springframework.stereotype.Component;

@Component
public class EmptyTrueResponse {
    boolean result;

    public EmptyTrueResponse() {
    }

    public EmptyTrueResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
