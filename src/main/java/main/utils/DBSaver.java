package main.utils;

import main.models.*;
import main.services.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class DBSaver {

    private SiteService siteService;
    private PageService pageService;
    private FieldService fieldService;
    private LemmaService lemmaService;
    private IndexService indexService;
    private Lemmatizer lemmatizer;
    private ConfigService configService;

    @Autowired
    public DBSaver(SiteService siteService, PageService pageService, FieldService fieldService, LemmaService lemmaService, IndexService indexService, Lemmatizer lemmatizer, ConfigService configService) {
        this.siteService = siteService;
        this.pageService = pageService;
        this.fieldService = fieldService;
        this.lemmaService = lemmaService;
        this.indexService = indexService;
        this.lemmatizer = lemmatizer;
        this.configService = configService;
    }

    public void addPagesToDBviaHibernate(String url) throws IOException {
        int code = Jsoup.connect(url)
                .userAgent(configService.getUserAgent())
                .referrer(configService.getReferrer())
                .execute().statusCode();
        String content = Jsoup.connect(url)
                .userAgent(configService.getUserAgent())
                .referrer(configService.getReferrer())
                .get().toString();
        content = content.replaceAll("\"","\\\"").replaceAll("'","''");

        Site site = siteService.findByName("%" + getHostFromLink(url) + "%");

        Page page = new Page();
        page.setPath(getPathFromLink(url));
        page.setCode(code);
        page.setContent(content);
        page.setSiteID(site.getId());
        int pageId = pageService.savePage(page);

        site.setStatusTime(new Date());
        siteService.updateSite(site);

        List<Field> fields = fieldService.findAllFields();
        String finalContent = content;

        HashMap<String, Float> mapTitle = new HashMap<>();
        HashMap<String, Float> mapBody = new HashMap<>();

        fields.forEach(f -> {
            Document document = Jsoup.parse(finalContent);
            String selector = f.getSelector();
            Elements ele = document.select(selector);
            String lemmaInput = ele.toString();

            if(lemmaInput.length() != 0) {
                HashMap<String, Float> map = lemmatizer.lemmasMapFromString(lemmaInput);
                if(f.getSelector().equals("title")) {
                    mapTitle.putAll(map);
                } else {
                    map.replaceAll((k,v) -> v = v * 0.8f);
                    mapBody.putAll(map);
                }
            }
        });

        HashMap<String, Float> mapToDB = combineTwoMaps(mapTitle, mapBody);
        mapToDB.forEach((key, value) -> {
            Lemma lemma = new Lemma();
            lemma.setLemma(key);
            lemma.setFrequency(1);
            lemma.setSiteID(site.getId());
            int lemmaId = lemmaService.saveOrUpdateLemma(lemma);

            Index index = new Index();
            index.setLemmaId(lemmaId);
            index.setPageId(pageId);
            index.setRank(value);
            indexService.saveIndex(index);
        });
    }
    private static HashMap<String, Float> combineTwoMaps(HashMap<String, Float> mapTitle,  HashMap<String, Float> mapBody){
        mapTitle.forEach((k, v) -> {
            if (mapBody.containsKey(k)) {
                mapBody.put(k, v + mapBody.get(k));
            } else mapBody.put(k, v);
        });
        return mapBody;
    }

    public String getHostFromLink(String link) {
        URL url = null;
        if (!link.isEmpty()) {
            try {
                url = new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url.getHost(); }
        else return "";
    }

    public String getPathFromLink(String link) {
        URL url = null;
        if (!link.isEmpty()) {
            try {
                url = new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url.getFile(); }
        else return "";
    }

    public void dropAndCreateTables(){
        pageService.dropAndCreateTablePage();
        indexService.dropAndCreateTableIndex();
        lemmaService.dropAndCreateTableLemma();
        fieldService.dropAndCreateTableField();
        fieldService.initializeFields();
        siteService.dropAndCreateTableSite();
    }

    private void addPagesToDBWithoutHibernate(String url) {
        try {
            int code = Jsoup.connect(url).execute().statusCode();
            String content = Jsoup.connect(url).get().body().toString();
            content = content.replaceAll("\"","\\\"").replaceAll("'","''");
            String queryValues = "('"  + getPathFromLink(url) + "', '" + code + "', '" + content +"')";
            DBConnection.insertQuery(queryValues);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
