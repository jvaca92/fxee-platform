package org.fxee.plugin.module.rest.api.annotations;

import java.lang.annotation.*;

/**
 * Mark class as rest module and provide some basic info about the module.
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/24/2018
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= ElementType.METHOD)
public @interface RestModule {

    String key();

    String name();

    String path();
}
