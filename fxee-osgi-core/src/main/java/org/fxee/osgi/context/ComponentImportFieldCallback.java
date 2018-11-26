package org.fxee.osgi.context;

import org.fxee.osgi.api.annotations.annotations.ComponentImport;
import org.fxee.osgi.utils.OsgiUtils;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

public class ComponentImportFieldCallback implements ReflectionUtils.FieldCallback {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentImportFieldCallback.class);

    private BundleContext bundleContext;
    private Object beanInstance;
    private Collection<String> constructorOsgiComponents;

    public ComponentImportFieldCallback(BundleContext bundleContext, Object beanInstance, Collection<String> constructorOsgiComponents) {
        this.bundleContext = bundleContext;
        this.beanInstance = beanInstance;
        this.constructorOsgiComponents = constructorOsgiComponents;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        LOG.debug("Starting field callback {}", getClass().getName());

        if(!field.isAnnotationPresent(ComponentImport.class) && !constructorOsgiComponents.contains(field.getType().getName())) {
            LOG.debug("The field {} is not annotated by @ComponentImport annotation", field.getName());
            return;
        }
        ReflectionUtils.makeAccessible(field);
        if(OsgiUtils.existOsgiComponent(bundleContext, field.getType())) {
            LOG.debug("Was found service reference of type {}", field.getType().getName());
            Object instanceComponent = OsgiUtils.findOsgiComponent(bundleContext, field.getType());
            ReflectionUtils.setField(field, beanInstance, instanceComponent);
            LOG.debug("Instance component {} was successfully imported", instanceComponent.getClass().getName());
        } else {
            LOG.warn("The component cannot be found for instance bean {} in bundle context", field.getType().getName());
        }
    }
}
