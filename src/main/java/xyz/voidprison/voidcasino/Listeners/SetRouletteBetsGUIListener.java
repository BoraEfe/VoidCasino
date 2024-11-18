package xyz.voidprison.voidcasino.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import xyz.voidprison.voidcasino.Models.RouletteBet;
import xyz.voidprison.voidcasino.Models.RouletteBetManager;

import java.util.*;

public class SetRouletteBetsGUIListener implements Listener {

    private final RouletteBetManager betManager = new RouletteBetManager();

    @EventHandler
    public void onClick(InventoryClickEvent event){

        if(!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();

        if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&5&lPlace roulette bet"))){
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if  (clickedItem == null || clickedItem.getType() == Material.AIR)return;

            String displayName = clickedItem.getItemMeta().getDisplayName();

            int betAmount = 0;
            if (event.getClick().isLeftClick()) {
                betAmount = 1_000_000;
            } else if (event.getClick().isRightClick()) {
                betAmount = 5_000_000;
            }

            if (event.getClick() == ClickType.DROP){
                for (int i = 0; i <= 36; i++) {
                    if (displayName.equals(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + i) || displayName.equals(ChatColor.RED + "" + ChatColor.BOLD + i)) {
                        betManager.resetBetOnNumber(playerName, i);
                        player.sendMessage(ChatColor.GREEN + "Your bet on number " + i + " has been reset!");
                        updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), i, playerName);
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                        return;
                    }
                }
                if (displayName.contains(ChatColor.GREEN + "" + ChatColor.BOLD + "0")) {
                    betManager.resetBetOnNumber(playerName, 0);
                    updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 0, playerName);
                    player.sendMessage(ChatColor.GREEN + "Your bet on number 0 has been reset!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                }
                if(displayName.equalsIgnoreCase(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bet All Black")) {
                    betManager.resetBetOnNumber(playerName, 38);
                    player.sendMessage(ChatColor.GREEN + "Your bet on color Black has been reset!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                    updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 38, playerName);
                }
                if(displayName.equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Bet All Red")) {
                    betManager.resetBetOnNumber(playerName, 37);
                    player.sendMessage(ChatColor.GREEN + "Your bet on color Red has been reset!");
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                    updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 37, playerName);
                }
                return;
            }

            if(displayName.contains(ChatColor.RED + "" + ChatColor.BOLD + "Bet All Red")){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                betManager.addBet(playerName, 37, betAmount, "Red");
                updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 37, playerName);
                player.sendMessage("All on RED");
            }
            else if(displayName.contains(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bet All Black")){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                betManager.addBet(playerName, 38, betAmount, "black");
                updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 38, playerName);
                player.sendMessage("All on BLACK");
            }
            else if(displayName.contains(ChatColor.GREEN + "" + ChatColor.BOLD + "0")){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                betManager.addBet(playerName, 0, betAmount, null);
                updateItemLore(player.getOpenInventory().getTopInventory(), 0, 0, playerName);
                player.sendMessage("Bet on Green");
            }
            else if (clickedItem.getType() == Material.REDSTONE_BLOCK){
                betManager.clearPlayerBets(player.getName());
                player.sendMessage("BET HAS BEEN RESET");

                Inventory inventory = player.getOpenInventory().getTopInventory();
                for (int i = 0; i < inventory.getSize(); i++) {
                    updateItemLore(inventory, i, i, player.getName());
                }
            }



            else {
                for(int i = 1; i <=36; i++){
                    if (displayName.equals(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + i)|| displayName.equals(ChatColor.RED + "" + ChatColor.BOLD + i)){
                        player.sendMessage(ChatColor.YELLOW + "You placed a bet on number " + i + "!");
                        betManager.addBet(playerName, i, betAmount, null);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), i, playerName);
                        break;
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        if (player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lPlace roulette bet"))){
            betManager.clearPlayerBets(player.getName());

        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        betManager.clearPlayerBets(player.getName());
    }

    private void updateItemLore(Inventory inventory, int slot, int rouletteNumber, String playerName) {
        ItemStack item = inventory.getItem(slot);
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        int totalBet = betManager.getTotalBetOnNumber(playerName, rouletteNumber);

        String formattedBet = new RouletteBet(rouletteNumber, totalBet, null).getFormatAmount();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&eTotal bet on this number: &7" + formattedBet));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your bet | &4&lREMOVE"));

        if (slot == 8) {
            ItemStack allRed = new ItemStack(Material.RED_WOOL);
            ItemMeta allRedMeta = allRed.getItemMeta();
            allRedMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Bet All Red");

            List<String> loreForAllRed = new ArrayList<>();
            loreForAllRed.add(ChatColor.translateAlternateColorCodes('&', "&eTotal bet on this red: &7" + formattedBet));
            loreForAllRed.add(ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"));
            loreForAllRed.add(ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"));
            loreForAllRed.add(ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your bet | &4&lREMOVE"));

            allRedMeta.setLore(loreForAllRed);
            allRed.setItemMeta(allRedMeta);
            inventory.setItem(8, allRed);
        }
        else if (slot == 17) {
                ItemStack allBlack = new ItemStack(Material.BLACK_WOOL);
                ItemMeta allBlackMeta = allBlack.getItemMeta();
                allBlackMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bet All Black");

                List<String> loreForAllBlack = new ArrayList<>();
                loreForAllBlack.add(ChatColor.translateAlternateColorCodes('&', "&eTotal bet on black: &7" + formattedBet));
                loreForAllBlack.add(ChatColor.translateAlternateColorCodes('&', "&eLeft &7click to bet | &a&l+1M"));
                loreForAllBlack.add(ChatColor.translateAlternateColorCodes('&', "&eRight &7click to bet | &a&l+5M"));
                loreForAllBlack.add(ChatColor.translateAlternateColorCodes('&', "&ePress Q &7to reset your bet | &4&lREMOVE"));

                allBlackMeta.setLore(loreForAllBlack);
                allBlack.setItemMeta(allBlackMeta);
                inventory.setItem(17, allBlack);
            }

        meta.setLore(lore);
        item.setItemMeta(meta);

        inventory.setItem(slot, item);

        int allBetsTotal = betManager.getTotalBets(playerName);

        String formattedAllBetsTotal = new RouletteBet(rouletteNumber, allBetsTotal, null).getFormatAmount();

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lTotal bet: " + formattedAllBetsTotal));

        List<String> allBetsLore = new ArrayList<>();
        for(int i = 0; i <=38; i++){
            int betAmount = betManager.getTotalBetOnNumber(playerName, i);
            String formattedBetAmount = new RouletteBet(i, betAmount, null).getFormatAmount();
            if (betAmount > 0 && i <=36){
                allBetsLore.add(ChatColor.translateAlternateColorCodes('&',"&7Number: &e" + i +  " " + "&7Amount: &e" + formattedBetAmount));
            }
            if(i == 37){
                allBetsLore.add(ChatColor.translateAlternateColorCodes('&',"&eAll RED" +  " " + "&7Amount: &e" + formattedBetAmount));
            }
            if(i == 38){
                allBetsLore.add(ChatColor.translateAlternateColorCodes('&',"&eAll BLACK" +  " " + "&7Amount: &e" + formattedBetAmount));
            }
        }
        paperMeta.setLore(allBetsLore);
        paper.setItemMeta(paperMeta);

        inventory.setItem(44, paper);
    }
}
