package org.ntu.apiconverter.common.write;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ApiDocFormatter {
    private ApiDocFormatterMediator apiDocFormatterMediator;

    public abstract String format(String key, Object object, int level, String padding);

    public void indentation(int level, StringBuilder sb){
        for (int i = 0; i < level; i++){
            sb.append(" ");
        }
    }

    public void newLine(StringBuilder sb){
        sb.append("\n");
    }

}
