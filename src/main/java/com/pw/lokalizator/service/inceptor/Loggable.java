package com.pw.lokalizator.service.inceptor;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Target(value = {METHOD, TYPE})
@Retention(RUNTIME)
public @interface Loggable {

}
