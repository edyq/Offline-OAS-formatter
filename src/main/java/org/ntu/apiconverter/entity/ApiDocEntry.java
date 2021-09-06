package org.ntu.apiconverter.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.util.HashMap;
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

    public ApiDocEntry(String path){
        this.path = path;
        this.body = new HashMap<>();
    }

    public ApiDocEntry(){
        this(null);
    }

}
