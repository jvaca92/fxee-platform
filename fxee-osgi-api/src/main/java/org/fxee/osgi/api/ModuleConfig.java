package org.fxee.osgi.plugin;

import java.util.List;

/**
 * Module config register for resources
 * @author Jan Vaca <jan.vaca92@gmail.com> on 12/2/2018
 */
public interface ModuleConfig {

    ModuleConfig register(Class<?> resource);

    void unregister(Class<?> resource);

    List<Class<?>> getAllResources();

}
