package org.fxee.osgi.spring.annotations;


import java.lang.annotation.*;

/**
 * Annotation is used to assign instance to variable during creating application context.
 * Can be used for constructor and variable in class
 * TODO - add element type constructor
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD, ElementType.PARAMETER})
public @interface ComponentImport {
}
