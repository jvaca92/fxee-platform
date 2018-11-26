package org.fxee.osgi.context;

import org.osgi.framework.BundleContext;

/**
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/26/2018
 */
public interface PluginContextFactory {

    AnnotationPluginContext createAnnotationPluginContext(BundleContext bundleContext, Class<?> pluginConfig);

    AnnotationWebPluginContext createAnnotationWebPluginContext(BundleContext bundleContext, Class<?> pluginConfig);
}
