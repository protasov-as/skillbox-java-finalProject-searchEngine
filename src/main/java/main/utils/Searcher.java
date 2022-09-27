package main.utils;

import main.models.*;
import main.models.search.Data;
import main.services.IndexService;
import main.services.LemmaService;
import main.services.PageService;
import main.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Searcher {

    private IndexService indexService;
    private PageService pageService;
    private SiteService siteService;
    private LemmaService lemmaService;
    private Lemmatizer lemmatizer;

    @Autowired
    public Searcher(IndexService indexService, PageService pageService, SiteService siteService, LemmaService lemmaService, Lemmatizer lemmatizer) {
        this.indexService = indexService;
        this.pageService = pageService;
        this.siteService = siteService;
        this.lemmaService = lemmaService;
        this.lemmatizer = lemmatizer;
    }

    private int lemmaInInputCount;

    public ArrayList<Data> searchStringToDataArray(String searchInput, String siteURL) {
        ArrayList<Lemma> sortedArray = inputToLemmasSortedArrayWithoutTooFrequentLemmas(searchInput, siteURL);
        ArrayList<Data> resultList = new ArrayList<>();
        if(sortedArray.size() == 0) {
            System.out.println("Искомые слова не найдены!");
        } else {
            List<Index> leastFrequentLemmaIndexes = new ArrayList<>();
            HashSet<Integer> sitesSet = new HashSet<>();
            for (Lemma sortedLemma : sortedArray) {
                sitesSet.add(sortedLemma.getSiteID());
            }
            sitesSet.forEach(siteID ->{
                List<Lemma> filteredList = sortedArray.stream().filter(lemma -> lemma.getSiteID() == siteID).collect(Collectors.toList());
                Collections.sort(filteredList);

                if(filteredList.size() == lemmaInInputCount) {
                List<Index> tempLemmaIndexes = findIndexesForSearchOutput(filteredList);
//                if(tempLemmaIndexes.size() > 1) {leastFrequentLemmaIndexes.addAll(tempLemmaIndexes);}
                leastFrequentLemmaIndexes.addAll(tempLemmaIndexes);}

            });
            resultList = lemmaIndexesToData(leastFrequentLemmaIndexes, sortedArray);
        }
        return resultList;
    }

    private ArrayList<Data> lemmaIndexesToData(List<Index> leastFrequentLemmaIndexes, ArrayList<Lemma> sortedArray) {
        ArrayList<Data> resultList = new ArrayList<>();
        leastFrequentLemmaIndexes.forEach(lfl -> {
            Page page = pageService.findPage(lfl.getPageId()).get();
            Site site = siteService.findById(page.getSiteID()).get();
            Lemma lemma = lemmaService.findLemma(lfl.getLemmaId()).get();
            Data data = new Data(site.getUrl(),
                    site.getName(),
                    page.getPath(),
                    findPageTitle(page.getContent()),
//                        findPageSnippet(page.getContent(),leastFrequentLemma.getLemma()),
                    findPageSnippet(page.getContent(),lemma.getLemma()),
                    0);
            sortedArray.forEach(l -> {
                if (indexService.checkIfIndexExists(lfl.getPageId(), l.getId())) {
                    data.setRelevance(data.getRelevance() + indexService.findByIdPair(lfl.getPageId(), l.getId()).getRank());
                }
            });
            resultList.add(data);
        });
        Collections.sort(resultList);
        return resultList;
    }

    private List<Index> findIndexesForSearchOutput(List<Lemma> lemmaList) {
        Lemma leastFrequentLemma = lemmaList.get(0);
        System.out.println(lemmaList);
        System.out.println(leastFrequentLemma);
        List<Index> leastFrequentLemmaIndexes = indexService.findByLemmaId(leastFrequentLemma.getId());
        List<Index> leastFrequentLemmaIndexesToDeleteFrom = new ArrayList<>(leastFrequentLemmaIndexes);
        System.out.println(leastFrequentLemmaIndexes);
        for (int i = 1; i < lemmaList.size(); i++) {
            if(!leastFrequentLemmaIndexesToDeleteFrom.isEmpty()) {
                int finalI = i;
                leastFrequentLemmaIndexes.forEach(lfl -> {
                    if (!indexService.checkIfIndexExists(lfl.getPageId(), lemmaList.get(finalI).getId()))
//                            && lfl.getLemmaId() != lemmaList.get(finalI).getId())
                    {
                        leastFrequentLemmaIndexesToDeleteFrom.remove(lfl);
                        System.out.println(leastFrequentLemmaIndexesToDeleteFrom.size());
                    }
                });
            }
            leastFrequentLemmaIndexes = new ArrayList<>(leastFrequentLemmaIndexesToDeleteFrom);
        }
        return leastFrequentLemmaIndexesToDeleteFrom;
    }

    public ArrayList<Lemma> inputToLemmasSortedArrayWithoutTooFrequentLemmas(String input, String siteURL) {
        ArrayList<String> list = new ArrayList<>();
        Long totalPagesCount = pageService.countPages();
        double frequencyThreshold = 0.3;
        int tooFrequentCoefficient = (int) (totalPagesCount * frequencyThreshold);
        try {
            list = lemmatizer.getBasicFormsFromString(input);
            lemmaInInputCount = list.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Lemma> lemmasSortedList = new ArrayList<Lemma>();
        list.forEach(e -> {
            Lemma lemma;
            List<Lemma> listFromDB = new ArrayList<>();
            if(!siteURL.isEmpty()) {
                Site site = siteService.findByName(siteURL);
                lemma = lemmaService.findByNameAndSiteID(e, site.getId());
                listFromDB.add(lemma);
            } else {
                listFromDB = lemmaService.findLemmaByName(e);
            }
            listFromDB.forEach(ele -> {
                if(ele.getFrequency() <= tooFrequentCoefficient && ele.getFrequency() != 0)
                    lemmasSortedList.add(ele);
            });
        });
        Collections.sort(lemmasSortedList);
        return lemmasSortedList;
    }

    public String findPageTitle(String page) {
        int startTitle = page.indexOf("<title>");
        int endTitle = page.indexOf("</title>");
        if(startTitle == -1 || endTitle == -1) {
            return "";
        }
        else return page.substring(startTitle + 7, endTitle);
    }

    public String findPageSnippet(String pageContent, String leastFrequentLemma) {
        String plainText = lemmatizer.replaceAuxiliarySymbols(pageContent);
        String out = "!!!error";

        for (int i = 0; i < 5; i++) {
            if(leastFrequentLemma.length() > i) {
                String leastFrequentLemmaWithoutSuffix = leastFrequentLemma.substring(0, leastFrequentLemma.length() - i);
                int beginIndex = plainText.indexOf(leastFrequentLemmaWithoutSuffix);
                if(beginIndex != -1) {
                    int beginSnippet = beginIndex > 100 ? beginIndex - 100 : 0;
                    int endSnippet = Math.min(beginIndex + 100, plainText.length());
                    out = plainText.substring(beginSnippet, endSnippet).replaceAll(leastFrequentLemmaWithoutSuffix, "<b>" + leastFrequentLemmaWithoutSuffix + "</b>");
                    break;
                }
            }
        }
        return out;
    }
}
