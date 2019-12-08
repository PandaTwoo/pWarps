package br.com.pandatwo.pwarps;

import br.com.pandatwo.pwarps.commands.WarpCommand;
import br.com.pandatwo.pwarps.commands.WarpsCommand;
import br.com.pandatwo.pwarps.listeners.PlayersEvents;
import br.com.pandatwo.pwarps.listeners.WarpEvents;
import br.com.pandatwo.pwarps.modules.Modules;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class pWarps extends JavaPlugin {

    private static pWarps instance;

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners(new PlayersEvents(), new WarpEvents());
        Modules.getInstance().loadAllModules();
    }

    @Override
    public void onDisable() {
        Modules.getInstance().unloadAllModules();
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("warp").setExecutor(new WarpCommand());
        Bukkit.getPluginCommand("warps").setExecutor(new WarpsCommand());
    }

    private void registerListeners(Listener... listeners) {
        for(Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public static pWarps getInstance() {
        return instance;
    }
}
