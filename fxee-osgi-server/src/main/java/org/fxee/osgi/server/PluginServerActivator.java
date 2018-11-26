package org.fxee.osgi.server;


import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *  Main bundle activator of FXee server
 * @author Jan Vaca <jan.vaca92@gmail.com>
 */
public class PluginServerActivator implements BundleActivator {

    /**
     * Tracker of Fxee plugins
     */
    private PluginTracker pluginTracker;



    @Override
    public void start(BundleContext bundleContext) throws Exception {
        pluginTracker = getOrCreatePluginTracker(bundleContext);
        pluginTracker.open();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        pluginTracker.close();
    }


    protected PluginTracker getOrCreatePluginTracker(BundleContext bundleContext) {
        return pluginTracker != null ? pluginTracker : new PluginTracker(bundleContext, Bundle.ACTIVE, null);
    }


}
