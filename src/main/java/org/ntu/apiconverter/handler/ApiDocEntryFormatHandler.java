package org.ntu.apiconverter.handler;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.common.method.GetStrategy;
import org.ntu.apiconverter.common.method.MethodTypeStrategy;
import org.ntu.apiconverter.common.method.PostStrategy;
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
        methodTypeStrategyMap.put("GET", new GetStrategy());
        methodTypeStrategyMap.put("POST", new PostStrategy());
        // tbd: delete and put
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
        apiDocEntry.getBody().put((String)document.get("method"), methodTypeStrategyMap.get(document.get("method")).construct(document));
        return arg;
    }
}
