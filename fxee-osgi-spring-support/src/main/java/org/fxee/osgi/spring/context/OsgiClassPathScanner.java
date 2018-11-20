package org.fxee.osgi.spring.context;


import io.github.classgraph.ClassGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Osgi classpath scanner. See {@link ClassGraph}
 */
public class OsgiClassPathScanner {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiClassPathScanner.class);


    /**
     * Custom classloader. Either will be used classloader of current class
     */
    private ClassLoader classLoader;

    /**
     * Default count of threads for scanning classpath
     */
    private static final int DEFAULT_NUM_THREAD_SCAN = 2;

    /**
     * Specify number of threads for classpath scanner (default 2)
     */
    private int numThreadScan = DEFAULT_NUM_THREAD_SCAN;


    /**
     * Return new instance of {@link ClassGraph}
     * @return new instance
     */
    public ClassGraph createClassPathScanner() {
        return new ClassGraph()
                .addClassLoader(classLoader != null ? classLoader : this.getClass().getClassLoader())
                .enableAllInfo();
    }

    /**
     *  Scan all osgi components defined by array of base packages exclude web components
     *  such as {@link org.springframework.stereotype.Controller} or {@link org.springframework.web.bind.annotation.RestController}
     * @param basePackages - array of packages
     * @return - collection of scanned classes
     */
    public Collection<Class<?>> scanOsgiComponents(String[] basePackages) {

        Collection<Class<?>> result =  createClassPathScanner()
                .whitelistPackages(basePackages)
                .scan(numThreadScan)
                .getAllClasses()
                .filter(new OsgiNoneWebComponentFilter())
                .loadClasses();

        return result;
    }

    /**
     *  Scan all osgi components defined by array of base packages include web components
     *  such as {@link org.springframework.stereotype.Controller} or {@link org.springframework.web.bind.annotation.RestController}
     * @param basePackages - array of packages
     * @return - collection of scanned classes
     */
    public Collection<Class<?>> scanOsgiWebComponents(String[] basePackages) {

        return  createClassPathScanner()
                .whitelistPackages(basePackages)
                .scan(numThreadScan)
                .getAllClasses()
                .loadClasses();
    }

    /**
     * Return new instance of {@link ClassGraph}
     * @return new instance
     */
    protected ClassGraph createDebugClassPathScanner() {
        return new ClassGraph()
                .addClassLoader(classLoader != null ? classLoader : this.getClass().getClassLoader())
                .verbose()
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

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
