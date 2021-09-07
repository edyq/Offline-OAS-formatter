package org.ntu.apiconverter.common.write;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ApiDocFormatterMediator {
    private Map<Class, ApiDocFormatter> apiDocFormatters;

    public ApiDocFormatterMediator(){
        apiDocFormatters = new HashMap<>();
    }

    public void registerApiDocFormatter(Class key, ApiDocFormatter formatter){
        apiDocFormatters.put(key, formatter);
    }

    public String relay(Class target, Object[] params){
        ApiDocFormatter apiDocFormatter = apiDocFormatters.get(target);
        if (apiDocFormatter != null && params.length == 4){
            return apiDocFormatter.format((String)params[0], params[1], (Integer)params[2], (String)params[3]);
        }

        return "";
    }

}
