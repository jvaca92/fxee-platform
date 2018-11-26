package org.fxee.osgi.utils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class OsgiUtils {

    public static boolean existOsgiComponent(BundleContext bundleContext, Class<?> classComponent) {
        return bundleContext.getServiceReference(classComponent) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Object findOsgiComponent(BundleContext bundleContext, Class<?> classComponent) {
        ServiceReference<?> componentReference = bundleContext.getServiceReference(classComponent);
        return bundleContext.getService(componentReference);
    }

}
