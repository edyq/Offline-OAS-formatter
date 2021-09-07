package org.ntu.apiconverter.common.write;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObjectApiDocWriterComponent implements ApiDocWriterComponent{

    private Map<Class, ApiDocWriterComponent> writerComponent;

    public JSONObjectApiDocWriterComponent(){
        StringApiDocWriterComponent stringApiDocWriterComponent = new StringApiDocWriterComponent();
        JSONArrayApiDocWriterComponent jsonArrayApiDocWriterComponent = new JSONArrayApiDocWriterComponent(this, stringApiDocWriterComponent);
        writerComponent = new HashMap<>();
        writerComponent.put(String.class, stringApiDocWriterComponent);
        writerComponent.put(JSONArray.class, jsonArrayApiDocWriterComponent);

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
            ApiDocWriterComponent apiDocWriterComponent = null;
            apiDocWriterComponent = writerComponent.get(jsonObject.get(temp_key).getClass());
            if (apiDocWriterComponent != null){
                sb.append(apiDocWriterComponent.format(temp_key, jsonObject.get(temp_key), level, padding));
                continue;
            }
            if (jsonObject.get(temp_key).getClass().isAssignableFrom(JSONObject.class)){
                sb.append(this.format(temp_key, jsonObject.get(temp_key), level, padding));
            }
        }

        return sb.toString();
    }
}
