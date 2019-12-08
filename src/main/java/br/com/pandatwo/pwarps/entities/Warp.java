package br.com.pandatwo.pwarps.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;

@AllArgsConstructor
@NonNull
@Getter
@Setter
public class Warp {
    private final String name;
    private Location location;
}
