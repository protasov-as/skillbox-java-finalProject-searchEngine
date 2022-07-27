package main.models;

public class Result implements Comparable<Result>{

    private String uri;
    private String title;
    private String snippet;
    private float relevance;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public float getRelevance() {
        return relevance;
    }

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }

    @Override
    public int compareTo(Result o) {
        return (int) (o.getRelevance() * 10 - this.relevance * 10);
    }

    public String toString(){
        return "Uri: " + uri + " Title: " + title + " Snippet: " + snippet + " Relevance: " + relevance + System.lineSeparator();
    }
}
