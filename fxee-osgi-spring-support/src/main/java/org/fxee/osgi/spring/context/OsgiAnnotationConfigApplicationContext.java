package org.fxee.osgi.spring.context;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.fxee.osgi.spring.annotations.OsgiComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class OsgiAnnotationConfigApplicationContext extends AnnotationConfigWebApplicationContext {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiAnnotationConfigApplicationContext.class);

    /**
     * Default count of threads for scanning classpath
     */
    private static final int DEFAULT_NUM_THREAD_SCAN = 2;

    private int numThreadScan = DEFAULT_NUM_THREAD_SCAN;

    /**
     * Config module class. Should be annotated by {@see Module }
     */
    private Class<?> moduleConfig;


    public OsgiAnnotationConfigApplicationContext(Class<?> moduleConfig) {
        this.moduleConfig = moduleConfig;
    }

    /***
     * Will be taken all defined base packages if exist. If not then will be taken all base packages from config module
     * class where should be defined base packages by {@see OsgiComponentScan}. If none of them exist then nothing will
     * be scanned
     * @param basePackages array of base packages
     */
    public void scanOsgiBasePackages(String[] basePackages) {
        LOG.debug("Starting scanning base packages");
        if(basePackages!= null && basePackages.length != 0) {
            doScanOsgiComponent(getClassPathScanner(), basePackages);
            LOG.debug("All packages were scanned and found classes were registered into application context");
        }
    }

    /**
     *  Will be taken module config class and then extract base packages defined by {@see OsgiComponentScan}
     *  if exist
     * @param classpathScanner - classpath scanner
     */
    protected void doScanAnnotatedOsgiComponent(ClassGraph classpathScanner) {
        LOG.debug("Starting scan base pacakges defined by annotation @OsgiComponentScan");
        if(moduleConfig != null) {
            if(moduleConfig.isAnnotationPresent(OsgiComponentScan.class)) {
                LOG.debug("Was found annotation @OsgiComponentScan in module class {}", moduleConfig.getName());
                OsgiComponentScan osgiComponentScan = moduleConfig.getAnnotation(OsgiComponentScan.class);
                Collections.addAll(Arrays.asList(osgiComponentScan.basePackages(), osgiComponentScan.basePackage()));   //Join all together
                if(osgiComponentScan.basePackages().length != 0) {
                    doScanOsgiComponent(classpathScanner, osgiComponentScan.basePackages());
                }
                LOG.debug("Stopping scan base pacakges defined by annotation @OsgiComponentScan");
            }
        } else {
            LOG.error("Module config class is not defined");
            throw new NullPointerException("Module config class is not defined");
        }

    }

    /**
     * Will scann each base package by scanner
     * @param classpathScanner - classpath scanner
     * @param basePackages - array of base packages
     */
    protected void doScanOsgiComponent(ClassGraph classpathScanner, String[] basePackages) {
        IntStream.range(0, basePackages.length)
                .forEach(basePackage -> {
                    LOG.debug("Start scan base package with name {}", basePackage);
                    classpathScanner.whitelistPackages(basePackages)
                            .scan(numThreadScan)
                            .getAllClasses()
                            .getStandardClasses()
                            .loadClasses()
                            .forEach(
                                    clazz -> {
                                        register(clazz);
                                        LOG.debug("The class {} was registered");
                                    }
                            );
                    LOG.debug("Stop scan base package with name {}", basePackage);
                });
    }


    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        doScanAnnotatedOsgiComponent(getClassPathScanner());
        super.loadBeanDefinitions(beanFactory);
    }

    /**
     * Return new instance of {@link ClassGraph}
     * @return new instance
     */
    protected ClassGraph createClassPathScanner() {
        return new ClassGraph()
                .addClassLoader(this.getClass().getClassLoader())
                .enableAllInfo();
    }

    /**
     * Return new instance of {@link ClassGraph} with debug mode
     * @return new instance
     */
    protected ClassGraph createDebugClassPathScanner() {
        return new ClassGraph()
                .verbose()
                .addClassLoader(getClassLoader() != null ? getClassLoader() : getClass().getClassLoader())
                .enableAllInfo();
    }

    protected ClassGraph getClassPathScanner() {
        return LOG.isDebugEnabled() ? createDebugClassPathScanner() : createClassPathScanner();
    }

    public int getNumThreadScan() {
        return numThreadScan;
    }

    public void setNumThreadScan(int numThreadScan) {
        this.numThreadScan = numThreadScan;
    }

    public Class<?> getModuleConfig() {
        return moduleConfig;
    }

    public void setModuleConfig(Class<?> moduleConfig) {
        this.moduleConfig = moduleConfig;
    }
}
