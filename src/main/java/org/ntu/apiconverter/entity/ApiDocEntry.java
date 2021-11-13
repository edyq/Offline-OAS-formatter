package org.ntu.apiconverter.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * structure:
 *
 */
@Data
public class ApiDocEntry {

    // uri
    private String path;

    private Map<String, JSONObject> body;

    private List<String> methods;

    private List<String> responseCode;

    public ApiDocEntry(String path){
        this.path = path;
        this.body = new HashMap<>();
        this.methods = new ArrayList<>();
        this.responseCode = new ArrayList<>();
    }

    public ApiDocEntry(){
        this(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().isAssignableFrom(ApiDocEntry.class)) {
            return this.getPath().equals(((ApiDocEntry)obj).getPath());
        }

        if (obj.getClass().isAssignableFrom(String.class)) {
            return this.getPath().equals((String)obj);
        }


    }


}
