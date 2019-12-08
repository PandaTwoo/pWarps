package br.com.pandatwo.pwarps.events;

import br.com.pandatwo.pwarps.entities.Warp;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@NonNull
@Getter
@Setter
public class PlayerTeleportWarpEvent extends Event implements Cancellable {

    private Player player;
    private Warp warp;
    private Boolean isCancelled = false;
    private int delay;

    public PlayerTeleportWarpEvent(Player player, Warp warp, int delay) {
        this.player = player;
        this.warp = warp;
        this.delay = delay;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
