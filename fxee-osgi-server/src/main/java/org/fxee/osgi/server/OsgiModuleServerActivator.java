package org.fxee.osgi.server;


import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *  Main bundle activator of FXee server
 * @author Jan Vaca <jan.vaca92@gmail.com>
 */
public class OsgiModuleServerActivator implements BundleActivator {

    private OsgiModuleTracker osgiModuleTracker;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        osgiModuleTracker = getOrCreateOsgiModuleTracker(bundleContext);
        osgiModuleTracker.open();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        osgiModuleTracker.close();
    }


    protected OsgiModuleTracker getOrCreateOsgiModuleTracker(BundleContext bundleContext) {
        return osgiModuleTracker != null ? osgiModuleTracker : new OsgiModuleTracker(bundleContext, Bundle.ACTIVE, null);
    }


}
