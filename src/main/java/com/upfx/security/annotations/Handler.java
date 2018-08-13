package com.upfx.security.annotations;

import com.upfx.security.SecurityHandler;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface Handler {
    @Nonbinding Class<? extends SecurityHandler> value();
}
