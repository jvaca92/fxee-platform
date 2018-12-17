package org.fxee.osgi.api.annotations;


import java.lang.annotation.*;

/**
 * Identify if would be current service (component) register into OSGi service container
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= {ElementType.METHOD, ElementType.TYPE})
public @interface ExposeAsService {
}
