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
import xyz.voidprison.voidcasino.Functions.RouletteSpinAnimationGUI;
import xyz.voidprison.voidcasino.Models.Bet;
import xyz.voidprison.voidcasino.Models.RouletteBet;
import xyz.voidprison.voidcasino.Models.RouletteBetManager;
import xyz.voidprison.voidcore.Data.Stars;


import java.lang.reflect.Array;
import java.util.*;

import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING;

public class SetRouletteBetsGUIListener implements Listener {

    private final RouletteBetManager betManager = new RouletteBetManager();

    @EventHandler
    public void onClick(InventoryClickEvent event){

        if(!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();
        List<String> colors = new ArrayList<>();

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
                    if (displayName.equals(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + i)
                            || displayName.equals(ChatColor.RED + "" + ChatColor.BOLD + i)
                            || displayName.equals(ChatColor.GREEN + "" + ChatColor.BOLD + "0")) {
                        betManager.resetBetOnNumber(playerName, i);
                        player.sendMessage(ChatColor.GREEN + "Your bet on number " + i + " has been reset!");
                        updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), i, playerName);
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                        return;
                    }
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
                if(betManager.canPlaceBetOnColor(playerName, 37, betAmount)){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    colors.add("red");
                    betManager.addBet(playerName, 37, betAmount, colors);
                    updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 37, playerName);
                    player.sendMessage("All on RED");
                }
                else{
                    player.sendMessage("Max bet");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                }
            }
            else if(displayName.contains(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Bet All Black")){
                if(betManager.canPlaceBetOnColor(playerName, 38, betAmount)){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    colors.add("black");
                    betManager.addBet(playerName, 38, betAmount, colors);
                    updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), 38, playerName);
                    player.sendMessage("All on BLACK");
                }
                else{
                    player.sendMessage("Max bet");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                }
            }
            else if (clickedItem.getType() == Material.REDSTONE_BLOCK){
                betManager.clearPlayerBets(player.getName());
                player.sendMessage("BET HAS BEEN RESET");
                Inventory inventory = player.getOpenInventory().getTopInventory();
                for (int i = 0; i < inventory.getSize(); i++) {
                    updateItemLore(inventory, i, i, player.getName());
                }
            }
            else if(clickedItem.getType() == Material.GREEN_STAINED_GLASS){
                if(betManager.getTotalBetAmount(playerName) > 0 ){

                    long starBalance = Stars.getStars(player);
                    long totalBet = betManager.getTotalBetAmount(playerName);

                    if (starBalance >= totalBet) {
                        player.playSound(player.getLocation(), BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                        starBalance = starBalance - totalBet;
                        Stars.setStars(player, starBalance);

                        List<String> chosenColors = betManager.getColor(playerName);
                        Map<Integer, Long> rouletteNumbers = betManager.getNumbersBetOn(playerName);
                        player.sendMessage(playerName + ": " + totalBet + chosenColors + rouletteNumbers);

                        player.closeInventory();
                        new RouletteSpinAnimationGUI(player, totalBet, rouletteNumbers.size(), String.join(", ", chosenColors));
                    }
                    else{
                        player.sendMessage("&5&lERROR! &8|&D Not enough stars for this bet!");
                    }
                }
            }

            else {
                List<Integer> redNumbers = Arrays.asList(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);
                List<Integer> blackNumbers = Arrays.asList(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35);

                for(int i = 0; i <=36; i++){
                    if (displayName.equals(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + i)
                            || displayName.equals(ChatColor.RED + "" + ChatColor.BOLD + i)
                            || displayName.equals((ChatColor.GREEN + "" + ChatColor.BOLD + i))){

                        if (!betManager.canPlaceBetOnNumber(playerName, i ,betAmount)){
                            player.sendMessage("Max bet on number!");
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            return;
                        }
                        if (i == 0){
                            colors.add("green");
                            betManager.addBet(playerName, i, betAmount, colors);
                        }
                        else if(redNumbers.contains(i)){
                            colors.add("red");
                            betManager.addBet(playerName, i, betAmount, colors);
                        }
                        else if(blackNumbers.contains(i)){
                            colors.add("black");
                            betManager.addBet(playerName, i, betAmount, colors);
                        }

                        player.sendMessage(ChatColor.YELLOW + "You placed a bet on number " + i + "!");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        updateItemLore(player.getOpenInventory().getTopInventory(), event.getSlot(), i, playerName);
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

        long totalBet = betManager.getTotalBetOnNumber(playerName, rouletteNumber);

        String formattedBet = RouletteBet.getFormatAmount(totalBet);

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

        long allBetsTotal = betManager.getTotalBetAmount(playerName);

        String formattedAllBetsTotal = RouletteBet.getFormatAmount(allBetsTotal);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lTotal bet: " + formattedAllBetsTotal));

        List<String> allBetsLore = new ArrayList<>();

        for(int i = 0; i <=38; i++){
            long betAmount = betManager.getTotalBetOnNumber(playerName, i);
            String formattedBetAmount = RouletteBet.getFormatAmount(betAmount);

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
        if(betManager.getTotalBetAmount(playerName) >= 1){
            ItemStack greenGlass = new ItemStack(Material.GREEN_STAINED_GLASS);
            ItemMeta greenGlassMeta = greenGlass.getItemMeta();
            greenGlassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lReady to Bet!"));
            greenGlass.setItemMeta(greenGlassMeta);
            inventory.setItem(53, greenGlass);
        }
        else{
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            barrierMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&l FILL IN BET"));
            barrier.setItemMeta(barrierMeta);
            inventory.setItem(53, barrier);
        }
    }
}
