package org.ntu.apiconverter.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiDocEntry {

    // uri
    private String path;

    // a (method, content) map
    private Map<String, JSONObject> body;


    public ApiDocEntry(String path){
        this.path = path;
        this.body = new HashMap<>();
    }

    public ApiDocEntry(){
        this(null);
    }
}
