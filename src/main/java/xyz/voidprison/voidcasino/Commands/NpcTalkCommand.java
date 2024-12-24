package xyz.voidprison.voidcasino.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class NpcTalkCommand implements CommandExecutor, Listener {

    private final HashMap<String, List<String>> npcDialogues = new HashMap<>();
    private final Random random = new Random();

    public NpcTalkCommand(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        npcDialogues.put("bartender", Arrays.asList(
                "Hello there!",
                "Brewing is an art, you know.",
                "Come back later for a special brew!"

        ));
        npcDialogues.put("brewer2", Arrays.asList(
                "What can I get you today?",
                "The best potions in town, guaranteed!",
                "Don't tell anyone, but I have a secret recipe."
        ));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length == 0){
                player.sendMessage("No arguments provided!");
            }
            else{
                player.sendMessage("command executed, but something went wrong");
            }
        }
        return true;
    }

}
