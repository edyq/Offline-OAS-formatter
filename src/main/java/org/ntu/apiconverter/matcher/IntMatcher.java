package org.ntu.apiconverter.matcher;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Pattern;

public class IntMatcher implements PatternMatcher{
    @Override
    public boolean match(String input) {
        return Pattern.matches("([1-9]\\d*)",input);
    }

    @Override
    public JSONObject parse(JSONObject original) {
        original.put("type","integer");
        return original;
    }
}
