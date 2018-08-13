package com.upfx.security;


class SecurityChecker {
/*
    private Instance<SecurityRule> securityRuleInstance;
    private Instance<SecurityHandler> handlerInstance;
    private ClassRequest classRequest;

    @Inject
    SecurityChecker(Instance<SecurityRule> securityRuleInstance, Instance<SecurityHandler> handlerInstance, ClassRequest classRequest) {
        this.securityRuleInstance = securityRuleInstance;
        this.handlerInstance = handlerInstance;
        this.classRequest = classRequest;
    }

    Object checkClass(InvocationContext invocationContext) throws Exception {

        ClassSecurity annotation = invocationContext.getConstructor().getDeclaringClass().getAnnotation(ClassSecurity.class);
        Class<SecurityRule> securityRuleClass = (Class<SecurityRule>) annotation.value();
        SecurityRule securityRule = securityRuleInstance.select(securityRuleClass).get();

        Handler securityHandler = securityRule.getClass().getAnnotation(Handler.class);
        Class<? extends SecurityHandler> handlerClass = securityHandler.value();
        SecurityHandler handler = handlerInstance.select(handlerClass).get();

        List<Object> parameterList = new ArrayList();
        parameterList.add(invocationContext.getConstructor().getDeclaringClass());
        parameterList.add(classRequest.getPath());
        parameterList.addAll(Arrays.asList(classRequest.getParameters()));

        Method isAllowedMethod = getMethodByName(securityRule.getClass(), "isAllowed");
        Boolean isAllowed = null;

        try{
            isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, invocationContext.getConstructor().getDeclaringClass(), invocationContext);
        } catch (IllegalArgumentException e){}
        if (isAllowed!=null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, invocationContext.getConstructor().getDeclaringClass(), classRequest.getPath());
            } catch (IllegalArgumentException e) {}
        }
        if (isAllowed!=null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, parameterList.toArray());
            } catch (IllegalArgumentException e) {}
        }
        if (isAllowed!=null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule);
            } catch (IllegalArgumentException e) {}
        }

        isAllowed = isAllowed == null ? false : isAllowed;
        if (isAllowed) {
            return invocationContext.proceed();
        }
        return handler.handle(invocationContext);
    }

    Object checkMethod(InvocationContext invocationContext) throws Exception {
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
        if (isAllowed!=null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule, parameterList.toArray());
            } catch (IllegalArgumentException e) {
            }
        }
        if (isAllowed!=null) {
            try {
                isAllowed = (Boolean) isAllowedMethod.invoke(securityRule);
            } catch (IllegalArgumentException e) {}
        }

        if (isAllowed == true) {
            return invocationContext.proceed();
        }
        handler.handle(invocationContext);
        return null;

    }

    private Method getMethodByName(Class clazz, String methodName){
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }
*/
}
