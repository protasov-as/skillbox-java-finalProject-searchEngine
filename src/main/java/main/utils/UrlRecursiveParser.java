package main.utils;

import main.services.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@Component
public class UrlRecursiveParser extends RecursiveTask<HashSet<String>> {

    private DBSaver dbSaver;
    private ApplicationContext applicationContext;
    private ConfigService configService;

    @Autowired
    public UrlRecursiveParser(DBSaver dbSaver, ApplicationContext applicationContext, ConfigService configService) {
        this.dbSaver = dbSaver;
        this.applicationContext = applicationContext;
        this.configService = configService;
    }

    private String url;
    private HashSet<String> outLinksSet;
    private HashSet<String> linksSet;
    public static ForkJoinPool pool = new ForkJoinPool();

    public UrlRecursiveParser(String url, HashSet<String> outLinksSet) {
        this.url = url;
        this.outLinksSet = outLinksSet;
    }

    public UrlRecursiveParser() {
    }

    public HashSet<String> startForkJoinPool(String siteURL) {
        linksSet = new HashSet<>();
        if(pool.isShutdown()){
            pool = new ForkJoinPool();
        }

        UrlRecursiveParser parser = applicationContext.getBean(UrlRecursiveParser.class);
        parser.setUrl(siteURL);
        parser.setOutLinksSet(linksSet);
        pool.execute(parser);

        System.out.println("Сохранено уникальных ссылок: " + linksSet.size());
        return linksSet;
    }
//
    private boolean checkLink(String link) {
        return !link.contains("#")
                && !link.contains("?")
                && !link.contains("%")
                && !link.contains(".pdf")
                && !link.contains(".jpg")
                && !link.contains(".doc")
                && !link.contains(".docx")
                && !link.contains(".jpeg");
    }

    private Set<String> getLinks(String url, Set<String> outLinksSet) throws IOException {
        Document document = Jsoup.connect(url)
                .userAgent(configService.getUserAgent())
                .referrer(configService.getReferrer())
                .get();
        Elements links = document.select("body").select("a");

        links.forEach(l -> {
                    String currentLink = l.attr("abs:href");
                    String domainHost = dbSaver.getHostFromLink(url);
                    String domainCurrentLink = dbSaver.getHostFromLink(currentLink);

                    if (checkLink(currentLink) &&
                            domainHost.equals(domainCurrentLink) &&
                            !outLinksSet.contains(currentLink)) {
                        outLinksSet.add(currentLink);
                        try {
                            dbSaver.addPagesToDBviaHibernate(currentLink);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Добавлена ссылка: " + currentLink);
                        try {
                            System.out.println("Второй заход " + currentLink);
                            getLinks(currentLink, outLinksSet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        return outLinksSet;
    }

    @Override
    protected HashSet<String> compute() {
        List<UrlRecursiveParser> taskList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url)
                    .userAgent(configService.getUserAgent())
                    .referrer(configService.getReferrer())
                    .get();
            Elements links = document.select("body").select("a");

            links.forEach(l -> {
                        String currentLink = l.attr("abs:href");
                        String domainHost = dbSaver.getHostFromLink(url);
                        String domainCurrentLink = dbSaver.getHostFromLink(currentLink);

                        if (checkLink(currentLink) &&
                                domainHost.equals(domainCurrentLink) &&
                                !outLinksSet.contains(currentLink)) {

                            UrlRecursiveParser task = applicationContext.getBean(UrlRecursiveParser.class);
                            task.setUrl(currentLink);
                            task.setOutLinksSet(outLinksSet);

                            task.fork();
                            taskList.add(task);
                        }
                    }
            );
            for (UrlRecursiveParser item : taskList) {
                getLinks(item.url, outLinksSet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outLinksSet;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setOutLinksSet(HashSet<String> outLinksSet) {
        this.outLinksSet = outLinksSet;
    }
}
