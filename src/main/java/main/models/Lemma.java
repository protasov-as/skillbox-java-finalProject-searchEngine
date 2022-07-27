package main.models;

import javax.persistence.*;

@Entity
@Table(name = "lemma")
public class Lemma implements Comparable<Lemma>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String lemma;

    private int frequency;

    @Column(name = "site_id")
    private int siteID;

    public Lemma() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }

    @Override
    public int compareTo(Lemma o) {
        return this.frequency - o.getFrequency();
    }

    public String toString(){
        return "Lemma id: " + id + " Lemma: " + lemma + " Frequency: " + frequency + " SiteID: " + siteID;
    }
}
