package org.fxee.osgi.spring.context;


import org.fxee.osgi.spring.annotations.Module;
import org.osgi.framework.BundleContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Factory class to create new instance of {@see OsgiAnnotationConfigApplicationContext}
 */
public class OsgiAnnotationConfigApplicationContextFactory {


    public OsgiAnnotationConfigApplicationContext createApplicationContext(BundleContext bundleContext, Class<?> moduleConfigClass) {
        OsgiAnnotationConfigApplicationContext applicationContext = new OsgiAnnotationConfigApplicationContext(moduleConfigClass);
        registerBeanFactoryPostProcessors(bundleContext, applicationContext);
        applicationContext.refresh();
        return applicationContext;
    }


    public Dictionary createApplicationContextMetadata(Class<?> moduleConfigClass) {
        Dictionary metadata = createDictionary();
        Module moduleInfo = moduleConfigClass.getAnnotation(Module.class);
        metadata.put(OsgiAnnotationConfigApplicationContext.MODULE_KEY, !moduleInfo.key().isEmpty() ? moduleInfo.key() : UUID.randomUUID().toString());
        metadata.put(OsgiAnnotationConfigApplicationContext.MODULE_NAME, !moduleInfo.name().isEmpty() ? moduleInfo.name() : UUID.randomUUID().toString());
        return metadata;
    }

    protected void registerBeanFactoryPostProcessors(BundleContext bundleContext, AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new OsgiComponentBeanFactoryPostProcessor(bundleContext));
        applicationContext.addBeanFactoryPostProcessor(new OsgiExposeServiceBeanFactoryPostProcessor(bundleContext));
    }

    protected Dictionary createDictionary() {
        return new Hashtable<>();
    }







}
