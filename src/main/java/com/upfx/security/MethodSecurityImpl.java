package com.upfx.security;

import com.upfx.security.annotations.AbstractMethodSecurity;
import com.upfx.security.annotations.Handler;
import com.upfx.security.annotations.MethodSecurity;

import javax.annotation.Priority;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Interceptor @AbstractMethodSecurity(value = SecurityRule.class)
@Priority(Interceptor.Priority.APPLICATION)
public class MethodSecurityImpl extends SecurityImpl{

    private Instance<SecurityRule> securityRuleInstance;
    private Instance<SecurityHandler> handlerInstance;

    @Inject
    public MethodSecurityImpl(Instance<SecurityRule> securityRuleInstance, Instance<SecurityHandler> handlerInstance) {
        this.securityRuleInstance = securityRuleInstance;
        this.handlerInstance = handlerInstance;
    }

    @AroundInvoke
    public Object check(InvocationContext invocationContext) throws Exception {
        this.invocationContext = invocationContext;

        MethodSecurity annotation = invocationContext.getMethod().getAnnotation(MethodSecurity.class);

        if (annotation == null)
            throw new NotAMethodException("Annotated object is not a method");

        Class<SecurityRule> securityRuleClass = (Class<SecurityRule>) annotation.value();
        SecurityRule securityRule = securityRuleInstance.select(securityRuleClass).get();

        Handler securityHandler = securityRule.getClass().getAnnotation(Handler.class);
        Class<? extends SecurityHandler> handlerClass = securityHandler.value();
        SecurityHandler handler = handlerInstance.select(handlerClass).get();

        List<Object> parameterList = new ArrayList();
        parameterList.add(invocationContext.getMethod().getDeclaringClass());
        parameterList.addAll(Arrays.asList(invocationContext.getParameters()));

        Method isAllowedMethod = getMethodByName(securityRule.getClass(), "isAllowed");
        Boolean isAllowed = null;
        try{
            isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, invocationContext.getMethod());
        } catch (IllegalArgumentException e){}
        if (isAllowed==null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, parameterList.toArray());
            } catch (IllegalArgumentException e) {}
        }
        if (isAllowed==null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule);
            } catch (IllegalArgumentException e) {}
        }

        if (isAllowed) {
            Object proceed = invocationContext.proceed();
            return proceed;
        }
        handler.setSecurityImpl(this);
        handler.handle(invocationContext);
        return null;
    }

    @Override
    public void retry() {
        try {
            check(this.invocationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
