package org.ntu.apiconverter.matcher;

import com.alibaba.fastjson.JSONObject;

public interface PatternMatcher {
    boolean match(String input);
    JSONObject parse(JSONObject original);
}
