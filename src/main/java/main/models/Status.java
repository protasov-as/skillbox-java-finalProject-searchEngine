package main.models;

public enum Status {
    INDEXING("INDEXING"),
    INDEXED("INDEXED"),
    FAILED("FAILED");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
