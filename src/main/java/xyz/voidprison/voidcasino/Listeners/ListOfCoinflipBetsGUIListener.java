package xyz.voidprison.voidcasino.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.voidprison.voidcasino.Commands.ListOfCoinflipBetsGUICommand;
import xyz.voidprison.voidcasino.Functions.FlipCoinflipAnimationGUI;
import xyz.voidprison.voidcasino.Models.Bet;
import xyz.voidprison.voidcasino.Models.BetManager;
import xyz.voidprison.voidcore.Data.Stars;

import java.util.Collections;
import java.util.UUID;

import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING;

public class ListOfCoinflipBetsGUIListener implements Listener {

    private String displayBalance;

    private String selectedColor = "";
    private BetManager betManager;

    public ListOfCoinflipBetsGUIListener(BetManager betManager, ListOfCoinflipBetsGUICommand command){
        this.betManager = betManager;
    }

    @EventHandler

    public void onClick (InventoryClickEvent event){

        if(!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        if (player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lList of coinflip bets"))){
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasDisplayName()) {
                return;
            }

            if(clickedItem.getType() == Material.NETHER_STAR &&
                    clickedItem.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&e&lCreate bet"))){
                player.closeInventory();
                openCreateBetGUI(player);
            }

            String displayName = clickedItem.getItemMeta().getDisplayName();
            displayName = ChatColor.stripColor(displayName);
            displayName = displayName.substring(0, displayName.length() - 6);

            if(clickedItem.getType() == Material.PAPER && event.getClick() == ClickType.DROP){
                event.setCancelled(true);

                if(displayName.equalsIgnoreCase(playerName)){
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Your bet has been removed. Your" + ChatColor.YELLOW + " stars " + ChatColor.LIGHT_PURPLE + "have been given back");
                    betManager.removeBet(player);
                    return;
                }
            }

            if(clickedItem.getType() == Material.PAPER && event.getClick() == ClickType.LEFT){
                long betAmount = betManager.getBetAmount(displayName);

                if (player != Bukkit.getPlayer(displayName)) {
                    long starBalance = Stars.getStars(player);

                    if(starBalance >= betAmount){

                        starBalance = starBalance - betAmount;
                        Stars.setStars(player, starBalance);

                        String betCreatorColor = betManager.getSelectedColor(displayName);

                        player.closeInventory();
                        betManager.deleteBetOnStart(Bukkit.getPlayer(displayName));

                        if (selectedColor.equalsIgnoreCase("blue")) {
                            new FlipCoinflipAnimationGUI(Bukkit.getPlayer(displayName), player, betAmount, betCreatorColor, "red");
                        }
                        else{
                            new FlipCoinflipAnimationGUI(Bukkit.getPlayer(displayName), player, betAmount, betCreatorColor, "blue");
                        }
                    }
                   else{
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&lERROR! &8|&D Not enough stars for this bet!"));
                    }
                }
                else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&lERROR! &8|&D You cannot play against yourself!"));
                }
            }
        }
        else if(player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lCreate coinflip bet")) || player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lFlipping..."))) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();

            if ( clickedItem != null){
                if (clickedItem.getType() == Material.RED_WOOL){
                    selectedColor = "red";
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                }
                else if (clickedItem.getType() == Material.BLUE_WOOL){
                    selectedColor = "blue";
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.2f, 1.2f);
                }
                if (clickedItem.getType() == Material.GOLD_NUGGET) {
                    long currentStake = betManager.getStake(playerUUID);
                    long newStake = currentStake + 1000000;
                    betManager.putTemporaryStakeInHashMap(playerUUID, newStake);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    updateCreateBetGUI(player);
                }
                else if (clickedItem.getType() == Material.GOLD_INGOT) {
                    long currentStake = betManager.getStake(playerUUID);
                    long newStake = currentStake + 10000000;
                    betManager.putTemporaryStakeInHashMap(playerUUID, newStake);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    updateCreateBetGUI(player);
                }
                else if (clickedItem.getType() == Material.GOLD_BLOCK) {
                    long currentStake = betManager.getStake(playerUUID);
                    long newStake = currentStake + 100000000;
                    betManager.putTemporaryStakeInHashMap(playerUUID, newStake);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    updateCreateBetGUI(player);
                }
                else if (clickedItem.getType() == Material.IRON_NUGGET) {
                    long currentStake = betManager.getStake(playerUUID);
                    if(currentStake >= 1000000){
                        long newStake = currentStake - 1000000;
                        betManager.putTemporaryStakeInHashMap(playerUUID, newStake);

                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        updateCreateBetGUI(player);
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Not enough stake!");
                    }
                }
                else if (clickedItem.getType() == Material.IRON_INGOT) {
                    long currentStake = betManager.getStake(playerUUID);
                    if(currentStake >= 10000000){
                        long newStake = currentStake - 10000000;
                        betManager.putTemporaryStakeInHashMap(playerUUID, newStake);

                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        updateCreateBetGUI(player);
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Not enough stake!");
                    }
                }
                else if (clickedItem.getType() == Material.IRON_BLOCK) {
                    long currentStake = betManager.getStake(playerUUID);
                    if(currentStake >= 100000000){
                        long newStake = currentStake - 100000000;
                        betManager.putTemporaryStakeInHashMap(playerUUID, newStake);

                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        updateCreateBetGUI(player);
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Not enough stake!");
                    }
                }
                else if (clickedItem.getType() == Material.REDSTONE_BLOCK){
                    long currentStake = betManager.getStake(playerUUID);
                    long newStake = currentStake - currentStake;
                    betManager.putTemporaryStakeInHashMap(playerUUID, newStake);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    updateCreateBetGUI(player);
                }
                else if(clickedItem.getType() == Material.GREEN_STAINED_GLASS){
                    long currentStake = betManager.getStake(playerUUID);
                    if(!selectedColor.isEmpty() && currentStake> 0){
                        long starBalance = Stars.getStars(player);
                        if(starBalance >= currentStake){
                            starBalance = starBalance - currentStake;
                            Stars.setStars(player, starBalance);
                            player.playSound(player.getLocation(),BLOCK_NOTE_BLOCK_PLING, 1f ,1f );

                            Bet newBet = new Bet(player.getName(), selectedColor, currentStake);

                            betManager.addBet(newBet);
                            player.sendMessage(ChatColor.GREEN + "Bet placed successfully! You bet " + displayBalance + " on " + selectedColor + ".");
                            player.closeInventory();
                        }
                        else{
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&lERROR! &8|&D Not enough stars for this bet!"));
                        }
                    }
                }
                long currentStake = betManager.getStake(playerUUID);
                if( currentStake  >= 1000000L && currentStake < 1000000000L){
                    displayBalance = (currentStake / 1000000L) + "M";
                }
                else if ( currentStake >= 1000000000L && currentStake < 1000000000000L){
                    displayBalance = String.format("%.3fB", currentStake /1000000000.0);
                }else{
                    displayBalance = String.valueOf(currentStake);
                }
                if(player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lCreate coinflip bet"))){
                    updateCreateBetGUI(player);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lCreate coinflip bet"))){
            selectedColor = "";
            betManager.deleteStake(playerUUID);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        betManager.deleteStake(playerUUID);

        if(betManager.hasBet(player.getName())){
            betManager.removeBet(player.getPlayer());
        }
    }
    private void openCreateBetGUI(Player player){
        UUID playerUUID = player.getUniqueId();
        long currentStake = betManager.getStake(playerUUID);

        Inventory inventory = Bukkit.createInventory(player, 9 * 5, ChatColor.translateAlternateColorCodes('&', "&5&lCreate coinflip bet"));
        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack redCoin = new ItemStack(Material.RED_WOOL);
        ItemStack blueCoin = new ItemStack(Material.BLUE_WOOL);
        ItemStack barrier = new ItemStack(Material.BARRIER);

        ItemStack goldNugget = new ItemStack(Material.GOLD_NUGGET);
        ItemStack goldIngot = new ItemStack(Material.GOLD_INGOT);
        ItemStack goldBlock = new ItemStack(Material.GOLD_BLOCK);

        ItemStack ironNugget = new ItemStack(Material.IRON_NUGGET);
        ItemStack ironIngot = new ItemStack(Material.IRON_INGOT);
        ItemStack ironBlock = new ItemStack(Material.IRON_BLOCK);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK);

        ItemMeta redCoinMeta = redCoin.getItemMeta();
        ItemMeta blueCoinMeta = blueCoin.getItemMeta();
        ItemMeta barrierMeta = barrier.getItemMeta();

        ItemMeta goldNuggetMeta = goldNugget.getItemMeta();
        ItemMeta goldIngotMeta = goldIngot.getItemMeta();
        ItemMeta goldBlockMeta = goldBlock.getItemMeta();

        ItemMeta ironNuggetMeta = ironNugget.getItemMeta();
        ItemMeta ironIngotMeta = ironIngot.getItemMeta();
        ItemMeta ironBlockMeta = ironBlock.getItemMeta();

        ItemMeta paperMeta = paper.getItemMeta();
        ItemMeta redstoneBlockMeta = redstoneBlock.getItemMeta();

        redCoinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRed coin"));
        blueCoinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lBlue coin"));
        barrierMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&l FILL IN BET"));

        goldNuggetMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&l+1M"));
        goldIngotMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&l+10M"));
        goldBlockMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&l+100M"));

        ironNuggetMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&4&l-1M"));
        ironIngotMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&4&l-10M"));
        ironBlockMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&4&l-100M"));

        paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&l" + currentStake));
        redstoneBlockMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&4&lRESET AMOUNT"));

        redCoin.setItemMeta(redCoinMeta);
        blueCoin.setItemMeta(blueCoinMeta);
        barrier.setItemMeta(barrierMeta);

        goldNugget.setItemMeta(goldNuggetMeta);
        goldIngot.setItemMeta(goldIngotMeta);
        goldBlock.setItemMeta(goldBlockMeta);

        ironNugget.setItemMeta(ironNuggetMeta);
        ironIngot.setItemMeta(ironIngotMeta);
        ironBlock.setItemMeta(ironBlockMeta);

        paper.setItemMeta(paperMeta);
        redstoneBlock.setItemMeta(redstoneBlockMeta);

        for (int i = 0; i < 45; i++){
            inventory.setItem(i, grayGlass);
        }
        inventory.setItem(11, redCoin);
        inventory.setItem(13, barrier);
        inventory.setItem(15, blueCoin);
        inventory.setItem(28, goldNugget);
        inventory.setItem(29, goldIngot);
        inventory.setItem(30, goldBlock);
        inventory.setItem(31, paper);
        inventory.setItem(32, ironNugget);
        inventory.setItem(33, ironIngot);
        inventory.setItem(34, ironBlock);
        inventory.setItem(40, redstoneBlock);

        player.openInventory(inventory);
    }
    private void updateCreateBetGUI(Player player){
        Inventory inventory = player.getOpenInventory().getTopInventory();

        UUID playerUUID = player.getUniqueId();
        long currentStake = betManager.getStake(playerUUID);


        if ((selectedColor.equals("red") || selectedColor.equals("blue")) && currentStake > 0) {
            ItemStack greenGlass = new ItemStack(Material.GREEN_STAINED_GLASS);
            ItemMeta greenMeta = greenGlass.getItemMeta();
            greenMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lReady to Bet!"));
            greenGlass.setItemMeta(greenMeta);
            inventory.setItem(13, greenGlass);
        } else {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            barrierMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&l FILL IN BET"));
            barrier.setItemMeta(barrierMeta);
            inventory.setItem(13, barrier);
        }

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e" + displayBalance));
        paper.setItemMeta(paperMeta);
        inventory.setItem(31, paper);

        ItemStack redCoin = new ItemStack(Material.RED_WOOL);
        ItemStack blueCoin = new ItemStack(Material.BLUE_WOOL);

        ItemMeta redCoinMeta = redCoin.getItemMeta();
        if (selectedColor.equals("red")){
            redCoinMeta.addEnchant(Enchantment.LURE, 1, true);
            redCoinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            redCoinMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&',"&a&lChosen side")));
            redCoinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRed coin"));
        }
        redCoinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRed coin"));
        redCoin.setItemMeta(redCoinMeta);
        inventory.setItem(11, redCoin);

        ItemMeta blueCoinMeta = blueCoin.getItemMeta();
        if (selectedColor.equals("blue")){
            blueCoinMeta.addEnchant(Enchantment.LURE, 1, true);
            blueCoinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            blueCoinMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&',"&a&lChosen side")));
            blueCoinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lBlue coin"));
        }
        blueCoinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lBlue coin"));
        blueCoin.setItemMeta(blueCoinMeta);
        inventory.setItem(15, blueCoin);
    }
}