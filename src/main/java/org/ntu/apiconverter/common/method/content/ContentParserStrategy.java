package org.ntu.apiconverter.common.method.content;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ContentParserStrategy {
    private List<Character> specialChar;

    public ContentParserStrategy(){
        specialChar = new ArrayList<>();
        specialChar.add('#');
        specialChar.add('!');
        specialChar.add('@');
        specialChar.add('%');
        specialChar.add('&');
        specialChar.add('*');
    }

    public abstract JSONObject parse(Document document);
}
