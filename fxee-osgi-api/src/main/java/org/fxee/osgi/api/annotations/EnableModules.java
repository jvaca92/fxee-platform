package org.fxee.osgi.api.annotations;

import java.lang.annotation.*;

/**
 * Define which all plugin modules would be enabled.
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface EnableModules{
    Class<?>[] modules();
}
