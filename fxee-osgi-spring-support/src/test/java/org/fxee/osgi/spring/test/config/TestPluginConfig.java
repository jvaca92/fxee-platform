package org.fxee.osgi.spring.test.config;

import org.fxee.osgi.plugin.annotations.Plugin;
import org.fxee.osgi.spring.annotations.OsgiComponentScan;

@Plugin(key = "test-module-key", name = "test-module-name")
@OsgiComponentScan(basePackage = "org.fxee.osgi.spring.test.components")
public class TestPluginConfig {
}
