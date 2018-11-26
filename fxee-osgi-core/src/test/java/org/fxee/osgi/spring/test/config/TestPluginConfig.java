package org.fxee.osgi.spring.test.config;

import org.fxee.osgi.api.annotations.Plugin;
import org.fxee.osgi.api.annotations.annotations.ExposeAsService;
import org.fxee.osgi.api.annotations.annotations.OsgiComponentScan;
import org.fxee.osgi.spring.test.beans.DefaultExposeBean;
import org.springframework.context.annotation.Bean;


@Plugin(key = "test-module-key", name = "test-module-name")
@OsgiComponentScan(basePackages = {"org.fxee.osgi.spring.test.components"})
public class TestPluginConfig {

    @Bean
    @ExposeAsService
    public DefaultExposeBean exposeBean() {
        return new DefaultExposeBean();
    }
}
