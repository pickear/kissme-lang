package com.kissme.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author loudyn
 * 
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface ThreadSafe {}
