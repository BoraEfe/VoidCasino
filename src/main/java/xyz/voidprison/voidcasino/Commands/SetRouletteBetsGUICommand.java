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

import java.util.ArrayList;
import java.util.List;

public class SetRouletteBetsGUICommand implements CommandExecutor {
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("playroulette")) {
           Player player = (Player) sender;

            Inventory inventory = Bukkit.createInventory(player, 9 * 6, ChatColor.translateAlternateColorCodes('&', "&5&lPlace roulette bet"));

            ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemStack redWool = new ItemStack(Material.RED_WOOL);
            ItemStack blackWool = new ItemStack(Material.BLACK_WOOL);
            ItemStack greenWool = new ItemStack(Material.GREEN_WOOL);
            ItemStack barrier = new ItemStack(Material.BARRIER);

            ItemStack allRed = new ItemStack(Material.RED_WOOL);
            ItemStack allBlack = new ItemStack(Material.BLACK_WOOL);


            ItemMeta redWoolMeta = redWool.getItemMeta();
            ItemMeta blackWoolMeta = blackWool.getItemMeta();
            ItemMeta greenWoolMeta = greenWool.getItemMeta();
            ItemMeta barrierMeta = barrier.getItemMeta();

            ItemMeta allRedMeta = allRed.getItemMeta();
            ItemMeta allBlackMeta = allBlack.getItemMeta();


            redWoolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRed bet"));
            blackWoolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lBlack bet"));
            greenWoolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lStart bet"));
            barrierMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&l FILL IN BET"));

            allRedMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lAll red"));
            allBlackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&8&lAll black"));

            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to add 500K"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to remove 500K"));

            redWoolMeta.setLore(lore);
            blackWoolMeta.setLore(lore);
            greenWoolMeta.setLore(lore);
            redWool.setItemMeta(redWoolMeta);
            blackWool.setItemMeta(blackWoolMeta);
            greenWool.setItemMeta(greenWoolMeta);
            barrier.setItemMeta(barrierMeta);

            allRed.setItemMeta(allRedMeta);
            allBlack.setItemMeta(allBlackMeta);

            inventory.setItem(0, greenWool);
            inventory.setItem(2, redWool);
            inventory.setItem(3, blackWool);
            inventory.setItem(4, redWool);
            inventory.setItem(5, blackWool);
            inventory.setItem(6, redWool);
            inventory.setItem(9, blackWool);
            inventory.setItem(10, redWool);
            inventory.setItem(11, blackWool);
            inventory.setItem(12, redWool);
            inventory.setItem(13, blackWool);
            inventory.setItem(14, blackWool);
            inventory.setItem(15, redWool);
            inventory.setItem(18, blackWool);
            inventory.setItem(19, redWool);
            inventory.setItem(20, blackWool);
            inventory.setItem(21, redWool);
            inventory.setItem(22, blackWool);
            inventory.setItem(23, redWool);
            inventory.setItem(24, redWool);
            inventory.setItem(27, blackWool);
            inventory.setItem(28, redWool);
            inventory.setItem(29, blackWool);
            inventory.setItem(30, redWool);
            inventory.setItem(31, blackWool);
            inventory.setItem(32, redWool);
            inventory.setItem(33, blackWool);
            inventory.setItem(36, redWool);
            inventory.setItem(37, blackWool);
            inventory.setItem(38, blackWool);
            inventory.setItem(39, redWool);
            inventory.setItem(40, blackWool);
            inventory.setItem(41, redWool);
            inventory.setItem(42, blackWool);
            inventory.setItem(45, redWool);
            inventory.setItem(46, blackWool);
            inventory.setItem(47, redWool);



           inventory.setItem(7, grayGlass);
           inventory.setItem(16, grayGlass);
           inventory.setItem(25, grayGlass);
           inventory.setItem(34, grayGlass);
           inventory.setItem(43, grayGlass);
           inventory.setItem(52, grayGlass);

           inventory.setItem(8, allRed);
           inventory.setItem(17, allBlack);

           inventory.setItem(53, barrier);

            player.openInventory(inventory);
        }
        return true;
    }
}
