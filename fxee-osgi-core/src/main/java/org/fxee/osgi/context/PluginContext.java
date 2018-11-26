package org.fxee.osgi.context;

import org.osgi.framework.BundleContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
public interface PluginContext extends PluginContextLifecycle {


    String getUUID();

    BundleContext getBundleContext();

    Class<?> getPluginConfig();

    void setPluginConfig(Class<?> pluginConfig);

    ConfigurableApplicationContext getContext();

    void setContext(ConfigurableApplicationContext pluginContext);

    OsgiClassPathScanner getScanner();

    void scanOsgiBasePackages(String... basePackages);
}
