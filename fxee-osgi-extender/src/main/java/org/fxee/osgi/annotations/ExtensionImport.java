package org.fxee.osgi.annotations;


import java.lang.annotation.*;
import java.lang.annotation.Retention;

/**
 * Annotation is used to assign instance to variable during creating application context.
 * Can be used for constructor and variable in class
 * TODO - add element type constructor
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD, ElementType.PARAMETER})
public @interface ExtensionImport {
}
