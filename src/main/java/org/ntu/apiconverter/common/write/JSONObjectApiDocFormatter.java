package org.ntu.apiconverter.common.write;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.xs.ItemPSVI;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObjectApiDocFormatter extends ApiDocFormatter{


    public JSONObjectApiDocFormatter(ApiDocFormatterMediator mediator){
        setApiDocFormatterMediator(mediator);
    }
    @Override
    public String format(String key, Object object, int level, String padding) {
        JSONObject jsonObject = (JSONObject) object;
        StringBuilder sb = new StringBuilder();

        if (key != null){
            indentation(level,sb);
            sb.append(padding);
            sb.append(key+": ");
            newLine(sb);
            level+=1;
        }

        for (String temp_key : jsonObject.keySet()){
            sb.append(getApiDocFormatterMediator().relay(jsonObject.get(temp_key).getClass(),new Object[]{temp_key, jsonObject.get(temp_key),level,padding}));
        }

        return sb.toString();
    }
}
