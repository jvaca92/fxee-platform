package org.fxee.osgi.plugin;

/**
 * Enum of plugin module state
 * @author Jan Vaca <jan.vaca92@gmail.com> on 12/2/2018
 */
public enum PluginModuleState {
    ENABLED(true),
    DISABLED(false);

    private boolean value;

    PluginModuleState(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

}
