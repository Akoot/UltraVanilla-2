package net.akoot.plugins.ultravanilla.commands;

import net.akoot.plugins.ultravanilla.UltraPlugin;
import net.akoot.plugins.ultravanilla.UltraVanilla;
import net.akoot.plugins.ultravanilla.util.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class UltravanillaCommand extends UltraCommand {

    public UltravanillaCommand(UltraVanilla instance) {
        super(instance, ChatColor.GOLD);
    }

    @Override
    public boolean onCommand() {

        // Sub-commands: none
        if (args.length == 0) {
            sender.sendMessage(message("no-args.version", "%v", plugin.getDescription().getVersion()));
            sender.sendMessage(list("no-args.modules", UltraVanilla.getHookNames()));
        }

        // Sub-commands: reload
        else if (args.length == 1) {

            // reload
            if (args[0].equalsIgnoreCase("reload")) {
                color = ChatColor.GREEN;
                if (hasPermission(sender, "reload")) {
                    for (UltraPlugin hook : UltraVanilla.getHooks()) {
                        hook.reload();
                    }
                    sender.sendMessage(message("reload.all"));
                } else {
                    sender.sendMessage(noPermission("reload-config"));
                }
            } else if (args[0].equalsIgnoreCase("changelog")) {
                color = ChatColor.GRAY;
                for (UltraPlugin hook : UltraVanilla.getHooks()) {
                    sendChangelogForPlugin(sender, hook);
                }
            } else {
                return false;
            }
        } else if (args.length == 2) {
            color = ChatColor.GREEN;
            if (args[0].equalsIgnoreCase("reload")) {
                if (hasPermission(sender, "reload")) {
                    UltraPlugin hook = UltraVanilla.getHook(args[1]);
                    if (hook != null) {
                        hook.reload();
                        sender.sendMessage(message("reload.module", "%p", hook.getName()));
                    } else {
                        sender.sendMessage(error("plugin-invalid", "%p", args[1]));
                    }

                } else {
                    sender.sendMessage(noPermission("reload configs"));
                }
            } else if (args[0].equalsIgnoreCase("changelog")) {
                color = ChatColor.GRAY;
                UltraPlugin hook = UltraVanilla.getHook(args[1]);
                if (hook != null) {
                    sendChangelogForPlugin(sender, hook);
                } else {
                    sender.sendMessage(error("plugin-invalid", "%p", args[1]));
                }
            }
        }

        return true;
    }

    private void sendChangelogForPlugin(CommandSender sender, UltraPlugin ultraPlugin) {
        Config changelog = ultraPlugin.getChangelog();
        String title = message("changelog.title", "%n", ultraPlugin.getDescription().getName(), "%v", ultraPlugin.getDescription().getVersion());

        sender.sendMessage(title);

        // Player section
        List<String> playerAdded = changelog.getConfig().getStringList("player.added");
        List<String> playerRemoved = changelog.getConfig().getStringList("player.removed");
        List<String> playerFixed = changelog.getConfig().getStringList("player.fixed");
        List<String> playerChanged = changelog.getConfig().getStringList("player.changed");
        List<String> playerBroke = changelog.getConfig().getStringList("player.broke");

        // Staff section
        List<String> staffAdded = changelog.getConfig().getStringList("staff.added");
        List<String> staffRemoved = changelog.getConfig().getStringList("staff.removed");
        List<String> staffFixed = changelog.getConfig().getStringList("staff.fixed");
        List<String> staffChanged = changelog.getConfig().getStringList("staff.changed");
        List<String> staffBroke = changelog.getConfig().getStringList("staff.broke");

        sendChangelog(sender, playerAdded, playerRemoved, playerFixed, playerChanged, playerBroke);

        if (hasPermission(sender, "changelog.staff")) {
            sendChangelog(sender, staffAdded, staffRemoved, staffFixed, staffChanged, staffBroke);
        }
    }

    private void sendChangelog(CommandSender sender, List<String> added, List<String> removed, List<String> fixed, List<String> changed, List<String> broke) {
        if (!added.isEmpty()) {
            sender.sendMessage(list("changelog.added", added));
        }

        if (!removed.isEmpty()) {
            sender.sendMessage(list("changelog.removed", removed));
        }

        if (!fixed.isEmpty()) {
            sender.sendMessage(list("changelog.fixed", fixed));
        }

        if (!changed.isEmpty()) {
            sender.sendMessage(list("changelog.changed", changed));
        }

        if (!broke.isEmpty()) {
            sender.sendMessage(list("changelog.broke", broke));
        }
    }

    @Override
    public List<String> onTabComplete() {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 0) {
            suggestions.add("changelog");
            suggestions.add("reload");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("changelog")) {
                for (UltraPlugin hook : UltraVanilla.getHooks()) {
                    suggestions.add(hook.getDescription().getName().toLowerCase());
                }
            }
        }
        return suggestions;
    }
}
