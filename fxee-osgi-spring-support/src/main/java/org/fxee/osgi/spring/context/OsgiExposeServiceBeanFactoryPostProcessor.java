package org.fxee.osgi.spring.context;

import org.osgi.framework.BundleContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class OsgiExposeServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    private final BundleContext bundleContext;

    public OsgiExposeServiceBeanFactoryPostProcessor(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
