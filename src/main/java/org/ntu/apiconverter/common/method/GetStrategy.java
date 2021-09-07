package org.ntu.apiconverter.common.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.ntu.apiconverter.common.method.content.ContentParserStrategy;
import org.ntu.apiconverter.common.method.content.JSONContentParserStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GetStrategy implements MethodTypeStrategy{
    private Map<String, ContentParserStrategy> supportedResponseType;

    public GetStrategy(){
        supportedResponseType = new HashMap<>();
        supportedResponseType.put("application/json", new JSONContentParserStrategy());
//        supportedResponseType.add("application/x-www-form-urlencoded");
    }

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

        jsonObject.put("responses",formatResponses((Document)content.get("response")));
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

    protected JSONObject formatResponses(Document response){
        JSONObject jsonObject = new JSONObject();
        JSONObject responseContent = new JSONObject();
        jsonObject.put(String.valueOf(response.get("status_code")), responseContent);

        responseContent.put("description","response with code "+response.get("status_code"));

        JSONObject content = formatContent(response);

        if (content != null){
            responseContent.put("content", content);
        }

        return jsonObject;
    }

    protected JSONObject formatContent(Document response){
        if (supportedResponseType.containsKey(response.getString("content-type"))){
            return supportedResponseType.get(response.getString("content-type")).parse((Document) response.get("content"));
        }
        return null;
    }
}
