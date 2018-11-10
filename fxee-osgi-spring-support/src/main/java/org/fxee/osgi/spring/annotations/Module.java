package org.fxee.osgi.spring.annotations;


import java.lang.annotation.*;
import java.util.UUID;

/**
 * Main annotation mark class as config class of module. Play important role during creating new module bundle
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface Module {

    String name();

}
