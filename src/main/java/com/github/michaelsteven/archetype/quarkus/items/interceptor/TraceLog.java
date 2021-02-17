package com.github.michaelsteven.archetype.quarkus.items.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Target( {ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
public @interface TraceLog {
}
