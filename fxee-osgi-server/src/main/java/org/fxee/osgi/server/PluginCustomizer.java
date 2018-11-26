package org.fxee.osgi.server;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * DefaultPlugin customizer which care about each plugin and register each plugin module if are defined
 *
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/24/2018
 */
public class PluginCustomizer implements BundleTrackerCustomizer {

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent bundleEvent) {
        return null;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent bundleEvent, Object o) {
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent bundleEvent, Object o) {

    }
}
