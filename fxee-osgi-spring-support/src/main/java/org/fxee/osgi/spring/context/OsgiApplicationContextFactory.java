package org.fxee.osgi.spring.context;


import org.fxee.osgi.plugin.PluginKeys;
import org.fxee.osgi.plugin.annotations.Plugin;
import org.osgi.framework.BundleContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Factory class to create new instance of {@see OsgiAnnotationConfigWebApplicationContext}
 */
public class OsgiApplicationContextFactory {


    public OsgiAnnotationConfigApplicationContext createApplicationContext(BundleContext bundleContext, Class<?> moduleConfigClass) {
        OsgiAnnotationConfigApplicationContext applicationContext = new OsgiAnnotationConfigApplicationContext(bundleContext, moduleConfigClass);
        registerBeanFactoryPostProcessors(bundleContext, applicationContext);
        return applicationContext;
    }

    public OsgiAnnotationConfigWebApplicationContext createWebApplicationContext(BundleContext bundleContext, Class<?> moduleConfigClass) {
        OsgiAnnotationConfigWebApplicationContext applicationContext = new OsgiAnnotationConfigWebApplicationContext(moduleConfigClass);
        registerBeanFactoryPostProcessors(bundleContext, applicationContext);
        return applicationContext;
    }


    public Dictionary createApplicationContextMetadata(Class<?> pluginConfigClass) {
        Dictionary metadata = createDictionary();
        Plugin pluginInfo = pluginConfigClass.getAnnotation(Plugin.class);
        metadata.put(PluginKeys.PLUGIN_ATTRIBUTE_KEY, !pluginInfo.key().isEmpty() ? pluginInfo.key() : UUID.randomUUID().toString());
        metadata.put(PluginKeys.PLUGIN_ATTRIBUTE_NAME, !pluginInfo.name().isEmpty() ? pluginInfo.name() : UUID.randomUUID().toString());
        return metadata;
    }

    protected void registerBeanFactoryPostProcessors(BundleContext bundleContext, ConfigurableApplicationContext applicationContext) {
       applicationContext.addBeanFactoryPostProcessor(new OsgiComponentBeanFactoryPostProcessor(bundleContext));
    }

    protected Dictionary createDictionary() {
        return new Hashtable<>();
    }







}
