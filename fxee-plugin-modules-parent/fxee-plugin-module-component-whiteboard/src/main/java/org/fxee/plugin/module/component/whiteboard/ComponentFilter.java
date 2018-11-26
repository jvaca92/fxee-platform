package org.fxee.plugin.module.component.whiteboard;

import org.fxee.osgi.plugin.PluginModuleKeys;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;

import java.util.Dictionary;
import java.util.Map;

/**
 * Implementation of filter which is responsible to filter correct services
 * which are then tracked by component tracker
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
public class ComponentFilter implements Filter {

    @Override
    public boolean match(ServiceReference<?> serviceReference) {
        return false;
    }

    @Override
    public boolean match(Dictionary<String, ?> dictionary) {
        return dictionary.get(PluginModuleKeys.PLUGIN_MODULE_TYPE).equals(ComponentModuleKeys.PLUGIN_MODULE_COMPONENT_TYPE) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public boolean matchCase(Dictionary<String, ?> dictionary) {
        return  dictionary.get(PluginModuleKeys.PLUGIN_MODULE_TYPE).equals(ComponentModuleKeys.PLUGIN_MODULE_COMPONENT_TYPE) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public boolean matches(Map<String, ?> map) {
        return false;
    }
}
