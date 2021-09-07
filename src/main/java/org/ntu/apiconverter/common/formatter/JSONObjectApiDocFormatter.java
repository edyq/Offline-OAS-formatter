package org.ntu.apiconverter.common.formatter;

import com.alibaba.fastjson.JSONObject;

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
