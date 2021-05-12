package net.kunmc.lab.strokemagic.command;

import net.kunmc.lab.strokemagic.MagicManager;
import net.kunmc.lab.strokemagic.magic.Magic;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final MagicManager magicManager = MagicManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;
        String subCmd = args[0];
        switch (subCmd) {
            case "list":
                listCommand(sender, args);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "不明なコマンドです.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("list").filter(x -> x.startsWith(args[0])).collect(Collectors.toList());
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "list":
                    return magicManager.getRegisteredMagics().stream().map(Magic::getName).filter(x -> x.startsWith(args[1])).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    private void listCommand(CommandSender sender, String[] args) {
        Collection<Magic> magics = magicManager.getRegisteredMagics();
        //表示したいMagicが指定された場合はそのMagicの説明のみ送信する.
        if (args.length > 1) {
            String magicName = args[1];
            magics.stream().filter(x -> x.getName().equalsIgnoreCase(magicName)).forEach(x -> {
                sender.sendMessage(String.format("%s: %s", ChatColor.YELLOW + x.getName(), ChatColor.AQUA + x.getStroke()));
                sender.sendMessage(String.format("%s", ChatColor.GREEN + x.getDescription()));
            });
        } else {
            magics.forEach(x -> {
                sender.sendMessage(String.format("%s: %s", ChatColor.YELLOW + x.getName(), ChatColor.AQUA + x.getStroke()));
                sender.sendMessage(String.format("%s", ChatColor.GREEN + x.getDescription()));
            });
        }
    }
}
