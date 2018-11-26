package org.fxee.plugin.module.component.whiteboard;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
public class ComponentTracker extends ServiceTracker {

    public ComponentTracker(BundleContext context, Filter filter, ServiceTrackerCustomizer customizer) {
        super(context, filter, customizer);
    }

}
