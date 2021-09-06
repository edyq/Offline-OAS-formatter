package org.ntu.apiconverter.common.write;

import com.alibaba.fastjson.JSONObject;

public interface ApiDocWriterComponent {
    String format(String key, Object object, int level, String padding);

    default void indentation(int level, StringBuilder sb){
        for (int i = 0; i < level; i++){
            sb.append(" ");
        }
    }

    default void newLine(StringBuilder sb){
        sb.append("\n");
    }

}
