package org.ntu.apiconverter.worker;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.ntu.apiconverter.common.Callback;
import org.ntu.apiconverter.entity.ApiDoc;
import org.ntu.apiconverter.handler.Handler;
import org.ntu.apiconverter.handler.context.ApiDocHandlerContext;

import java.util.ArrayList;

@Data
public class HandlerChainWorker implements Callback, Worker{

    private ApiDocHandlerContext rootHandlerContext;
    private ApiDocHandlerContext tailHandlerContext;

    private ApiDoc apiDoc;

    public HandlerChainWorker(){
        configure();
    }

    public void configure(){
        rootHandlerContext = null;
        tailHandlerContext = null;
        // initialize header info for apiDoc
        apiDoc = new ApiDoc();

        apiDoc.setDefaultHeader();

    }

    @Override
    public void run(Object arg) {
        invokeHandlerChain(arg);
    }

    public void invokeHandlerChain(Object arg){
        if (rootHandlerContext == null ){
             return;
        }

        rootHandlerContext.handle(arg);

    }

    @Override
    public void execute(Object arg) {
        run(arg);
    }

    // tbc: change responsibility chain to internal mode
    // chained builder
    public static class HandlerChainWorkerBuilder{
        HandlerChainWorker handlerChainWorker;

        public HandlerChainWorkerBuilder(){
            handlerChainWorker = new HandlerChainWorker();
        }

        public HandlerChainWorkerBuilder addHandler(Handler handler){
            if (handlerChainWorker.rootHandlerContext == null){
                handlerChainWorker.rootHandlerContext = new ApiDocHandlerContext(handlerChainWorker.apiDoc, handler);
                handlerChainWorker.tailHandlerContext = handlerChainWorker.rootHandlerContext;
                return this;
            }


            handlerChainWorker.tailHandlerContext.setNext(new ApiDocHandlerContext(handlerChainWorker.apiDoc, handler));
            handlerChainWorker.tailHandlerContext = handlerChainWorker.tailHandlerContext.getNext();
            return this;
        }

        public HandlerChainWorker build(){
            return this.handlerChainWorker;
        }

    }
}
