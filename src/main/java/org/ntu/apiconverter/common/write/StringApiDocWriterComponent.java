package org.ntu.apiconverter.common.write;

import com.alibaba.fastjson.JSONObject;

/**
 *
 */
public class StringApiDocWriterComponent implements ApiDocWriterComponent{
    @Override
    public String format(String key, Object object, int level, String padding) {
        if (!object.getClass().isAssignableFrom(String.class)){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        indentation(level, sb);
        sb.append(padding);
        sb.append(key + ": "+(String) object);
        newLine(sb);

        return sb.toString();

    }
}
