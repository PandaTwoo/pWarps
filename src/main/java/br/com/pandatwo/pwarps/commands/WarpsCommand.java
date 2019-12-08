package br.com.pandatwo.pwarps.commands;

import br.com.pandatwo.pwarps.concurrent.AsyncTask;
import br.com.pandatwo.pwarps.events.WarpCreatedEvent;
import br.com.pandatwo.pwarps.events.WarpDeleteEvent;
import br.com.pandatwo.pwarps.entities.Warp;
import br.com.pandatwo.pwarps.modules.Modules;
import br.com.pandatwo.pwarps.modules.Module;
import br.com.pandatwo.pwarps.modules.impl.MessagesModule;
import br.com.pandatwo.pwarps.modules.impl.WarpsModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WarpsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (Arrays.asList("reload", "recarregar").contains(args[0].toLowerCase())) {
                if (!sender.hasPermission("pwarps.warps.reload")) {
                    sender.sendMessage(MessagesModule.noPermissionToExecuteCommand);
                    return true;
                }
                sender.sendMessage(MessagesModule.reloadingAllModules);
                CompletableFuture<Void> future = AsyncTask.completeFuture(() -> Modules.getInstance().reloadAllModules());
                future.whenComplete((aVoid, throwable) -> sender.sendMessage(MessagesModule.allModulesReloaded));
                return true;
            }
        }
        if (args.length == 2) {
            if (Arrays.asList("create", "criar", "setar", "set").contains(args[0].toLowerCase())) {
                if (!sender.hasPermission("pwarps.warps.create")) {
                    sender.sendMessage(MessagesModule.noPermissionToExecuteCommand);
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessagesModule.onlyPlayersCanExecuteCommand);
                    return true;
                }
                Player player = (Player) sender;
                Optional<WarpsModule> warpsModuleOptional = WarpsModule.getInstance();
                if (!warpsModuleOptional.isPresent()) {
                    sender.sendMessage(MessagesModule.errorOnExecutingCommand);
                    Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnModule.replace("%module%", "WARPS"));
                    return true;
                }
                WarpsModule warpsModule = warpsModuleOptional.get();
                if (warpsModule.existsWarp(args[1])) {
                    sender.sendMessage(MessagesModule.warpAlreadyExists.replace("%warp%", args[1]));
                    return true;
                }
                WarpCreatedEvent warpCreatedEvent = new WarpCreatedEvent(player, new Warp(args[1], player.getLocation()));
                Bukkit.getPluginManager().callEvent(warpCreatedEvent);
                return true;
            }
            if (Arrays.asList("deletar", "delete", "remove", "remover").contains(args[0].toLowerCase())) {
                if (!sender.hasPermission("pwarps.warps.delete")) {
                    sender.sendMessage(MessagesModule.noPermissionToExecuteCommand);
                    return true;
                }
                Optional<WarpsModule> warpsModuleOptional = WarpsModule.getInstance();
                if (!warpsModuleOptional.isPresent()) {
                    sender.sendMessage(MessagesModule.errorOnExecutingCommand);
                    Bukkit.getConsoleSender().sendMessage(MessagesModule.errorOnModule.replace("%module%", "WARPS"));
                    return true;
                }
                WarpsModule warpsModule = warpsModuleOptional.get();
                Optional<Warp> warpOptional = warpsModule.getWarp(args[1]);
                if (!warpOptional.isPresent()) {
                    sender.sendMessage(MessagesModule.warpNotFound.replace("%warp%", args[1]));
                    return true;
                }
                Warp warp = warpOptional.get();
                WarpDeleteEvent warpDeleteEvent = new WarpDeleteEvent(sender, warp);
                Bukkit.getPluginManager().callEvent(warpDeleteEvent);
                return true;
            }
            if (Arrays.asList("reload", "recarregar").contains(args[0].toLowerCase())) {
                if (!sender.hasPermission("pwarps.warps.reload")) {
                    sender.sendMessage(MessagesModule.noPermissionToExecuteCommand);
                    return true;
                }
                Optional<Module> targetModuleOptional = Modules.getInstance().getModule(args[1]);
                if (!targetModuleOptional.isPresent()) {
                    sender.sendMessage(MessagesModule.moduleNotFound.replace("%module", args[1]));
                    return true;
                }
                Module targetModule = targetModuleOptional.get();
                sender.sendMessage(MessagesModule.reloadingModule.replace("%module%", args[1]));
                CompletableFuture<Void> future = AsyncTask.completeFuture(targetModule::reload);
                future.whenComplete((aVoid, throwable) -> sender.sendMessage(MessagesModule.moduleReloaded.replace("%module%", args[1])));
                return true;
            }
        }

        if (sender.hasPermission("pwarps.warps.help")) {
            sender.sendMessage(MessagesModule.adminHelp);
            return true;
        } else {
            sender.sendMessage(MessagesModule.noPermissionToExecuteCommand);
            return true;
        }
    }
}
