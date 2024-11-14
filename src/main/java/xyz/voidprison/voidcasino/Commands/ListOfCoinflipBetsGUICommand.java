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

import java.util.Arrays;

public class ListOfCoinflipBetsGUICommand implements CommandExecutor {
    private BetManager betManager;

    public ListOfCoinflipBetsGUICommand(){
        this.betManager = new BetManager();
    }
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("coinfliplist")){
            Player player = (Player) sender;

            Inventory inventory = Bukkit.createInventory(player, 9 * 6, ChatColor.translateAlternateColorCodes('&', "&5&lList of coinflip bets"));
            ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
            ItemMeta netherStarMeta = netherStar.getItemMeta();
            netherStarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&lCreate bet"));
            netherStar.setItemMeta(netherStarMeta);


            for (int i = 45; i < 54; i++){
                inventory.setItem(i, grayGlass);
            }
            inventory.setItem(49, netherStar);

            int index = 0;

            for(Bet bet : betManager.getAllBets()){

                String selectedColor = bet.getSelectedColor();
                ChatColor color = ChatColor.WHITE;
                if("red".equalsIgnoreCase(selectedColor)){
                    color = ChatColor.RED;
                }
                else if ("blue".equalsIgnoreCase(selectedColor)){
                    color = ChatColor.BLUE;
                }

                if (index >= 45){
                    break;
                }
                ItemStack betItem = new ItemStack(Material.PAPER);
                ItemMeta betItemMeta = betItem.getItemMeta();
                betItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l"+ bet.getBetCreator() + "'s bet"));
                betItemMeta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7Amount of stars: " + "&e" + bet.getFormatAmount()),
                        ChatColor.translateAlternateColorCodes('&', "&7Chosen side: " + color + bet.getSelectedColor()),
                        ChatColor.translateAlternateColorCodes('&',""),
                        ChatColor.translateAlternateColorCodes('&',"&eClick to Play!")
                ));
                betItem.setItemMeta(betItemMeta);

                inventory.setItem(index++, betItem);
            }


            player.openInventory(inventory);
        }
        return false;
    }
}
