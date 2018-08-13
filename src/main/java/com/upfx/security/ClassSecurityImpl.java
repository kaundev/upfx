package com.upfx.security;

import com.upfx.result.ClassRequest;
import com.upfx.result.RequestType;
import com.upfx.result.Result;
import com.upfx.security.annotations.ClassSecurity;
import com.upfx.security.annotations.Handler;

import javax.annotation.Priority;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Interceptor @ClassSecurity(value = SecurityRule.class)
@Priority(Interceptor.Priority.APPLICATION)
public class ClassSecurityImpl extends SecurityImpl{

    private Instance<SecurityRule> securityRuleInstance;
    private Instance<SecurityHandler> handlerInstance;
    private ClassRequest classRequest;
    private Result result;

    private ClassSecurityRequest classSecurityRequest;

    @Inject
    ClassSecurityImpl(Instance<SecurityRule> securityRuleInstance, Instance<SecurityHandler> handlerInstance, ClassRequest classRequest,
                        Result result) {
        this.securityRuleInstance = securityRuleInstance;
        this.handlerInstance = handlerInstance;
        this.classRequest = classRequest;
        this.result = result;
        classSecurityRequest = new ClassSecurityRequest(classRequest.clone());
    }

    @AroundConstruct
    public Object check(InvocationContext invocationContext) throws Exception {

        this.invocationContext = this.invocationContext == null ? invocationContext : this.invocationContext;

        ClassSecurity annotation = invocationContext.getConstructor().getDeclaringClass().getAnnotation(ClassSecurity.class);
        Class<SecurityRule> securityRuleClass = (Class<SecurityRule>) annotation.value();
        SecurityRule securityRule = securityRuleInstance.select(securityRuleClass).get();

        Handler securityHandler = securityRule.getClass().getAnnotation(Handler.class);
        if (securityHandler==null){
            throw new Exception("SecurityHandler not set in " + securityRule.getClass().getName());
        }
        Class<? extends SecurityHandler> handlerClass = securityHandler.value();
        SecurityHandler handler = handlerInstance.select(handlerClass).get();
        List<Object> parameterList = new ArrayList();
        parameterList.add(invocationContext.getConstructor().getDeclaringClass());
        //parameterList.add(classRequest.getPath());
        parameterList.addAll(Arrays.asList(classRequest.getParameters()));

        Method isAllowedMethod = getMethodByName(securityRule.getClass(), "isAllowed");
        Boolean isAllowed = null;

        try{
            isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, invocationContext.getConstructor().getDeclaringClass());
        } catch (IllegalArgumentException e){}
        if (isAllowed==null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, invocationContext.getConstructor().getDeclaringClass(), classRequest.getPath());
            } catch (IllegalArgumentException e) {}
        }
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

        isAllowed = isAllowed == null ? false : isAllowed;

        if (isAllowed) {
            return invocationContext.proceed();
        }
        handler.setSecurityImpl(this);
        handler.handle(invocationContext);
        return null;
    }

    @Override
    public void retry() throws Exception {
        if (classSecurityRequest.getClassRequest().getRequestType() == RequestType.RENDER)
            result.render(classSecurityRequest.getClassRequest().getPath(),
                    classSecurityRequest.getClassRequest().getTitle(),
                    classSecurityRequest.getClassRequest().getActualStage(),
                    classSecurityRequest.getClassRequest().getParameters());

        else if(classSecurityRequest.getClassRequest().getRequestType() == RequestType.OPEN_POPUP)
            result.openPopup(classSecurityRequest.getClassRequest().getPath(),
                    classSecurityRequest.getClassRequest().getTitle(),
                    classSecurityRequest.getClassRequest().getParameters());
    }

}
