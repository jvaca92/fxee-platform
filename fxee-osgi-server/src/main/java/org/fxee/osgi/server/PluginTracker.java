package org.fxee.osgi.server;

import org.fxee.osgi.api.annotations.Plugin;
import org.fxee.osgi.context.AnnotationPluginContext;
import org.fxee.osgi.context.AnnotationWebPluginContext;
import org.fxee.osgi.context.PluginContextFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Bundle tracker to track correct bundle for FxEE platform
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/24/2018
 */
public class PluginTracker extends BundleTracker implements PluginContextFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PluginTracker.class);

    public PluginTracker(BundleContext context, int stateMask, BundleTrackerCustomizer customizer) {
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
        //Check if the bundle has manifest attribute to enable FxEE module
        if(isEnabled) {
            String moduleConfigClassName = bundle.getHeaders().get(MANIFEST_ATTRIBUTE_MODULE_CONFIG_NAME);
            if(moduleConfigClassName != null && !moduleConfigClassName.isEmpty()) {
                Class<?> moduleConfigClass;
                try {
                    moduleConfigClass = bundle.loadClass(moduleConfigClassName);
                    LOG.debug("Plugin config class {} was loaded", moduleConfigClassName);
                    //Further check if class is annotated by annotation DefaultPlugin
                    if(moduleConfigClass.isAnnotationPresent(Plugin.class)) {
                        LOG.debug("Starting registering plugin modules");
                        BundleContext bundleContext = bundle.getBundleContext();


                        LOG.debug("Osgi application context was registered as service");
                    } else {
                        LOG.warn("The module config class {} cannot be processed because is not annotated by annotation plugin", moduleConfigClassName);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("The module config class {} cannot be found: ", moduleConfigClassName, e);
                }
            } else {
                LOG.warn("The module {} has not specified module config class", bundle.getSymbolicName());
            }
        } else {
            LOG.info("The bundle {} is not specified as module for FxEE platform", bundle.getSymbolicName());
        }
        return bundle;
    }



    @Override
    public AnnotationPluginContext createAnnotationPluginContext(BundleContext bundleContext, Class<?> pluginConfig) {
        return null;
    }

    @Override
    public AnnotationWebPluginContext createAnnotationWebPluginContext(BundleContext bundleContext, Class<?> pluginConfig) {
        return null;
    }
}
