package br.com.pandatwo.pwarps.modules.impl;

import br.com.pandatwo.pwarps.entities.Warp;
import br.com.pandatwo.pwarps.entities.utilitaries.Config;
import br.com.pandatwo.pwarps.events.WarpLoadEvent;
import br.com.pandatwo.pwarps.modules.Module;
import br.com.pandatwo.pwarps.modules.ModulePriority;
import br.com.pandatwo.pwarps.modules.Modules;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class WarpsModule implements Module {

    private Collection<Warp> warps;
    private Config warpsFile = new Config("warps.yml", true);

    @Override
    public void load() {
        warps = new ArrayList<>();
        loadWarps();
    }

    @Override
    public void unload() {
        warps.clear();
    }

    @Override
    public void reload() {
        warps.clear();
        loadWarps();
    }

    @Override
    public String getName() {
        return "warps";
    }

    private void loadWarps() {
        warpsFile.reload();
        ConfigurationSection warpsSection = warpsFile.getConfigurationSection("Warps");
        if (warpsSection == null) {
            return;
        }

        for (String warpPatch : warpsSection.getKeys(false)) {
            try {

                ConfigurationSection warpSection = warpsFile.getConfigurationSection("Warps." + warpPatch);
                Location warpDestiny = warpsFile.getLocation(warpSection);

                Warp warp = new Warp(warpPatch, warpDestiny);

                WarpLoadEvent warpLoadEvent = new WarpLoadEvent(warp);
                Bukkit.getPluginManager().callEvent(warpLoadEvent);

                if (warpLoadEvent.isCancelled()) {
                    continue;
                }

                warps.add(warpLoadEvent.getWarp());

            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnLoadingWarp.replace("%name%", warpPatch));
                e.printStackTrace();
            }
        }
    }

    public boolean registerWarp(Warp warp) {
        if (!getWarp(warp.getName()).isPresent()) {
            warpsFile.setLocation("Warps." + warp.getName(), warp.getLocation());
            warpsFile.save();
            warps.add(warp);
            return true;
        }
        return false;
    }

    public boolean unregisterWarp(Warp warp) {
        Optional<Warp> findedWarp = getWarp(warp.getName());
        if (findedWarp.isPresent()) {
            warpsFile.set("Warps." + warp.getName(), null);
            warpsFile.save();
            warps.remove(findedWarp.get());
            return true;
        }
        return false;
    }

    public Optional<Warp> getWarp(String warpName) {
        Warp warp = null;
        for (Warp warps : warps) {
            if (warps.getName().equalsIgnoreCase(warpName)) {
                warp = warps;
                break;
            }
        }
        return Optional.ofNullable(warp);
    }

    public Boolean existsWarp(String warpName) {
        return getWarp(warpName).isPresent();
    }

    public Collection<Warp> getWarps() {
        return warps;
    }

    @Override
    public ModulePriority getPriority() {
        return ModulePriority.MEDIUM;
    }

    public static Optional<WarpsModule> getInstance() {
        return Modules.getInstance().getModule("Warps").filter(module -> module instanceof WarpsModule).map(module -> (WarpsModule) module);
    }

}
