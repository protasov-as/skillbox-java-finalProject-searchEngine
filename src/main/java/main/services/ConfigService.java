package main.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
//@Configuration
@ConfigurationProperties(prefix = "config")
//@EnableConfigurationProperties
public class ConfigService {

    private String userAgent;
    private String referrer;
    private String webInterfacePath;
    private ArrayList<HashMap<String, String>> sites;
    private String url;
    private String username;
    private String password;


    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<HashMap<String, String>> getSites() {
        return sites;
    }

    public void setSites(ArrayList<HashMap<String, String>> sites) {
        this.sites = sites;
    }

    public String getWebInterfacePath() {
        return webInterfacePath;
    }

    public void setWebInterfacePath(String webInterfacePath) {
        this.webInterfacePath = webInterfacePath;
    }
}
