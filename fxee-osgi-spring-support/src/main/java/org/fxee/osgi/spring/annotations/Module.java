package org.fxee.osgi.spring.annotations;


import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * Main annotation mark class as config class of module. Play important role during creating new module bundle
 * Also play the same role as {@link Configuration} where can be defined bean class by {@link org.springframework.context.annotation.Bean} in configuration class
 */
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
@Configuration
public @interface Module {

    String name();

    String key();

}
