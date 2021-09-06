package org.ntu.apiconverter.common.write;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONArrayApiDocWriterComponent implements ApiDocWriterComponent{

    private Map<Class, ApiDocWriterComponent> writerComponent;

    public JSONArrayApiDocWriterComponent(JSONObjectApiDocWriterComponent jsonObjectApiDocWriterComponent, StringApiDocWriterComponent stringApiDocWriterComponent) {
        writerComponent = new HashMap<>();
        writerComponent.put(JSONObject.class, jsonObjectApiDocWriterComponent);
        writerComponent.put(String.class, stringApiDocWriterComponent);
    }
    //    @Override
//    public String format(JSONObject jsonObject, int level) {
//        StringBuilder sb = new StringBuilder();
//        indentation(level,sb);
//        sb.append("servers:");
//        newLine(sb);
//
//        for (jsonObject.)
//        indentation(level+1,sb);
//        sb.append("- ");
//        indentation(level+2,sb);
//
//
//    }

    @Override
    public String format(String key, Object object, int level, String padding) {
        JSONArray jsonArray = (JSONArray) object;
        StringBuilder sb = new StringBuilder();
        if (key != null){
            indentation(level,sb);
            sb.append(key+": ");
            newLine(sb);
            level+=1;
        }
        for (Object obj : jsonArray.toArray()){
            indentation(level, sb);
            sb.append(padding);
            sb.append("- ");
            newLine(sb);
            sb.append(writerComponent.get(obj.getClass()).format(null, obj, level, "  "));
        }

        return sb.toString();
    }
}
