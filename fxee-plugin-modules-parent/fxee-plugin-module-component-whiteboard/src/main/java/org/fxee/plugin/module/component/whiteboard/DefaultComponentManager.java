package org.fxee.plugin.module.component.whiteboard;

import org.fxee.plugin.module.component.api.ComponentManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
public class DefaultComponentManager implements ComponentManager {

    private ServiceTracker serviceTracker;

    public DefaultComponentManager(ServiceTracker serviceTracker) {
        this.serviceTracker = serviceTracker;
    }




}
