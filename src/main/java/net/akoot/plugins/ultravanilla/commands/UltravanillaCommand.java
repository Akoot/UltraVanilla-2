package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.UltraPlugin;
import net.akoot.plugins.ultravanilla.UltraVanilla;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UltravanillaCommand extends UltraCommand implements CommandExecutor, TabExecutor {

    public UltravanillaCommand(UltraVanilla instance) {
        super(instance, ChatColor.GOLD);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.command = command;

        // Sub-commands: reload
        if (args.length == 1) {

            // reload
            if (args[0].equalsIgnoreCase("reload")) {
                if (hasPermission(sender, "reload")) {
                    plugin.reloadConfig();
                    strings.reload();
                    for (UltraPlugin plugin : UltraVanilla.getInstance().getHooks()) {
                        plugin.reload();
                    }
                    sender.sendMessage(message("reload"));
                } else {
                    sender.sendMessage(strings.getString("error.no-permission", "%a", "reload the configs"));
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("reload");
        }
        return new ArrayList<>();
    }
}
