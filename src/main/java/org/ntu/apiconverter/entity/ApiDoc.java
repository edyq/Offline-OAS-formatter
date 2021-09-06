package org.ntu.apiconverter.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * structure:
 *
 *      JSONObject header;
 *      ArrayList<JSONObject> apiDocEntries;
 *
 */
@Data
public class ApiDoc {
    // header info

    private JSONObject headerInfo;

    private List<ApiDocEntry> apiDocEntries;

    public ApiDoc(){
        this.setApiDocEntries(new ArrayList<>());
    }

    public void addNewApiDocEntry(ApiDocEntry apiDocEntry){
        apiDocEntries.add(apiDocEntry);
    }

    public void setDefaultHeader(){
        // set header Info
        JSONObject header = new JSONObject();
        // set version info
        header.put(
                "openapi", "3.0.0"
        );
        // set api server address
        JSONArray servers = new JSONArray();
        JSONObject server1 = new JSONObject();
        server1.put("url", "/");
        JSONObject server2 = new JSONObject();
        server2.put("url","localhost");
        servers.add(server1);
        servers.add(server2);
        header.put(
                "servers", servers
        );
        // set info field
        JSONObject info = new JSONObject();
        info.put(
                "version", "2.0.0"
        );
        info.put(
                "title","title"
        );
        info.put(
                "description","test api"
        );
        header.put("info",info);

        this.setHeaderInfo(header);
    }


}
