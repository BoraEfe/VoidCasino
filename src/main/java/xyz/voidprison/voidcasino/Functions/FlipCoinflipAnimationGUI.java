package xyz.voidprison.voidcasino.Functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FlipCoinflipAnimationGUI {


    public FlipCoinflipAnimationGUI(Player betCreator, Player betAcceptor, long Amount, String BetCreatorColor, String BetAcceptorColor){

        Random random = new Random();
        int winner = random.nextInt(2) + 1;
        if (winner == 1){
            if(BetCreatorColor.equalsIgnoreCase("blue")){
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
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 15L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openBlueInventory(betCreator);
                        openBlueInventory(betAcceptor);
                        betCreator.sendMessage("You have won your own bet!");
                        betAcceptor.sendMessage("You have lost against the creator");
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 20L);
            }
            else{
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
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voiCasino"), 15L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openBlueInventory(betCreator);
                        openBlueInventory(betAcceptor);
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 20L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openRedInventory(betCreator);
                        openRedInventory(betAcceptor);
                        betCreator.sendMessage("You have won your own bet!");
                        betAcceptor.sendMessage("You have lost against the creator");
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 25L);
            }
        }else {
            if(BetAcceptorColor.equalsIgnoreCase("blue")){
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
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 15L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openBlueInventory(betCreator);
                        openBlueInventory(betAcceptor);
                        betAcceptor.sendMessage("You have won against the creator!");
                        betCreator.sendMessage("You have lost your own bet");
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 20L);
            }
            else{
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
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 15L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openBlueInventory(betCreator);
                        openBlueInventory(betAcceptor);
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 20L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openRedInventory(betCreator);
                        openRedInventory(betAcceptor);
                        betAcceptor.sendMessage("You have won against the creator!");
                        betCreator.sendMessage("You have lost your own bet");
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("voidCasino"), 25L);
            }
        }

    }

    public void openBlueInventory(Player player){
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColor.translateAlternateColorCodes('&', "&5&lFlipping..."));
        ItemStack blueWool = new ItemStack(Material.BLUE_WOOL);

        for(int i = 0; i <26 + 1; i++){
            inventory.setItem(i, blueWool);
        }
        player.openInventory(inventory);
    }

    public void openRedInventory(Player player){
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(player,9 * 3, ChatColor.translateAlternateColorCodes('&', "&5&lFlipping..."));
        ItemStack redWool = new ItemStack(Material.RED_WOOL);
        for(int i = 0; i <26 + 1; i++){
            inventory.setItem(i, redWool);
        }
        player.openInventory(inventory);
    }
}
