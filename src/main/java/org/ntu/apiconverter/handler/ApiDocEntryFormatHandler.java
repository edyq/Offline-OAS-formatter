package org.ntu.apiconverter.handler;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.common.method.GetStrategy;
import org.ntu.apiconverter.common.method.MethodTypeStrategy;
import org.ntu.apiconverter.common.method.PostStrategy;
import org.ntu.apiconverter.entity.ApiDoc;
import org.ntu.apiconverter.entity.ApiDocEntry;
import org.ntu.apiconverter.handler.context.ApiDocHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiDocEntryFormatHandler implements Handler{

    private Map<String, MethodTypeStrategy> methodTypeStrategyMap;

    public ApiDocEntryFormatHandler(){
        initStrategyMap();
    }

    public void initStrategyMap(){
        methodTypeStrategyMap = new HashMap<>();
        GetStrategy getStrategy = new GetStrategy();
        PostStrategy postStrategy = new PostStrategy();
        methodTypeStrategyMap.put("GET", getStrategy);
        methodTypeStrategyMap.put("POST", postStrategy);
        methodTypeStrategyMap.put("DELETE", getStrategy);
        methodTypeStrategyMap.put("PUT", postStrategy);
        methodTypeStrategyMap.put("PATCH",postStrategy);
    }

    @Override
    public boolean supports(Object arg) {
        return arg.getClass().isAssignableFrom(Document.class);
    }

    //tbd: refactor
    @Override
    public Object doHandle(ApiDocHandlerContext ctx, Object arg) {
        // assume that the document can be added already(no duplication)
        Document document = (Document) arg;

        ApiDoc apiDoc = ctx.getApiDoc();

        ApiDocEntry apiDocEntry = null;
        // find the target entry
        List<ApiDocEntry> entries = ctx.getApiDoc().getApiDocEntries();
        for (ApiDocEntry entry : entries){
            if (entry.getPath().equals(document.get("path"))){
                apiDocEntry = entry;
                break;
            }
        }


        // double check
        if (apiDocEntry == null){
            return null;
        }

        // insert method
        apiDocEntry.getBody().put(document.getString("method"), insertBody(apiDocEntry, document));

        return arg;
    }

    public JSONObject insertBody(ApiDocEntry apiDocEntry, Document document){

        // check for existing methods
        for (String method : apiDocEntry.getBody().keySet()){
            // method exist, add response code
            if (method.equals(document.getString("method"))){
                return methodTypeStrategyMap.get(method).addResponseStatusCode(apiDocEntry.getBody().get(method),document);
            }
        }

        // method not exist, add method
        return methodTypeStrategyMap.get(document.get("method")).construct(document);
    }
}
