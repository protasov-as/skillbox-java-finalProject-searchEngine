package main.models.responses;

import org.springframework.stereotype.Component;

@Component
public class ErrorResponse {
    boolean result;
    String error;

    public ErrorResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public ErrorResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
