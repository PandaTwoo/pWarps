package br.com.pandatwo.pwarps.modules.impl;

import br.com.pandatwo.pwarps.concurrent.AsyncTask;
import br.com.pandatwo.pwarps.entities.User;
import br.com.pandatwo.pwarps.entities.WarpTeleportRequest;
import br.com.pandatwo.pwarps.modules.Module;
import br.com.pandatwo.pwarps.modules.ModulePriority;
import br.com.pandatwo.pwarps.modules.Modules;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public class UsersModule implements Module {

    private Collection<User> users;
    private BukkitTask runnable;

    @Override
    public void load() {
        users = new LinkedHashSet<>();
        runnable = this.startTasker();
    }

    @Override
    public void unload() {
        users.clear();
        this.runnable.cancel();
    }

    @Override
    public void reload() {
        users.clear();
        runnable = startTasker();
        Bukkit.getOnlinePlayers().forEach(player -> users.add(new User(player)));
    }

    @Override
    public String getName() {
        return "Usuarios";
    }

    @Override
    public ModulePriority getPriority() {
        return ModulePriority.LOW;
    }

    public void registerUser(User user) {
        if (!existsUser(user.getUniqueId())) {
            users.add(user);
        }
    }

    public void unregisterUser(User user) {
        getUser(user.getUniqueId()).ifPresent(finded -> users.remove(finded));
    }

    public Optional<User> getUser(Player player) {
       return getUser(player.getUniqueId());
    }

    public Optional<User> getUser(UUID uuid) {
        User user = null;
        for (User users : users) {
            if (users.getUniqueId().equals(uuid)) {
                user = users;
                break;
            }
        }
        return Optional.ofNullable(user);
    }

    public Boolean existsUser(Player player) {
        return existsUser(player.getUniqueId());
    }

    public Boolean existsUser(UUID uuid) {
        return getUser(uuid).isPresent();
    }

    private BukkitTask startTasker() {
        return AsyncTask.repeatingTask(() -> users.stream().filter(User::hasWarpTeleportRequest).forEach(user -> {
            WarpTeleportRequest request = user.getWarpTeleportRequest();
            if (System.currentTimeMillis() >= request.getFinishDate()) {
                Player player = Bukkit.getPlayer(user.getUniqueId());
                player.teleport(request.getWarp().getLocation());
                player.sendMessage(MessagesModule.playerTeleportedSucessful.replace("%warp%", request.getWarp().getName()));
                user.setWarpTeleportRequest(null);
            }
        }), false, 20L);
    }


    public static Optional<UsersModule> getInstance() {
        return Modules.getInstance().getModule("Usuarios").filter(module -> module instanceof UsersModule).map(module -> (UsersModule) module);
    }
}
