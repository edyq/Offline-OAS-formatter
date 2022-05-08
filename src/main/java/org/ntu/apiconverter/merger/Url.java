package org.ntu.apiconverter.merger;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Url {
    private ArrayList<Token> tokenList;

    private boolean inGroup;

    public Url(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        this.inGroup = true;
        constructNormalizedTokenList();
    }
    private void constructNormalizedTokenList() {
        this.tokenList.sort(new Comparator<Token>() {
            @Override
            public int compare(Token o1, Token o2) {
                return Integer.compare(o2.getFrequency(), o1.getFrequency());
            }
        });
    }

    public int length() {
        return tokenList.size();
    }
}
