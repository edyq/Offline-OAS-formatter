package org.ntu.apiconverter.common.method.content;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.matcher.PatternMatcherUtil;

public class WwwFormUrlencodedContentParserStrategy extends ContentParserStrategy{
    @Override
    public JSONObject parse(Document document) {
        JSONObject res = new JSONObject();
        JSONObject json = new JSONObject();
        json.put("schema",parseParams(document));
        res.put("application/x-www-form-urlencoded",json);
        return res;
    }


    private JSONObject parseParams(Document document) {
        JSONObject res = new JSONObject();
        res.put("type","object");
        JSONObject properties = new JSONObject();
        res.put("properties", properties);
        for (String key : document.keySet()){
            JSONObject property = new JSONObject();
            property.put("example",document.getString(key));
            if (super.getSpecialChar().indexOf(key.charAt(0)) != -1) {
                key = '"' + key + '"';
            }

            property.put("example",document.get(key));
            property = PatternMatcherUtil.matchPattern((String)document.get(key),property);
            properties.put(key,property);
        }

        return res;
    }
}
