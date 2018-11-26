package org.fxee.osgi.plugin;

import org.fxee.osgi.context.PluginContext;
import org.osgi.framework.ServiceRegistration;

import java.util.List;

/**
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/21/2018
 */
public interface Plugin {

    String getKey();

    String getName();

    String getDesc();

    List<ServiceRegistration<PluginModule>> getPluginModules();

    PluginContext getPluginContext();

    void destroyPluginModules();

}
