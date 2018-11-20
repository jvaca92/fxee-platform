package org.fxee.osgi.utils;

import org.fxee.osgi.spring.context.ComponentKeys;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.springframework.core.type.StandardMethodMetadata;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class OsgiUtils {

    public static boolean existOsgiComponent(BundleContext bundleContext, Class<?> classComponent) {
        return bundleContext.getServiceReference(classComponent) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Object findOsgiComponent(BundleContext bundleContext, Class<?> classComponent) {
        ServiceReference<?> componentReference = bundleContext.getServiceReference(classComponent);
        return bundleContext.getService(componentReference);
    }

    public static Dictionary createOsgiComponentServiceMetadata(Class<?> clazz) {
        Dictionary metadata = new Hashtable();
        metadata.put(ComponentKeys.OSGI_COMPONENT_ATTRIBUTE_KEY, clazz.getName());
        metadata.put(ComponentKeys.OSGI_COMPONENT_ATTRIBUTE_NAME, clazz.getSimpleName());
        return metadata;
    }

    public static void doExposeComponentAsService(BundleContext context, List<ServiceRegistration> registrations, Object beanInstance) {
        Dictionary matadata =  createOsgiComponentServiceMetadata(beanInstance.getClass());
        ServiceRegistration registration = context.registerService(beanInstance.getClass().getName(), beanInstance, matadata);
        registrations.add(registration);
    }

    public static void doExposeBeanAsService(BundleContext context, List<ServiceRegistration> registrations, StandardMethodMetadata methodMetadata, Object beanInstance) {
        String className = methodMetadata.getIntrospectedMethod().getReturnType().getCanonicalName();
        Dictionary matadata =  createOsgiComponentServiceMetadata(beanInstance.getClass());
        ServiceRegistration registration = context.registerService(className, beanInstance, matadata);
        registrations.add(registration);
    }

}
