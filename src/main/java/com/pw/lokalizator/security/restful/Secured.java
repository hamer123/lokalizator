package com.pw.lokalizator.security.restful;
import com.pw.lokalizator.model.enums.Roles;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by wereckip on 18.08.2016.
 */

/** Provide Authentication*/
@NameBinding
@Retention(RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Secured {
    /** Provide Authorization*/
    Roles[] value() default {};
}
