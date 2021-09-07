package org.ntu.apiconverter.common.method;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;

public class PostStrategy extends GetStrategy{

    @Override
    public JSONObject construct(Document content) {
        JSONObject jsonObject = super.construct(content);

        JSONObject content_obj = super.formatContent(content);
        if (content != null){
            JSONObject requestBody = new JSONObject();
            jsonObject.put("requestBody",requestBody);
            requestBody.put("content",content_obj);
        }


    return jsonObject;
    }
}
