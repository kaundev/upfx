package com.upfx.security;

import javax.interceptor.InvocationContext;

public abstract class SecurityHandler {
    private SecurityImpl securityImpl;

    public abstract void handle(InvocationContext invocationContext);

    public SecurityImpl getSecurityImpl() {
        return securityImpl;
    }

    public void setSecurityImpl(SecurityImpl securityImpl) {
        this.securityImpl = securityImpl;
    }
}
