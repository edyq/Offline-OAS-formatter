package org.ntu.apiconverter.handler;
import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.entity.ApiDoc;
import org.ntu.apiconverter.entity.ApiDocEntry;
import org.ntu.apiconverter.handler.context.ApiDocHandlerContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * this handler checks if Document already in ApiDoc
 */
public class DuplicationDetectionHandler implements Handler{

    @Override
    public boolean supports(Object arg) {
        return arg != null && arg.getClass().isAssignableFrom(Document.class);
    }

    @Override
    public Object doHandle(ApiDocHandlerContext ctx, Object arg) {
        Document document = (Document) arg;
        ApiDoc apiDoc = ctx.getApiDoc();
        // create apiDocEntry with path and url
        if (apiDoc.getApiDocEntries().size() == 0 ){
            ApiDocEntry apiDocEntry = new ApiDocEntry((String)document.get("path"));
            apiDoc.addNewApiDocEntry(apiDocEntry);
            return arg;
        }

        // checks if path exists
        List<ApiDocEntry> apiDocEntries = apiDoc.getApiDocEntries();
        for (ApiDocEntry apiDocEntry : apiDocEntries){
            if (apiDocEntry.getPath().equals(document.get("path"))){
                // if path exists and method, return null
                // tbd: refactor
                if (apiDocEntry.getBody().keySet().contains(((String)document.get("method")))) {
                    JSONObject methodObj = apiDocEntry.getBody().get((String)document.get("method"));
                    if (!methodObj.containsKey("responses") || !methodObj.getJSONObject("responses").containsKey(((Document)document.get("response")).get("status_code"))){
                        return arg;
                    }
                    return null;
                } else {
                    return arg;
                }
            }
        }
        ApiDocEntry temp_apiDocEntry = new ApiDocEntry((String)document.get("path"));
        apiDoc.addNewApiDocEntry(temp_apiDocEntry);
        return arg;
    }


}
