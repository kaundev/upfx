package com.upfx.security;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

public abstract class SecurityImpl {

    protected InvocationContext invocationContext;

    public abstract Object check(InvocationContext invocationContext) throws Exception;

    public abstract void retry() throws Exception;

    protected Method getMethodByName(Class clazz, String methodName){
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    public InvocationContext getInvocationContext() {
        return invocationContext;
    }
}
