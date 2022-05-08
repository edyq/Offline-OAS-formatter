package org.ntu.apiconverter.merger;

import lombok.Getter;

import java.util.*;

@Getter
public class UrlGroup {

    private int length;

    private Set<Url> urls;

    public UrlGroup(int length) {
        this.length = length;
        this.urls = new HashSet<Url>();
    }

    public void addUrl(Url url) {
        this.urls.add(url);
    }

    public ArrayList<Url> getUrlList() {
        ArrayList<Url> res = new ArrayList<>();
        for (Url url : this.urls)
            if (url.isInGroup())
                res.add(url);
        return res;
    }

    public void removeUrls(List<Token> tokens) {
        for (Url url : this.urls) {
            for (Token token : tokens)
                if (url.getTokenList().contains(token)) url.setInGroup(false);
        }
    }
}
