package org.ntu.apiconverter.handler.context;

import lombok.Getter;
import lombok.Setter;
import org.ntu.apiconverter.entity.ApiDoc;
import org.ntu.apiconverter.handler.Handler;

@Getter
@Setter
public class ApiDocHandlerContext {

    private ApiDoc apiDoc;

    private ApiDocHandlerContext next;

    private Handler handler;

    public ApiDocHandlerContext(ApiDoc apiDoc, Handler handler){
        this.handler = handler;
        this.next = null;
        this.apiDoc = apiDoc;
    }


    public void handle(Object arg){
        Object res = handler.handle(this, arg);
        if (next != null){
            next.handle(res);
        }
    }
}

