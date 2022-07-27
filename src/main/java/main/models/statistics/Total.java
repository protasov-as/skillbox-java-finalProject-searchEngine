package main.models.statistics;

import org.springframework.stereotype.Component;

@Component
public class Total {
    long sites;
    long pages;
    long lemmas;
    boolean isIndexing;

    public Total(long sites, long pages, long lemmas, boolean isIndexing) {
        this.sites = sites;
        this.pages = pages;
        this.lemmas = lemmas;
        this.isIndexing = isIndexing;
    }

    public Total() {
    }

    public long getSites() {
        return sites;
    }

    public void setSites(long sites) {
        this.sites = sites;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public long getLemmas() {
        return lemmas;
    }

    public void setLemmas(long lemmas) {
        this.lemmas = lemmas;
    }

    public boolean isIndexing() {
        return isIndexing;
    }

    public void setIndexing(boolean indexing) {
        isIndexing = indexing;
    }
}
