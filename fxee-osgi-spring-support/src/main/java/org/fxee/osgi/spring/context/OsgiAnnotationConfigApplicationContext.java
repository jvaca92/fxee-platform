package org.fxee.osgi.spring.context;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.stream.IntStream;

public class OsgiAnnotationConfigApplicationContext extends AnnotationConfigWebApplicationContext {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiAnnotationConfigApplicationContext.class);

    /**
     * Default count of threads for scanning classpath
     */
    private static final int DEFAULT_NUM_THREAD_SCAN = 2;

    private int numThreadScan = DEFAULT_NUM_THREAD_SCAN;



    /***
     * Custom scanning of current classpath and then register matched annotated classes
     * @param basePackages
     */
    @Override
    public void scan(String... basePackages) {

        LOG.debug("Starting scanning base packages");
        ClassGraph classpathScanner = LOG.isDebugEnabled() ? createDebugClassPathScanner() : createClassPathScanner();
        if(basePackages.length != 0) {
            doScan(classpathScanner, basePackages);
            LOG.debug("All packages were scanned and found classes registered into application context");
        } else {

            LOG.info("No base packages defined as parameters. Starting scan packages from annotation scanner");
        }
    }

    protected void doScan(ClassGraph classpathScanner, String[] basePackages) {
        IntStream.range(0, basePackages.length)
                .forEach(basePackage -> {
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
                });
    }

    protected String[] getOsgiComponentScanPackages() {
        ///TODO
    };


    public int getNumThreadScan() {
        return numThreadScan;
    }

    public void setNumThreadScan(int numThreadScan) {
        this.numThreadScan = numThreadScan;
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
                .addClassLoader(this.getClass().getClassLoader())
                .enableAllInfo();
    }


}
