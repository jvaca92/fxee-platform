package org.fxee.osgi.spring.test.config;

import org.fxee.osgi.spring.annotations.Module;
import org.fxee.osgi.spring.annotations.OsgiComponentScan;

@Module(key = "test-module-key", name = "test-module-name")
@OsgiComponentScan(basePackage = "org.fxee.osgi.spring.test.components")
public class TestModuleConfig {
}
