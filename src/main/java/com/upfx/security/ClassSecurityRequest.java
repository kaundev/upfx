package com.upfx.security;

import com.upfx.result.ClassRequest;

public class ClassSecurityRequest {

    private ClassRequest classRequest;

    public ClassSecurityRequest(ClassRequest classRequest) {
        this.classRequest = classRequest;
    }

    public ClassRequest getClassRequest() {
        return classRequest;
    }
}
