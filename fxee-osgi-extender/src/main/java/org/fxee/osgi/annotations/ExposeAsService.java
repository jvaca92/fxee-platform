package org.fxee.osgi.annotations;


import java.lang.annotation.*;

@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= {ElementType.METHOD, ElementType.TYPE})
public @interface ExposeAsService {
}
