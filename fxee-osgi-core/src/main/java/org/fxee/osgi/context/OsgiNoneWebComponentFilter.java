package org.fxee.osgi.context;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementation of filter to just scan all components instead of web components
 * such as {@link Controller} and {@link RestController}
 */
public class OsgiNoneWebComponentFilter implements ClassInfoList.ClassInfoFilter {
    @Override
    public boolean accept(ClassInfo classInfo) {
        List<String> names = classInfo.getAnnotations().getNames();
        return classInfo.hasAnnotation(Controller.class.getName()) ||
               classInfo.hasAnnotation(RestController.class.getName()) ? Boolean.FALSE : Boolean.TRUE;
    }
}
