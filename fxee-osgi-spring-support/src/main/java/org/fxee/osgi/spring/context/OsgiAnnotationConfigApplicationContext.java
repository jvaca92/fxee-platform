package org.fxee.osgi.spring.context;

import org.fxee.osgi.spring.annotations.ExposeAsService;
import org.fxee.osgi.utils.OsgiUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;


public class OsgiAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext implements Exposable {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiAnnotationConfigApplicationContext.class);

    /**
     * All registered osgi components as services
     */
    private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();

    /**
     * Osgi bundle context
     */
    private BundleContext bundleContext;

    /**
     * Config module class. Should be annotated by {@see Plugin }
     */
    private Class<?> pluginConfig;

    /**
     * Instance of osgi scanner
     */
    private OsgiClassPathScanner scanner;

    public OsgiAnnotationConfigApplicationContext(BundleContext bundleContext) {
        this.scanner = createOsgiClassPathScanner();
        this.bundleContext = bundleContext;
        register(pluginConfig);
    }


    public OsgiAnnotationConfigApplicationContext(BundleContext bundleContext, Class<?> pluginConfig) {
        this.scanner = createOsgiClassPathScanner();
        this.pluginConfig = pluginConfig;
        this.bundleContext = bundleContext;
        register(pluginConfig);
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
            doScanOsgiComponents(basePackages);
            LOG.debug("All packages were scanned and found classes were registered into application context");
        }
    }

    /**
     * Will scan each base package by scanner
     * @param basePackages - array of base packages
     */
    protected void doScanOsgiComponents(String[] basePackages) {
        LOG.debug("Starting scan of base packages: {}", basePackages.toString());
        scanner.scanOsgiComponents(basePackages).forEach(
                clazz -> {
                    register(clazz);
                    LOG.debug("The class {} was registered");
                }
        );
        LOG.debug("Stopping scan of base packages: {}", basePackages.toString());
    }

    /**
     *  Get all objects which are annotated by {@link Repository}
     * @return - collection of repositories
     */
    public Collection<Object> getRepositories() {
        return  getBeansWithAnnotation(Repository.class).values();
    }

    @Override
    public void exposeServices() {
        LOG.debug("Starting exposing osgi components or beans as service");
        Stream.of(this.getBeanDefinitionNames()).forEach(beanDefinitionName -> {
            BeanDefinition beanDefinition = this.getBeanDefinition(beanDefinitionName);
            Object beanInstance = this.getBean(beanDefinitionName);
            /** First process class if is marked by annotation @ExposeAsService **/
            if(beanInstance.getClass().isAnnotationPresent(ExposeAsService.class)) {
                OsgiUtils.doExposeComponentAsService(bundleContext, serviceRegistrations, beanInstance);
                LOG.debug("Expose component {} as service", beanInstance.getClass().getName());
            }
            /** Further process beans in config class if are marked by annotation @ExposeAsService **/
            else if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                StandardMethodMetadata methodMetadata = (StandardMethodMetadata) beanDefinition.getSource();
                if (methodMetadata.isAnnotated(ExposeAsService.class.getCanonicalName())) {
                    OsgiUtils.doExposeBeanAsService(bundleContext, serviceRegistrations, methodMetadata, beanInstance);
                    LOG.debug("Expose component {} as service", beanInstance.getClass().getName());
                }
            }
        });
        LOG.debug("Stopping exposing osgi components or beans as service");
    }

    @Override
    public void disposeServices() {
        serviceRegistrations.forEach(serviceRegistration -> serviceRegistration.unregister());
    }

    @Override
    public void close() {
        disposeServices();
        super.close();
    }

    protected OsgiClassPathScanner createOsgiClassPathScanner() { return new OsgiClassPathScanner(); }

    protected void registerOsgiBeanFactoryPostProcessors() {
        this.addBeanFactoryPostProcessor(new OsgiComponentBeanFactoryPostProcessor(bundleContext));
    }

}
