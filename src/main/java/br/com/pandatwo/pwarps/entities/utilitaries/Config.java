package br.com.pandatwo.pwarps.entities.utilitaries;

import br.com.pandatwo.pwarps.pWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

@SuppressWarnings("unchecked")
public class Config {

    /*
        NÃO É RECOMENDADO USAR ESSA CLASSE, ELA NÃO FOI REVISADA NO REWORKING!
        ELA POSSUI ALGUMAS (MUITAS) COISAS PARA MELHORAR!
     */

    private File root;
    private FileConfiguration delegate;

    public Config(String name) {
        this(name, false);
    }

    public Config(String name, Boolean isGivenAsResource) {
        Plugin plugin = pWarps.getInstance();
        root = new File(plugin.getDataFolder(), name);
        if (!root.exists()) {
            if (isGivenAsResource)
                plugin.saveResource(name, false);
            else
                root.mkdirs();
        }
        delegate = YamlConfiguration.loadConfiguration(root);
    }

    public void save() {
        try {
            delegate.save(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        delegate = YamlConfiguration.loadConfiguration(root);
    }

    public Config set(String key, Object value) {
        delegate.set(key, value);
        return this;
    }

    public Location getLocation(String patch) {
        return getLocation(getConfigurationSection(patch));
    }

    public Location getLocation(ConfigurationSection section) {
        String world = section.getString("World");
        double x = section.getDouble("X");
        double y = section.getDouble("Y");
        double z = section.getDouble("Z");
        float pitch = Float.parseFloat(section.getString("Pitch"));
        float yaw = Float.parseFloat(section.getString("Yaw"));
        return new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }

    public void setLocation(String local, Location location) {
        delegate.set(local + ".World", location.getWorld().getName());
        delegate.set(local + ".X", location.getX());
        delegate.set(local + ".Y", location.getY());
        delegate.set(local + ".Z", location.getZ());
        delegate.set(local + ".Pitch", location.getPitch());
        delegate.set(local + ".Yaw", location.getYaw());
    }


    public ConfigurationSection getConfigurationSection(String section) {
        return delegate.getConfigurationSection(section);
    }

    public <T> T getValue(String key) {
        return (T) delegate.get(key);
    }

    public <T> T getValue(String key, T def) {
        return (T) delegate.get(key, def);
    }

}
