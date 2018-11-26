package org.fxee.osgi.plugin;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.BundleTracker;

/**
 * Implementation of manager which care about all filtered plugins for fxee platform
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/24/2018
 */
public class DefaultPluginManager implements PluginManager {


    /**
     * Tracker of plugins which are related to Fxee platform
     */
    private BundleTracker bundleTracker;

    /**
     * Osgi bundle context
     */
    private BundleContext bundleContext;

    public DefaultPluginManager(BundleTracker bundleTracker) {
        this.bundleTracker = bundleTracker;
    }

    @Override
    public void enablePluginModule(String moduleKey) {
    }

    @Override
    public void disablePluginModule(String moduleKey) {

    }


}
