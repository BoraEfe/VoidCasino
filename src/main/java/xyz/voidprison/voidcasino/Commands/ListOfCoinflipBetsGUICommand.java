package xyz.voidprison.voidcasino.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.voidprison.voidcasino.Models.Bet;
import xyz.voidprison.voidcasino.Models.BetManager;

import java.util.*;

public class ListOfCoinflipBetsGUICommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("coinfliplist")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            String playerName = player.getName();

            openBetInventory(player, playerName);
        }
        return true;
    }

    private static void openBetInventory(Player player, String playerName) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, ChatColor.translateAlternateColorCodes('&', "&5&lList of coinflip bets"));

        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, grayGlass);
        }

        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherStarMeta = netherStar.getItemMeta();
        netherStarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lCreate bet"));
        netherStar.setItemMeta(netherStarMeta);
        inventory.setItem(49, netherStar);

        int index = 0;
        for (Bet bet : BetManager.getBetsFromYaml()) {
            if (index >= 45) break;

            ChatColor color = "red".equalsIgnoreCase(bet.getSelectedColor()) ? ChatColor.RED :
                    "blue".equalsIgnoreCase(bet.getSelectedColor()) ? ChatColor.BLUE : ChatColor.WHITE;

            ItemStack betItem = new ItemStack(Material.PAPER);
            ItemMeta betItemMeta = betItem.getItemMeta();
            betItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l" + bet.getBetCreator() + "'s bet"));


            List<String> lore = new ArrayList<>(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&7Amount of stars: &e" + bet.getFormatAmount()),
                    ChatColor.translateAlternateColorCodes('&', "&7Chosen side: " + color + bet.getSelectedColor()),
                    ChatColor.translateAlternateColorCodes('&', "")
            ));

            if(bet.getBetCreator().equalsIgnoreCase(playerName)){
                lore.add(ChatColor.RED + "Press Q to Delete Bet");
            }
            else{
                lore.add(ChatColor.YELLOW + "Click to Play!");
            }

            betItemMeta.setLore(lore);
            betItem.setItemMeta(betItemMeta);

            inventory.setItem(index++, betItem);
        }
        player.openInventory(inventory);
    }

    public static void checkForPlayersWithListOpen(){
        for(Player players : Bukkit.getOnlinePlayers()){
            if (players.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lList of coinflip bets"))){
                openBetInventory(players, players.getName());
            }
        }
    }
}