package org.fxee.osgi.server;

import org.fxee.osgi.plugin.annotations.Plugin;
import org.fxee.osgi.spring.context.OsgiAnnotationConfigWebApplicationContext;
import org.fxee.osgi.spring.context.OsgiApplicationContextFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

/***
 * Bundle tracker to track correct bundle for Fxee platform
 */
public class OsgiModuleTracker extends BundleTracker {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiModuleTracker.class);


    public OsgiModuleTracker(BundleContext context, int stateMask, BundleTrackerCustomizer customizer) {
        super(context, stateMask, customizer);
    }

    /**
     * The attribute name for FXee module in manifest
     */
    private static final String MANIFEST_ATTRIBUTE_FXEE_MODULE_NAME = "Fxee-Plugin";

    /**
     * The attribute name for module config class in manifest
     */
    private static final String MANIFEST_ATTRIBUTE_MODULE_CONFIG_NAME = "Plugin-Configuration";


    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        LOG.debug("Starting register bundle as module {}", bundle.getSymbolicName());
        Boolean isEnabled = Boolean.getBoolean(bundle.getHeaders().get(MANIFEST_ATTRIBUTE_FXEE_MODULE_NAME));
        //Check if the bundle has manifest attribute to enable FXee module
        if(isEnabled) {
            String moduleConfigClassName = bundle.getHeaders().get(MANIFEST_ATTRIBUTE_MODULE_CONFIG_NAME);
            if(moduleConfigClassName != null && !moduleConfigClassName.isEmpty()) {
                Class<?> moduleConfigClass;
                try {
                    moduleConfigClass = bundle.loadClass(moduleConfigClassName);
                    LOG.debug("Plugin config class {} was loaded", moduleConfigClassName);
                    //Further check if class is annotated by annotation Plugin
                    if(moduleConfigClass.isAnnotationPresent(Plugin.class)) {
                        LOG.debug("Starting registering Osgi application context");
                        BundleContext bundleContext = bundle.getBundleContext();
                        OsgiApplicationContextFactory factory = new OsgiApplicationContextFactory();
                        OsgiAnnotationConfigWebApplicationContext applicationContext = factory.createApplicationContext(bundleContext, moduleConfigClass);
                        Dictionary applicationContextMetadata = factory.createApplicationContextMetadata(moduleConfigClass);
                        //Finally register as service
                        bundleContext.registerService(OsgiAnnotationConfigWebApplicationContext.class, applicationContext, applicationContextMetadata);
                        LOG.debug("Osgi application context was registered as service");
                    } else {
                        LOG.warn("The module config class {} cannot be processed because is not annotated by annotation Plugin", moduleConfigClassName);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("The module config class {} cannot be found: ", moduleConfigClassName, e);
                }
            } else {
                LOG.warn("The module {} has not specified module config class", bundle.getSymbolicName());
            }
        } else {
            LOG.info("The bundle {} is not specified as module for FXee platform", bundle.getSymbolicName());
        }
        return bundle;
    }

}
