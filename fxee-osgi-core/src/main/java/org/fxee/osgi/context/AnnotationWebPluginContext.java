package org.fxee.osgi.context;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Collection;
import java.util.Collections;


public class AnnotationWebPluginContext extends AbstractPluginContext {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationWebPluginContext.class);

    public AnnotationWebPluginContext(BundleContext bundleContext, ConfigurableApplicationContext pluginContext, Class<?> pluginConfig) {
        super(bundleContext, pluginContext, pluginConfig);
    }

    /**
     * Will scan each base package by scanner
     * @param basePackages - array of base packages
     */
    protected void doScanOsgiWebComponent(String[] basePackages) {
        LOG.debug("Starting scan of base packages: {}", basePackages.toString());
        scanner.scanOsgiWebComponents(basePackages).forEach(
                        clazz -> {
                            registerComponent(clazz);
                            LOG.debug("The class {} was registered");
                        }
                );
        LOG.debug("Stopping scan of base packages: {}", basePackages.toString());
    }

    protected void registerComponent(Class<?> clazz) {
        ((AnnotationConfigWebApplicationContext) pluginContext).register(clazz);
    }

    /***
     * Get all objects which are annotated by {@link Controller}
     * @return - collection of controllers
     */
    public Collection<Object> getControllers() {
        return pluginContext.getBeanFactory().getBeansWithAnnotation(Controller.class).values();
    }


    /***
     * Get all objects which are annotated by {@link org.springframework.web.bind.annotation.RestController}
     * @return - collection of rest controllers
     */
    public Collection<Object> getRestControllers() {
        return pluginContext.getBeanFactory().getBeansWithAnnotation(RestController.class ).values();
    }


    /***
     * Get all objects which are annotated by {@link Controller} or {@link RestController}
     * @return - collection of controllers
     */
    public Collection<Object> getAllControllers() {
        Collection controllers = getControllers();
        Collections.addAll(controllers, getRestControllers());
        return controllers;
    }

    @Override
    public void refresh() {
        registerComponent(pluginConfig);
        doScanOsgiWebComponent(basePackages.toArray(new String[basePackages.size()]));
        super.refresh();
    }
}
