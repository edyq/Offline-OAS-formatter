package org.ntu.apiconverter.common.method;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.ntu.apiconverter.entity.ApiDocEntry;

import java.util.List;

/*
*   strategies are associated in ApiDocEntryConstructionHandler
* */

public interface MethodTypeStrategy {

    JSONObject construct(Document content);
}

