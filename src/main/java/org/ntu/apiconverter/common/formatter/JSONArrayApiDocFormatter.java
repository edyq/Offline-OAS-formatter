package org.ntu.apiconverter.common.formatter;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;

public class JSONArrayApiDocFormatter extends ApiDocFormatter{

    private Map<Class, ApiDocFormatter> writerComponent;

    public JSONArrayApiDocFormatter(ApiDocFormatterMediator mediator) {
        setApiDocFormatterMediator(mediator);
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
            sb.append(getApiDocFormatterMediator().relay(obj.getClass(),new Object[]{ null, obj, level, "  " }));
        }

        return sb.toString();
    }
}
