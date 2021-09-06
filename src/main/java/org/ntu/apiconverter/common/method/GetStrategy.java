package org.ntu.apiconverter.common.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bson.Document;

import java.util.Map;

public class GetStrategy implements MethodTypeStrategy{
    @Override
    public JSONObject construct(Document content) {
        // get common ones: url param, path param(temporarily ignore)
        JSONObject jsonObject = new JSONObject();
        if (content.containsKey("urlParam")) {
            JSONArray parameters = new JSONArray();
            for (Map.Entry<String, Object> entry : ((Document)content.get("urlParam")).entrySet()){
                parameters.add(formatUrlParam(entry));
            }
            jsonObject.put("parameters",parameters);
            jsonObject.put("description","request with urlParam");
        } else{
            jsonObject.put("description","request without urlParam");
        }
        return jsonObject;
    }

    protected JSONObject formatUrlParam(Map.Entry<String, Object> entry){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("in","query");
        jsonObject.put("name",entry.getKey());
        JSONObject schema = new JSONObject();
        schema.put("type","string");
        schema.put("example",entry.getValue());
        jsonObject.put("schema",schema);

        return jsonObject;
    }
}
