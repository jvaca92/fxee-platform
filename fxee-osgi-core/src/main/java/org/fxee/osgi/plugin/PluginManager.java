package org.fxee.osgi.plugin;

/**
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/24/2018
 */
public interface PluginManager {

    void enablePluginModule(String moduleKey);

    void disablePluginModule(String moduleKey);

}
