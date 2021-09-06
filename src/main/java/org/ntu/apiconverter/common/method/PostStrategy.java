package org.ntu.apiconverter.common.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.entity.ApiDocEntry;

import java.util.List;

public class PostStrategy extends GetStrategy{

    @Override
    public JSONObject construct(Document content) {
        JSONObject jsonObject = super.construct(content);



        return jsonObject;
    }
}
