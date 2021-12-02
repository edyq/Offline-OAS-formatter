package org.ntu.apiconverter.common.method.content;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.matcher.PatternMatcherUtil;

public class JSONContentParserStrategy extends ContentParserStrategy {
    @Override
    public JSONObject parse(Document document) {
        JSONObject res = new JSONObject();
        JSONObject json = new JSONObject();
        json.put("schema",recursivelyParseObjectType(document));
        res.put("application/json",json);

        return res;
    }

    public JSONObject recursivelyParseObjectType(Document document){
        JSONObject res = new JSONObject();
        res.put("type","object");
        JSONObject properties = new JSONObject();
        res.put("properties", properties);
        for (String key : document.keySet()){
            if (document.get(key).getClass().isAssignableFrom(Document.class)){
                properties.put(key, recursivelyParseObjectType((Document) document.get(key)));
                continue;
            }
            else {
                JSONObject property = new JSONObject();
                property.put("example",document.getString(key));
                if (super.getSpecialChar().indexOf(key.charAt(0)) != -1) {
                    key = '"' + key + '"';
                }
                property.put("example",document.get(key));
                property = PatternMatcherUtil.matchPattern((String)document.get(key),property);
                properties.put(key,property);
            }

        }
        return res;
    }
}
