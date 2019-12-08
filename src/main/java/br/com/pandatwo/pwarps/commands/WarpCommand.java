package br.com.pandatwo.pwarps.commands;

import br.com.pandatwo.pwarps.entities.Warp;
import br.com.pandatwo.pwarps.events.PlayerTeleportWarpEvent;
import br.com.pandatwo.pwarps.modules.impl.ConfigModule;
import br.com.pandatwo.pwarps.modules.impl.MessagesModule;
import br.com.pandatwo.pwarps.modules.impl.WarpsModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class WarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String commandLower = args[0].toLowerCase();

        Optional<WarpsModule> warpsModuleOptional = WarpsModule.getInstance();
        if (!warpsModuleOptional.isPresent()) {
            sender.sendMessage(MessagesModule.errorOnExecutingCommand);
            Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnModule.replace("%module%", "WARPS"));
            return true;
        }

        WarpsModule warpsModule = warpsModuleOptional.get();

        if (args.length == 1) {
            if (Arrays.asList("list", "listar", "lista").contains(commandLower)) {
                Collection<Warp> warps = warpsModule.getWarps();
                if (warps.isEmpty()) {
                    sender.sendMessage(MessagesModule.emptyWarpList);
                    return true;
                }
                String warpsJoined = warps.stream().map(warp -> "" + warp.toString()).collect(Collectors.joining(", "));
                sender.sendMessage(MessagesModule.warpList.replace("%warps%", warpsJoined));
                return true;
            }
        }

        if (sender instanceof ConsoleCommandSender) {
            Bukkit.getConsoleSender().sendMessage(MessagesModule.onlyPlayersCanExecuteCommand);
            return true;
        }

        Player player = (Player) sender;


        if ((args.length == 2 && Arrays.asList("ir", "go", "travel", "viajar").contains(commandLower)) || args.length == 1) {
            Optional<Warp> targetWarp = warpsModule.getWarp(args.length == 1 ? args[0] : args[1]);
            if (!targetWarp.isPresent()) {
                player.sendMessage(MessagesModule.warpNotFound.replace("%warp%", args.length == 1 ? args[0] : args[1]));
                return true;
            }
            Warp warp = targetWarp.get();
            if (!player.hasPermission("pwarps.warp." + warp.getName().toLowerCase())) {
                player.sendMessage(MessagesModule.noPermissionToWarp.replace("%warp%", args.length == 1 ? args[0] : args[1]));
                return true;
            }
            PlayerTeleportWarpEvent playerTeleportWarpEvent = new PlayerTeleportWarpEvent(player, warp, player.hasPermission("pwarps.delay.bypass") ? 0 : ConfigModule.defaultTeleportDelay);
            Bukkit.getPluginManager().callEvent(playerTeleportWarpEvent);
            return true;
        }

        sender.sendMessage(MessagesModule.playerHelp);
        return true;
    }
}
