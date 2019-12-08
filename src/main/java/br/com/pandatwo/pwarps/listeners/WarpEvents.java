package br.com.pandatwo.pwarps.listeners;

import br.com.pandatwo.pwarps.entities.Warp;
import br.com.pandatwo.pwarps.entities.WarpTeleportRequest;
import br.com.pandatwo.pwarps.events.PlayerTeleportWarpEvent;
import br.com.pandatwo.pwarps.events.WarpCreatedEvent;
import br.com.pandatwo.pwarps.events.WarpDeleteEvent;
import br.com.pandatwo.pwarps.modules.impl.ConfigModule;
import br.com.pandatwo.pwarps.modules.impl.MessagesModule;
import br.com.pandatwo.pwarps.modules.impl.UsersModule;
import br.com.pandatwo.pwarps.modules.impl.WarpsModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WarpEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWarpTeleport(PlayerTeleportWarpEvent event) {
        Player player = event.getPlayer();
        Warp warp = event.getWarp();
        int delay = event.getDelay();

        Optional<UsersModule> usersModuleOptional = UsersModule.getInstance();
        if (!usersModuleOptional.isPresent()) {
            Bukkit.broadcastMessage(MessagesModule.errorOnModule.replace("%module%", "USERS"));
            return;
        }

        UsersModule module = usersModuleOptional.get();
        module.getUser(player).ifPresent(
                user -> {
                    user.setWarpTeleportRequest(new WarpTeleportRequest(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delay), warp));
                    if (event.getDelay() > 0)
                        player.sendMessage(MessagesModule.playerStartedTeleport.replace("%warp%", warp.getName()).replace("%delay%", Integer.toString(delay)));
                });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWarpCreatedEvent(WarpCreatedEvent event) {
        Player player = event.getPlayer();
        Warp warp = event.getWarp();

        Optional<WarpsModule> warpsModuleOptional = WarpsModule.getInstance();
        if (!warpsModuleOptional.isPresent()) {
            player.sendMessage(MessagesModule.errorOnExecutingCommand);
            Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnModule.replace("%module%", "WARPS"));
            return;
        }

        warpsModuleOptional.get().registerWarp(warp);
        player.sendMessage(MessagesModule.warpCreated.replace("%warp%", warp.getName()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWarpDeleted(WarpDeleteEvent event) {
        CommandSender player = event.getCommandSender();
        Warp warp = event.getWarp();

        Optional<WarpsModule> warpsModuleOptional = WarpsModule.getInstance();
        if (!warpsModuleOptional.isPresent()) {
            player.sendMessage(MessagesModule.errorOnExecutingCommand);
            Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnModule.replace("%module%", "WARPS"));
            return;
        }

        warpsModuleOptional.get().unregisterWarp(warp);
        player.sendMessage(MessagesModule.warpDeleted.replace("%warp%", warp.getName()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWarpCommand(PlayerCommandPreprocessEvent event) {
        if (!ConfigModule.allowWarpsAliases)
            return;

        Player player = event.getPlayer();

        Optional<WarpsModule> warpsModuleOptional = WarpsModule.getInstance();
        if (!warpsModuleOptional.isPresent()) {
            Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnModule.replace("%module%", "WARPS"));
            return;
        }
        WarpsModule module = warpsModuleOptional.get();

        String warpName = event.getMessage().split(" ")[0].replaceFirst("/", "");

        Optional<Warp> warpOptional = module.getWarp(warpName);
        if (!warpOptional.isPresent()) {
            return;
        }
        Warp warp = warpOptional.get();

        if (!player.hasPermission("pwarps.warp." + warp.getName())) {
            player.sendMessage(MessagesModule.noPermissionToWarp.replace("%warp%", warp.getName()));
            return;
        }

        PlayerTeleportWarpEvent playerTeleportWarpEvent = new PlayerTeleportWarpEvent(player, warp, player.hasPermission("pwarps.delay.bypass") ? 0 : ConfigModule.defaultTeleportDelay);
        Bukkit.getPluginManager().callEvent(playerTeleportWarpEvent);
        event.setCancelled(!playerTeleportWarpEvent.isCancelled());
    }
}
