package org.fxee.osgi.api.annotations;

import java.lang.annotation.*;

/**
 * Annotation is used for custom scanning of current processors
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface OsgiComponentScan {

    String basePackage() default "";

    String[] basePackages() default "";
}
