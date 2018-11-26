package org.fxee.osgi.context;

/**
 * DefaultPlugin context lifecycle base on lifecycle of application context {@link org.springframework.context.ApplicationContext}
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/21/2018
 */
public interface PluginContextLifecycle {

    void start();

    void stop();

    void refresh();
}
