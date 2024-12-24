package xyz.voidprison.voidcasino.Models;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.voidprison.voidcasino.Commands.ListOfCoinflipBetsGUICommand;
import xyz.voidprison.voidcasino.Listeners.ListOfCoinflipBetsGUIListener;
import xyz.voidprison.voidcore.Data.Stars;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BetManager{

    private final HashMap<UUID, Long> playerStake = new HashMap<>();
    private final File betsFile;
    private static FileConfiguration config = null;

    public BetManager(JavaPlugin plugin){
        File pluginDataFolder = new File(plugin.getServer().getPluginManager().getPlugin("VoidCasino").getDataFolder().getParentFile(), "VoidCasino");

        if(!pluginDataFolder.exists()){
            pluginDataFolder.mkdirs();
        }

        betsFile = new File(pluginDataFolder, "CoinFlipBets.yml");

        if(!betsFile.exists()){
            try{
                betsFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(betsFile);
        loadBets();
    }

    public void putTemporaryStakeInHashMap(UUID playerUUID, long stake){
        playerStake.put(playerUUID, stake);
    }

    public long getStake(UUID playerUUID){
        return playerStake.getOrDefault(playerUUID, 0L);
    }
    public void deleteStake(UUID playerUUID){
        playerStake.remove(playerUUID);
    }
    public String getFormatAmount(long amount) {
        if (amount >= 1000000L && amount < 1000000000L) {
            return String.format("%dM", amount / 1000000);
        } else if (amount >= 1000000000L && amount < 1000000000000L) {
            return String.format("%.2fB", amount / 1000000000.0);
        }
        return String.valueOf(amount);
    }


    public void addBet(Bet bet){

        String creatorName = bet.getBetCreator();

        if(!config.contains("bets")){
            config.createSection("bets");
        }

        if(hasBet(creatorName)){
            long oldAmount = getBetAmount(bet.getBetCreator());
            String oldFormattedAmount = getFormatAmount(oldAmount);
            Player creator = Bukkit.getPlayer(creatorName);

            if(creator != null){
                Stars.giveStars(creator, oldAmount);
                creator.sendMessage(ChatColor.LIGHT_PURPLE + "Your previous bet of " + oldFormattedAmount +  ChatColor.YELLOW + " stars " + ChatColor.LIGHT_PURPLE + "has been refunded.");
            }
            config.set("bets." + creatorName, null);
        }

        config.set("bets." + bet.getBetCreator() + ".selectedColor", bet.getSelectedColor());
        config.set("bets." + bet.getBetCreator() + ".amount", bet.getAmount());
        saveConfig();

        ListOfCoinflipBetsGUICommand.checkForPlayersWithListOpen();
    }

    public void removeBet(Player player){
        String playerName = player.getName();

        long amount = config.getLong("bets." + playerName + ".amount");
        config.set("bets." + playerName, null);
        Stars.giveStars(player, amount);
        saveConfig();

        ListOfCoinflipBetsGUICommand.checkForPlayersWithListOpen();
    }
    public void deleteBetOnStart(Player player){
        String playerName = player.getName();
        config.set("bets." + playerName, null);
        saveConfig();

        ListOfCoinflipBetsGUICommand.checkForPlayersWithListOpen();
    }

    public long getBetAmount(String playerName){
        if(config.contains("bets." + playerName + ".amount")){
            return config.getLong("bets." + playerName + ".amount");
        }
        return 0L;
    }
    public String getSelectedColor(String playerName){
        if(config.contains("bets." + playerName + ".selectedColor")){
            return config.getString("bets." + playerName + ".selectedColor");
        }
        return "";
    }

    private void saveConfig() {
        try {
            config.save(betsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean hasBet(String playerName){
        return config.contains("bets." + playerName);
    }


    private void loadBets(){
        if(config.contains("bets")){
            for(String creator : config.getConfigurationSection("bets").getKeys(false)){
                String selectedColor = config.getString("bets." + creator + ".selectedColor");
                long amount = config.getLong("bets." + creator + ".amount");
                Bet bet = new Bet(creator, selectedColor, amount);
            }
        }
    }

    public static List<Bet> getBetsFromYaml(){
        List<Bet> bets = new ArrayList<>();
        if(config.contains("bets")){
            for(String creator : config.getConfigurationSection("bets").getKeys(false)){
                String selectedColor = config.getString("bets." + creator + ".selectedColor");
                long amount = config.getLong("bets." + creator + ".amount");
                bets.add(new Bet(creator, selectedColor, amount));
            }
        }
        return bets;
    }
}