package xyz.voidprison.voidcasino.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SetRouletteBetsGUICommand implements CommandExecutor {


 @Override
 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
  if (command.getName().equalsIgnoreCase("playroulette")) {
   if (!(sender instanceof Player)) {
    sender.sendMessage("Only players can use this command.");
    return true;
   }

   Player player = (Player) sender;
   Inventory inventory = Bukkit.createInventory(player, 9 * 6, ChatColor.translateAlternateColorCodes('&', "&5&lPlace roulette bet"));

   Map<Integer, ChatColor> rouletteNumbers = new HashMap<>();
   rouletteNumbers.put(0, ChatColor.GREEN);
   int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
   int[] blackNumbers = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

   for (int red : redNumbers) {
    rouletteNumbers.put(red, ChatColor.RED);
   }
   for (int black : blackNumbers) {
    rouletteNumbers.put(black, ChatColor.DARK_GRAY);
   }

   // Slot order for roulette numbers
   int[] slotIndices = {0, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 36, 37, 38, 39, 40, 41, 42, 45, 46, 47};
   int[] rouletteOrder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36};


   for (int i = 0; i < slotIndices.length; i++) {
    if (i >= rouletteOrder.length) break;
    int slot = slotIndices[i];
    if (slot < 0 || slot >= inventory.getSize()) continue;

    int rouletteNumber = rouletteOrder[i];
    ChatColor color = rouletteNumbers.get(rouletteNumber);

    Material woolMaterial = Material.WHITE_WOOL;
    if (color == ChatColor.GREEN) {
     woolMaterial = Material.GREEN_WOOL;
    } else if (color == ChatColor.RED) {
     woolMaterial = Material.RED_WOOL;
    } else if (color == ChatColor.DARK_GRAY) {
     woolMaterial = Material.BLACK_WOOL;
    }

    ItemStack wool = new ItemStack(woolMaterial);
    ItemMeta woolMeta = wool.getItemMeta();
    woolMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&eTotal bet on this number: &70"),
            ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"),
            ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"),

            ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your stake | &4&lREMOVE")
    ));
    if (woolMeta != null) {
     woolMeta.setDisplayName(color + "" + ChatColor.BOLD + rouletteNumber);
     wool.setItemMeta(woolMeta);
    }
    inventory.setItem(slot, wool);
   }

   ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
   ItemMeta grayMeta = grayGlass.getItemMeta();
   if (grayMeta != null) {
    grayMeta.setDisplayName(ChatColor.GRAY + " ");
    grayGlass.setItemMeta(grayMeta);
   }

   ItemStack allRed = new ItemStack(Material.RED_WOOL);
   ItemStack allBlack = new ItemStack(Material.BLACK_WOOL);

   ItemMeta allRedMeta = allRed.getItemMeta();
   if (allRedMeta != null){
    allRedMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Bet All Red");
            allRedMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&eTotal bet on this number: &70"),
            ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"),
            ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"),
            ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your bet | &4&lREMOVE")
    ));
    allRed.setItemMeta(allRedMeta);

   }

   ItemMeta allBlackMeta = allBlack.getItemMeta();
   if (allBlackMeta != null) {
    allBlackMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bet All Black");
    allBlackMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&eTotal bet on this number: &70"),
            ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"),
            ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"),
            ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your bet | &4&lREMOVE")
    ));
    allBlack.setItemMeta(allBlackMeta);
   }


   Material woolMaterial = Material.WHITE_WOOL;
   ItemStack wool = new ItemStack(woolMaterial);
   ItemMeta woolMeta = wool.getItemMeta();
   woolMeta.setLore(Arrays.asList(
           ChatColor.translateAlternateColorCodes('&', "&eTotal bet on this number: &70"),
           ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"),
           ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"),

           ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your bet | &4&lREMOVE")
   ));

   ItemStack barrier = new ItemStack(Material.BARRIER);
   ItemMeta barrierMeta = barrier.getItemMeta();
   if (barrierMeta != null) {
    barrierMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "FILL IN BED");
    barrier.setItemMeta(barrierMeta);
   }

   ItemStack paper = new ItemStack(Material.PAPER);
   ItemMeta paperMeta = paper.getItemMeta();
   paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lTotal bet: 0"));
   paper.setItemMeta(paperMeta);


   for (int slot : new int[]{7, 16, 25, 34, 43, 52}) {
    if (slot >= 0 && slot < inventory.getSize()) {
     inventory.setItem(slot, grayGlass);
    }
   }

   if (8 < inventory.getSize()) inventory.setItem(8, allRed);
   if (17 < inventory.getSize()) inventory.setItem(17, allBlack);
   if (53 < inventory.getSize()) inventory.setItem(53, barrier);
   if (44 < inventory.getSize()) inventory.setItem(44, paper);


   player.openInventory(inventory);
   return true;
  }

  return false;
 }
}
