package org.ntu.apiconverter.handler;

import org.ntu.apiconverter.handler.context.ApiDocHandlerContext;

public interface Handler {

    boolean supports(Object arg);
    default Object handle(ApiDocHandlerContext ctx, Object arg){
        if (arg == null || !supports(arg)){
            return null;
        }
        return doHandle(ctx, arg);

    }

    Object doHandle(ApiDocHandlerContext ctx, Object arg);
}
