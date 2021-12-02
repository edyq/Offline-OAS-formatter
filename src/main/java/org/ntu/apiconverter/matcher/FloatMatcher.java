package org.ntu.apiconverter.matcher;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Pattern;

public class FloatMatcher implements PatternMatcher{
    @Override
    public boolean match(String input) {
        return Pattern.matches("\\d+\\.\\d+",input);
    }

    @Override
    public JSONObject parse(JSONObject original) {
        original.put("type","number");
        original.put("format","float");
        return original;
    }
}
