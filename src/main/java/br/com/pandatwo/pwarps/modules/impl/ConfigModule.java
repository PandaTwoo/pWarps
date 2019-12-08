package br.com.pandatwo.pwarps.modules.impl;

import br.com.pandatwo.pwarps.entities.utilitaries.Config;
import br.com.pandatwo.pwarps.modules.Module;
import br.com.pandatwo.pwarps.modules.ModulePriority;

public class ConfigModule implements Module {

    public static boolean allowWarpsAliases = true;
    public static int defaultTeleportDelay = 3;

    private Config configFile = new Config("config.yml", true);

    @Override
    public void load() {
        loadConfiguration();
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        configFile.reload();
        allowWarpsAliases = configFile.getValue("Config.Allow_Aliases", true);
        String configTeleportDelay = configFile.getValue("Config.Teleport_Delay", "3");
        defaultTeleportDelay = isInt(configTeleportDelay) ? Integer.parseInt(configTeleportDelay) : 3;
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "Config";
    }

    @Override
    public ModulePriority getPriority() {
        return ModulePriority.HIGH;
    }
}