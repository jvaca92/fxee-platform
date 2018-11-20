package org.fxee.osgi.spring.context;

import org.fxee.osgi.spring.annotations.ExposeAsService;
import org.fxee.osgi.spring.annotations.OsgiComponentScan;
import org.fxee.osgi.utils.OsgiUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.*;
import java.util.stream.Stream;

public  class OsgiAnnotationConfigWebApplicationContext extends AnnotationConfigWebApplicationContext implements Exposable {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiAnnotationConfigWebApplicationContext.class);


    /**
     * All registered osgi components as services
     */
    private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();

    /**
     * Config module class. Should be annotated by {@see Plugin }
     */
    private Class<?> pluginConfig;

    /**
     * Osgi bundle context
     */
    private BundleContext bundleContext;

    /**
     * Instance of osgi scanner
     */
    private OsgiClassPathScanner scanner;

    public OsgiAnnotationConfigWebApplicationContext(Class<?> pluginConfig) {
        this.scanner = new OsgiClassPathScanner();
        this.pluginConfig = pluginConfig;
        this.register(pluginConfig);
    }

    /***
     * Will be taken all defined base packages if exist. If not then will be taken all base packages from config module
     * class where should be defined base packages by {@see OsgiComponentScan}. If none of them exist then nothing will
     * be scanned
     * @param basePackages array of base packages
     */
    public void scanOsgiBasePackages(String[] basePackages) {
        LOG.debug("Starting scan base packages");
        if(basePackages != null && basePackages.length != 0) {
            doScanOsgiWebComponent(basePackages);
            LOG.debug("All packages were scanned and found classes were registered into application context");
        }
    }

    /**
     *  Will be taken module config class and then extract base packages defined by {@see OsgiComponentScan}
     *  if exist
     */
    protected void doScanAnnotatedOsgiComponent() {
        LOG.debug("Starting scan base packages defined by annotation @OsgiComponentScan");
        if(pluginConfig != null) {
            if(pluginConfig.isAnnotationPresent(OsgiComponentScan.class)) {
                LOG.debug("Was found annotation @OsgiComponentScan in module class {}", pluginConfig.getName());
                OsgiComponentScan osgiComponentScan = pluginConfig.getAnnotation(OsgiComponentScan.class);
                List<String> basePackages = new ArrayList<>(Arrays.asList(osgiComponentScan.basePackages()));
                Collections.addAll(basePackages, osgiComponentScan.basePackage());
                if(basePackages.size() != 0) {
                    doScanOsgiWebComponent(basePackages.toArray(new String[basePackages.size()]));
                }
                LOG.debug("Stopping scan base packages defined by annotation @OsgiComponentScan");
            }
        } else {
            LOG.error("Plugin config class is not defined");
            throw new NullPointerException("Plugin config class is not defined");
        }
    }

    /**
     * Will scan each base package by scanner
     * @param basePackages - array of base packages
     */
    protected void doScanOsgiWebComponent(String[] basePackages) {
        LOG.debug("Starting scan of base packages: {}", basePackages.toString());
        scanner.scanOsgiWebComponents(basePackages).forEach(
                        clazz -> {
                            register(clazz);
                            LOG.debug("The class {} was registered");
                        }
                );
        LOG.debug("Stopping scan of base packages: {}", basePackages.toString());
    }

    /**
     * Implementation of {@link OsgiComponentScan} to be sure that is invoked in right state
     */
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        doScanAnnotatedOsgiComponent();
        super.loadBeanDefinitions(beanFactory);
    }

    /***
     * Get all objects which are annotated by {@link Controller}
     * @return - collection of controllers
     */
    public Collection<Object> getControllers() {
        return getBeansWithAnnotation(Controller.class).values();
    }


    /***
     * Get all objects which are annotated by {@link org.springframework.web.bind.annotation.RestController}
     * @return - collection of rest controllers
     */
    public Collection<Object> getRestControllers() {
        return getBeansWithAnnotation(RestController.class ).values();
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
    public void exposeServices() {
        Stream.of(this.getBeanDefinitionNames()).forEach(beanDefinition -> {
            BeanDefinition definition = getBeanFactory().getBeanDefinition(beanDefinition);
            Object beanInstance = getBeanFactory().getBean(beanDefinition);
            if(beanInstance.getClass().isAnnotationPresent(ExposeAsService.class)) {
                OsgiUtils.doExposeComponentAsService(bundleContext, serviceRegistrations, beanInstance);
                LOG.debug("Expose component {} as service", beanInstance.getClass().getName());
            }
            /** Further process beans in config class if are marked by annotation @ExposeAsService **/
            else if (definition instanceof StandardMethodMetadata) {
                StandardMethodMetadata methodMetadata = (StandardMethodMetadata) definition.getSource();
                if (methodMetadata.isAnnotated(ExposeAsService.class.getCanonicalName())) {
                    OsgiUtils.doExposeBeanAsService(bundleContext, serviceRegistrations, methodMetadata, beanInstance);
                    LOG.debug("Expose component {} as service", beanInstance.getClass().getName());
                }
            }
        });
    }


    @Override
    public void disposeServices() {
        serviceRegistrations.forEach(serviceRegistration -> serviceRegistration.unregister());
    }

    /**
     *  Get all objects which are annotated by {@link Repository}
     * @return - collection of repositories
     */
    public Collection<Object> getRepositories() {
        return  getBeansWithAnnotation(Repository.class).values();
    }

    public Class<?> getPluginConfig() {
        return pluginConfig;
    }

    public void setPluginConfig(Class<?> pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public OsgiClassPathScanner getScanner() {
        return scanner;
    }

    public void setScanner(OsgiClassPathScanner scanner) {
        this.scanner = scanner;
    }

}
