package org.fxee.osgi.context;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/21/2018
 */
public class AnnotationPluginContext extends AbstractPluginContext {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationPluginContext.class);

    public AnnotationPluginContext(BundleContext bundleContext, ConfigurableApplicationContext pluginContext, Class<?> pluginConfig) {
        super(bundleContext, pluginContext, pluginConfig);
     }

    /**
     * Will scan each base package by scanner
     * @param basePackages - array of base packages
     */
    protected void doScanOsgiComponents(String[] basePackages) {
        LOG.debug("Starting scan of base packages: {}", basePackages.toString());
        scanner.scanOsgiComponents(basePackages).forEach(
                clazz -> {
                    registerComponent(clazz);
                    LOG.debug("The class {} was registered");
                }
        );
        LOG.debug("Stopping scan of base packages: {}", basePackages.toString());
    }

    protected void registerComponent(Class<?> clazz) {
        ((AnnotationConfigApplicationContext) pluginContext).register(clazz);
    }

    @Override
    public void refresh() {
        registerComponent(pluginConfig);
        doScanOsgiComponents(basePackages.toArray(new String[basePackages.size()]));
        super.refresh();
    }
}
