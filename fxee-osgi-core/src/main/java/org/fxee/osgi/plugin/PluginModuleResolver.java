package org.fxee.osgi.plugin;

import org.osgi.framework.BundleContext;

/**
 * Interface to resolve plugin modules in plugin which implement these modules
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
public interface PluginModuleResolver {

    void resolve(Plugin plugin, BundleContext bundleContext);
}
