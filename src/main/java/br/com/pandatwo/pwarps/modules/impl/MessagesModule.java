package br.com.pandatwo.pwarps.modules.impl;

import br.com.pandatwo.pwarps.entities.utilitaries.Config;
import br.com.pandatwo.pwarps.modules.Module;
import br.com.pandatwo.pwarps.modules.ModulePriority;
import br.com.pandatwo.pwarps.modules.Modules;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Optional;

public class MessagesModule implements Module {

    public static String prefix = "§epWarps §7»";

    public static String errorOnExecutingCommand = prefix + " §cHouve um erro ao executar esse comando!";

    public static String errorOnLoadingWarp = prefix + "§cHouve um erro ao carregar a warp §a%name%§c!";
    public static String onlyPlayersCanExecuteCommand = prefix + " §cApenas jogadores podem executar esse commando!";

    public static String warpCreated = prefix + " §aA warp §e%warp% §afoi criada com sucesso!";
    public static String warpDeleted = prefix + " §aA warp §e%warp% §afoi deletada com sucesso!";
    public static String warpAlreadyExists = prefix + " §cA warp §a%warp% §cjá existe!";

    public static String warpNotFound = prefix + " §cA warp §a%warp% §cnão existe!";

    public static String errorOnModule = prefix + " §cHouve um erro no modulo §e%module%§c!";
    public static String moduleNotFound = prefix + " §cO modulo §a%module% §cnão existe!";
    public static String reloadingModule = prefix + " §aRecarregando o modulo §e%module%§a!";
    public static String moduleReloaded = prefix + " §aO modulo §e%module% §afoi recarregado!";
    public static String reloadingAllModules = prefix + " §aRecarregando todos os modulos...";
    public static String allModulesReloaded = prefix + " §aTodos os modulos foram recarregados!";

    public static String playerTeleportCancelled = prefix + " §cVocê se mexeu, teleporte cancelado!";
    public static String playerTeleportedSucessful = prefix + " §aVocê se teleportou com sucesso para §e%warp%§a!";
    public static String playerStartedTeleport = prefix + " §aVocê está indo para §e%warp%§a! Aguarde §e%delay% §asegundos!";
    public static String noPermissionToWarp = prefix + " §cVocê não tem permissão para ir para a warp §a%warp%";
    public static String noPermissionToExecuteCommand = prefix + " §cVocê não tem permissão para executar esse comando!";

    public static String warpList = prefix + " §a%warps%";
    public static String emptyWarpList = prefix + " §cNão há warps carregadas!";

    public static String[] playerHelp = new String[]{
            "§c» §aÍndice de ajuda das warps!",
            "",
            "  §c• §e/warp ir (warp) §f- §eTeleporta até a warp desejada",
            "  §c• §e/warp list §f- §eLista todas as warps existentes"
    };
    public static String[] adminHelp = new String[]{
            "§c» §aÍndice de ajuda do /warps!",
            "",
            "  §c• §e/warps criar <nome da warp> §f- §eCria uma nova warp",
            "  §c• §e/warps deletar <nome da warp> §f- §eDeleta uma warp",
            "  §c• §e/warps reload §f - §eRecarrega todos os modulos do plugin",
            "  §c• §e/warps reload <Modulo> §f- §eRecarrega o modulo infomado"
    };

    private Config messagesFile = new Config("messages.yml", true);

    @Override
    public void load() {
        loadMessages();
    }

    @Override
    public void unload() {
    }

    @Override
    public void reload() {
        loadMessages();
    }

    private void loadMessages() {
        messagesFile.reload();
        prefix = getFormatted("Messages.Prefix", prefix);
        errorOnExecutingCommand = getFormatted("Messages.Player.Both.Command_Error", errorOnExecutingCommand);
        errorOnLoadingWarp = getFormatted("Messages.Console.Loading_Warp_Error", errorOnLoadingWarp);
        onlyPlayersCanExecuteCommand = getFormatted("Messages.Console.Cant_Use_Command", onlyPlayersCanExecuteCommand);
        warpCreated = getFormatted("Messages.Player.Admin.Warp_Created", warpCreated);
        warpDeleted = getFormatted("Messages.Player.Admin.Warp_Deleted", warpDeleted);
        warpAlreadyExists = getFormatted("Messages.Player.Admin.Warp_Already_Exists", warpAlreadyExists);
        warpNotFound = getFormatted("Messages.Player.Both.Warp_Dont_Exists", warpNotFound);
        errorOnModule = getFormatted("Messages.Modules.Module_Error", errorOnModule);
        moduleNotFound = getFormatted("Messages.Modules.Module_Not_Exists", moduleNotFound);
        reloadingModule = getFormatted("Messages.Modules.Modules_Reloading", reloadingModule);
        moduleReloaded = getFormatted("Messages.Modules.Module_Reloaded", moduleReloaded);
        reloadingAllModules = getFormatted("Messages.Modules.Modules_Reloading", reloadingAllModules);
        allModulesReloaded = getFormatted("Messages.Modules.Modules_Reloaded", allModulesReloaded);
        playerTeleportCancelled = getFormatted("Messages.Player.Normal.Warp_Teleport_Cancel", playerTeleportCancelled);
        playerTeleportedSucessful = getFormatted("Messages.Player.Normal.Warp_Teleport_Sucess", playerTeleportedSucessful);
        playerStartedTeleport = getFormatted("Messages.Player.Normal.Warp_Teleport_Started", playerStartedTeleport);
        noPermissionToWarp = getFormatted("Messages.Player.Normal.No_Permission_Warp", noPermissionToWarp);
        noPermissionToExecuteCommand = getFormatted("Messages.Player.Normal.No_Permission", noPermissionToExecuteCommand);
        warpList = getFormatted("Messages.Player.Normal.Warp_List", warpList);
        emptyWarpList = getFormatted("Messages.Player.Normal.Empty_Warp_List", emptyWarpList);
        playerHelp = getFormattedStringList("Messages.Player.Normal.Help_Command", playerHelp);
        adminHelp = getFormattedStringList("Messages.Player.Admin.Help_Command", adminHelp);
    }

    private String getFormatted(String local, String or) {
        return ChatColor.translateAlternateColorCodes('&', messagesFile.getValue(local, or).replace("%prefix%", prefix));
    }

    private String[] getFormattedStringList(String local, String[] or) {
        List<String> messageList = messagesFile.getValue(local);
        String[] message = messageList == null ? or : (String[]) messageList.toArray();
        for (int i = 0; i < message.length; i++) {
            message[i] = ChatColor.translateAlternateColorCodes('&', message[i].replace("%prefix%", prefix));
        }
        return message;
    }

    @Override
    public String getName() {
        return "Mensagens";
    }

    @Override
    public ModulePriority getPriority() {
        return ModulePriority.HIGH;
    }

    public static Optional<MessagesModule> getInstance() {
        return Modules.getInstance().getModule("Mensagens").filter(module -> module instanceof MessagesModule).map(module -> (MessagesModule) module);
    }

}
