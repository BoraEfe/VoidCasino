package xyz.voidprison.voidcasino.Functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.voidprison.voidcasino.Models.BetManager;
import xyz.voidprison.voidcore.Data.Stars;

import java.util.Random;

public class FlipCoinflipAnimationGUI {

    public FlipCoinflipAnimationGUI(Player betCreator, Player betAcceptor, long amount, String betCreatorColor, String betAcceptorColor) {
        long betEarnings = amount * 2;

        Random random = new Random();

        final int BET_CREATOR_WINS = 1;
        final int BET_ACCEPTOR_WINS = 2;
        int winner = random.nextInt(2) + 1;

        new BukkitRunnable() {
            @Override
            public void run() {
                openBlueInventory(betCreator);
                openBlueInventory(betAcceptor);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 10L);

        new BukkitRunnable() {
            @Override
            public void run() {
                openRedInventory(betCreator);
                openRedInventory(betAcceptor);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 18L);

        new BukkitRunnable() {
            @Override
            public void run() {
                openBlueInventory(betCreator);
                openBlueInventory(betAcceptor);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 26L);

        new BukkitRunnable() {
            @Override
            public void run() {
                openRedInventory(betCreator);
                openRedInventory(betAcceptor);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 32L);

        new BukkitRunnable() {
            @Override
            public void run() {
                openBlueInventory(betCreator);
                openBlueInventory(betAcceptor);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 40L);
        new BukkitRunnable() {
            @Override
            public void run() {

                if (winner == BET_CREATOR_WINS) {
                    openCustomColorInventory(betCreator, betCreatorColor);
                    openCustomColorInventory(betAcceptor, betCreatorColor);
                } else if (winner == BET_ACCEPTOR_WINS) {
                    openCustomColorInventory(betCreator, betAcceptorColor);
                    openCustomColorInventory(betAcceptor, betAcceptorColor);
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 48L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (winner == BET_CREATOR_WINS) {
                    gameResults(amount, betEarnings, betEarnings - amount, betCreator); // winner
                    gameResults(amount, 0 , -amount, betAcceptor); //loser
                    Stars.giveStars(betCreator, betEarnings);

                } else if (winner == BET_ACCEPTOR_WINS) {
                    gameResults(amount, betEarnings, betEarnings - amount, betAcceptor); //winner
                    gameResults(amount, 0, -amount, betCreator); //loser
                    Stars.giveStars(betAcceptor, betEarnings);
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 56L);

        new BukkitRunnable(){
            @Override
            public void run(){
                betAcceptor.closeInventory();
                betCreator.closeInventory();
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 90L);
    }

    public void gameResults(long betAmount, long earnings, long profit, Player... players) {
        String header = ChatColor.translateAlternateColorCodes('&', "&D&l&nTOTAL GAME RESULTS");
        String separator = ChatColor.translateAlternateColorCodes('&', "&5&l==================");
        String betDisplay = ChatColor.translateAlternateColorCodes('&', "&d&lYOUR BET: &e&l" + formatAmount(betAmount));
        String earningsDisplay = ChatColor.translateAlternateColorCodes('&', "&d&lEARNINGS: &e&l" + formatAmount(earnings));
        String profitDisplay = ChatColor.translateAlternateColorCodes('&', "&d&lNET RESULT: &e&l" + formatAmount(profit));

        for (Player player : players) {
            player.sendMessage(header);
            player.sendMessage(separator);

            if (profit > 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lYOU &2&lWON &d&lTHE COINFLIP BET!"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lYOU &4&lLOST &d&lTHE COINFLIP BET"));
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l------------------"));
            player.sendMessage(betDisplay);
            player.sendMessage(earningsDisplay);
            player.sendMessage(profitDisplay);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l------------------"));
        }
    }
    public void openCustomColorInventory(Player player, String color){
        if (color.equalsIgnoreCase("blue")){
            openBlueInventory(player);
        }
        else if(color.equalsIgnoreCase("red")){
            openRedInventory(player);
        }
    }

    public void openBlueInventory(Player player) {
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColor.translateAlternateColorCodes('&', "&5&lFlipping..."));
        ItemStack blueWool = new ItemStack(Material.BLUE_WOOL);

        for (int i = 0; i < 26 + 1; i++) {
            inventory.setItem(i, blueWool);
        }
        player.openInventory(inventory);
    }

    public void openRedInventory(Player player) {
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColor.translateAlternateColorCodes('&', "&5&lFlipping..."));
        ItemStack redWool = new ItemStack(Material.RED_WOOL);
        for (int i = 0; i < 26 + 1; i++) {
            inventory.setItem(i, redWool);
        }
        player.openInventory(inventory);
    }
    public String formatAmount(long amount) {
        if (amount >= 1000000L && amount < 1000000000L) {
            return String.format("%dM", amount / 1000000);
        } else if (amount >= 1000000000L && amount < 1000000000000L) {
            return String.format("%.2fB", amount / 1000000000.0);
        }
        return String.valueOf(amount);
    }
}


