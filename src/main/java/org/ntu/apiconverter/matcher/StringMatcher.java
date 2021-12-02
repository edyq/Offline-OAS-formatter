package org.ntu.apiconverter.matcher;

import com.alibaba.fastjson.JSONObject;

public class StringMatcher implements PatternMatcher{
    @Override
    public boolean match(String input) {
        return true;
    }

    @Override
    public JSONObject parse(JSONObject original) {
        original.put("type","string");
        return original;
    }
}
