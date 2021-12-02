package org.ntu.apiconverter.matcher;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatternMatcherUtil {

    private static List<PatternMatcher> patternMatcherChain = new ArrayList<>();

    static {
        patternMatcherChain.add(new FloatMatcher());
        patternMatcherChain.add(new IntMatcher());
        patternMatcherChain.add(new StringMatcher());
    }

    public static JSONObject matchPattern(String input, JSONObject original){
        for (PatternMatcher patternMatcher : patternMatcherChain){
            if (patternMatcher.match(input)){
                patternMatcher.parse(original);
                break;
            }
        }

        return original;
    }

}
