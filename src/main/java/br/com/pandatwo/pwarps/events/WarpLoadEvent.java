package br.com.pandatwo.pwarps.events;

import br.com.pandatwo.pwarps.entities.Warp;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarpLoadEvent extends Event implements Cancellable {

    @Getter
    @Setter
    @NonNull
    private Warp warp;
    private Boolean isCancelled = false;

    public WarpLoadEvent(Warp warp) {
        this.warp = warp;
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
