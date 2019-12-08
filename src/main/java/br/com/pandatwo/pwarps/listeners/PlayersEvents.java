package br.com.pandatwo.pwarps.listeners;

import br.com.pandatwo.pwarps.entities.User;
import br.com.pandatwo.pwarps.modules.impl.MessagesModule;
import br.com.pandatwo.pwarps.modules.impl.UsersModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayersEvents implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = new User(player);
        UsersModule.getInstance().ifPresent(usersModule -> usersModule.registerUser(user));
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getBlock() == event.getFrom().getBlock())
            return;

        Optional<UsersModule> userModuleOptional = UsersModule.getInstance();
        if (!userModuleOptional.isPresent()) {
            return;
        }

        Player player = event.getPlayer();

        UsersModule userModule = userModuleOptional.get();
        userModule.getUser(event.getPlayer()).ifPresent(user -> {
            if (user.hasWarpTeleportRequest()) {
                user.setWarpTeleportRequest(null);
                player.sendMessage(MessagesModule.playerTeleportCancelled);
            }
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UsersModule.getInstance().ifPresent(usersModule ->
                usersModule.getUser(player).ifPresent(usersModule::unregisterUser));
    }
}
