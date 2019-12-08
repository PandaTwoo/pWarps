package br.com.pandatwo.pwarps.entities;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class User {
    
    private final UUID uniqueId;
    private WarpTeleportRequest warpTeleportRequest;

    public User(Player player) {
        this.uniqueId = player.getUniqueId();
    }

    public Boolean hasWarpTeleportRequest() {
        return warpTeleportRequest != null;
    }
}
