package org.fxee.osgi.context;

import org.fxee.osgi.api.annotations.ExposeAsService;
import org.fxee.osgi.api.annotations.OsgiComponentScan;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/21/2018
 */

public class AbstractPluginContext implements PluginContext, Exposable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPluginContext.class);

    /**
     * All registered osgi components as services
     */
    protected List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();

    /**
     * Osgi bundle context
     */
    protected BundleContext bundleContext;

    /**
     * Instance of osgi scanner
     */
    protected OsgiClassPathScanner scanner;

    /**
     * Config module class. Should be annotated by {@see DefaultPlugin }
     */
    protected Class<?> pluginConfig;

    /**
     * DefaultPlugin context as application context
     */
    protected ConfigurableApplicationContext pluginContext;

    /**
     * Cached base packages as set
     */
    protected Set<String> basePackages = new HashSet<>();

    public AbstractPluginContext(BundleContext bundleContext, ConfigurableApplicationContext pluginContext, Class<?> pluginConfig) {
        this.pluginContext = pluginContext;
        this.bundleContext = bundleContext;
        this.pluginConfig = pluginConfig;
        this.scanner = createOsgiClassPathScanner();
        registerOsgiBeanFactoryPostProcessors();
        registerOsgiComponentScanBasePackages();
    }

    protected OsgiClassPathScanner createOsgiClassPathScanner() { return new OsgiClassPathScanner(); }


    /***
     * Will be taken all defined base packages if exist. If not then will be taken all base packages from config module
     * class where should be defined base packages by {@see OsgiComponentScan}. If none of them exist then nothing will
     * be scanned
     * @param basePackages array of base packages
     */
    public void scanOsgiBasePackages(String... basePackages) {
        Collections.addAll(this.basePackages, basePackages);
    }

    @Override
    public void exposeServices() {
        Stream.of(pluginContext.getBeanFactory().getBeanDefinitionNames())
                .forEach(definitionName -> {
                    BeanDefinition beanDefinition = pluginContext.getBeanFactory().getBeanDefinition(definitionName);
                    Object beanInstance = pluginContext.getBean(definitionName);
                    /** First process class if is marked by annotation @ExposeAsService **/
                    if(beanInstance.getClass().isAnnotationPresent(ExposeAsService.class)) {
                        doExposeComponentAsService(beanInstance);
                        LOG.debug("Expose component {} as service", beanInstance.getClass().getName());
                    }
                    /** Further process beans in config class if are marked by annotation @ExposeAsService **/
                    if (beanDefinition instanceof AnnotatedBeanDefinition) {
                        StandardMethodMetadata metadata = (StandardMethodMetadata) ((AnnotatedBeanDefinition)beanDefinition).getFactoryMethodMetadata();
                        if (metadata != null && metadata.isAnnotated(ExposeAsService.class.getCanonicalName())) {
                            String serviceClassName = metadata.getIntrospectedMethod().getReturnType().getCanonicalName();
                            doExposeBeanAsService(serviceClassName, beanInstance);
                            LOG.debug("Expose bean {} as service", beanInstance.getClass().getName());
                        }
                    }
                });
    }

    @Override
    public void disposeServices() {
        serviceRegistrations.forEach(serviceRegistration -> serviceRegistration.unregister());
    }

    @Override
    public void start() {
        pluginContext.start();
    }

    @Override
    public void stop() {
        disposeServices();
        pluginContext.stop();
    }

    @Override
    public void refresh() {
        pluginContext.refresh();
    }

    protected void registerOsgiBeanFactoryPostProcessors() {
        pluginContext.addBeanFactoryPostProcessor(new OsgiComponentBeanFactoryPostProcessor(bundleContext));
    }

    protected void doExposeComponentAsService(Object beanInstance) {
        Dictionary metadata =  createOsgiComponentServiceMetadata(beanInstance.getClass());
        ServiceRegistration registration = bundleContext.registerService(beanInstance.getClass().getName(), beanInstance, metadata);
        serviceRegistrations.add(registration);
    }

    protected void doExposeBeanAsService(String className, Object beanInstance) {
        Dictionary metadata =  createOsgiComponentServiceMetadata(beanInstance.getClass());
        ServiceRegistration registration = bundleContext.registerService(className, beanInstance, metadata);
        serviceRegistrations.add(registration);
    }

    protected Dictionary createOsgiComponentServiceMetadata(Class<?> clazz) {
        Dictionary metadata = new Hashtable();
        metadata.put(ComponentKeys.OSGI_COMPONENT_ATTRIBUTE_KEY, clazz.getName());
        metadata.put(ComponentKeys.OSGI_COMPONENT_ATTRIBUTE_NAME, clazz.getSimpleName());
        return metadata;
    }

    public <T> T getComponent(Class<?> component) {
        return (T) pluginContext.getBeanFactory().getBean(component);
    }

    /**
     *  Will be taken module config class and then extract base packages defined by {@see OsgiComponentScan}
     *  if exist
     */
    protected void registerOsgiComponentScanBasePackages() {
        LOG.debug("Starting register base packages defined by annotation @OsgiComponentScan");
        if(pluginConfig != null) {
            if(pluginConfig.isAnnotationPresent(OsgiComponentScan.class)) {
                LOG.debug("Was found annotation @OsgiComponentScan in module class {}", pluginConfig.getName());
                OsgiComponentScan osgiComponentScan = pluginConfig.getAnnotation(OsgiComponentScan.class);

                if(!osgiComponentScan.basePackage().isEmpty()){
                    this.basePackages.add(osgiComponentScan.basePackage());
                }
                //Checking default value
                if(!osgiComponentScan.basePackages()[0].isEmpty()) {
                    this.basePackages.addAll(new HashSet<>(Arrays.asList(osgiComponentScan.basePackages())));
                }
                LOG.debug("Stopping scan base packages defined by annotation @OsgiComponentScan");
            }
        } else {
            LOG.error("DefaultPlugin config class is not defined");
            throw new NullPointerException("DefaultPlugin config class is not defined");
        }
    }

    @Override
    public BundleContext getBundleContext() {
        return null;
    }

    @Override
    public Class<?> getPluginConfig() {
        return null;
    }

    @Override
    public ConfigurableApplicationContext getContext() {
        return pluginContext;
    }

    @Override
    public void setContext(ConfigurableApplicationContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    @Override
    public OsgiClassPathScanner getScanner() {
        return null;
    }

    /**
     *  Get all objects which are annotated by {@link Repository}
     * @return - collection of repositories
     */
    public Collection<Object> getRepositories() {
        return  pluginContext.getBeanFactory().getBeansWithAnnotation(Repository.class).values();
    }
}
