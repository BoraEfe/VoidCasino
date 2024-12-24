package xyz.voidprison.voidcasino.Functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.voidprison.voidcasino.Models.RouletteBet;
import xyz.voidprison.voidcore.Data.Stars;
import org.bukkit.enchantments.Enchantment;


import java.util.Map;
import java.util.Random;

public class RouletteSpinAnimationGUI {

    private final Player player;
    public boolean isSpinning = false;
    private final long totalBet;
    private final Map<Integer, Long> rouletteNumbers;
    private final String chosenColors;
    private final Inventory gui;
    private final Random random = new Random();
    private int[] rouletteWheel = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36
    };

    public RouletteSpinAnimationGUI(Player betCreator, long totalBet, Map<Integer, Long> rouletteNumbers, String colors){

        gui = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&5&lRoulette Spin"));

        this.player = betCreator;
        this.totalBet = totalBet;
        this.rouletteNumbers = rouletteNumbers;
        this.chosenColors = colors;

        setupGUI();
        startSpin();
    }

    private void setupGUI() {
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, createRouletteItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            gui.setItem(4, createRouletteItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
            gui.setItem(22, createRouletteItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        }
        player.openInventory(gui);
    }

    private ItemStack createRouletteItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    private Material getColorMaterial(int number) {
        int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        int[] blackNumbers = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

        if (number == 0){
            return Material.GREEN_WOOL;
        }
        for(int red : redNumbers){
            if (number == red){
                return Material.RED_WOOL;
            }
        }
        for(int black : blackNumbers){
            if(number == black){
                return Material.BLACK_WOOL;
            }
        }
        return Material.WHITE_WOOL;
    }

    private void startSpin() {
        isSpinning = true;
        new BukkitRunnable() {
            private int spinTicks = 0;
            private int currentIndex = random.nextInt(rouletteWheel.length);



            @Override
            public void run() {
                if (spinTicks >= 60) {
                    int winningNumber = rouletteWheel[currentIndex - 1];
                    checkForPlayersEarnings(rouletteNumbers, winningNumber);
                    isSpinning = false;
                    cancel();
                    return;
                }

                for (int i = 0; i < 9; i++) {
                    int index = (currentIndex + i - 4 + rouletteWheel.length) % rouletteWheel.length;
                    int number = rouletteWheel[index];
                    Material colorMaterial = getColorMaterial(number);

                    int slot = 9 + i; // Posities in de GUI (9 t/m 17)
                    gui.setItem(slot, createRouletteItem(colorMaterial, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Number: " + number));
                }
                int middleNumber = rouletteWheel[currentIndex];
                Material middleMaterial = getColorMaterial(middleNumber);

                ItemStack middleItem = createRouletteItem(middleMaterial,ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "number: " + middleNumber);
                ItemMeta middleMeta = middleItem.getItemMeta();
                if(middleMeta !=null){
                    middleMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1,true);
                    middleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    middleItem.setItemMeta(middleMeta);
                }

                gui.setItem(13, middleItem);

                player.playSound(player.getLocation(), "block.note_block.pling", 0.5f, 0.5f);

                currentIndex = (currentIndex + 1) % rouletteWheel.length;

                spinTicks++;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("voidCasino"), 0L, 2L);
    }


    private void announceResult(int winningNumber, Player player) {

        int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        int[] blackNumbers = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

        if(winningNumber == 0){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lTHE WINNING NUMBER IS: &a&l" + winningNumber));
            return;
        }
        if(isNumberInArray(winningNumber, redNumbers)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lTHE WINNING NUMBER IS: &c&l" + winningNumber));
            return;
        }
        if(isNumberInArray(winningNumber, blackNumbers)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lTHE WINNING NUMBER IS: &8&l" + winningNumber));
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "Error: Invalid winning number!");
    }
    private boolean isNumberInArray(int number, int[] array) {
        for (int value : array) {
            if (value == number) {
                return true;
            }
        }
        return false;
    }
    private void checkForPlayersEarnings(Map<Integer, Long> rouletteNumbers, int winningNumber) {
        int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        int[] blackNumbers = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

        long totalEarnings = 0;
        long totalProfit = 0;
        long betAmount = 0;

        if (rouletteNumbers.containsKey(winningNumber)) {
            betAmount = rouletteNumbers.get(winningNumber);
            totalEarnings = (betAmount * 35) + betAmount;
            totalProfit = totalEarnings - totalBet;

           // player.sendMessage(ChatColor.GREEN + "You won! Bet on number " + winningNumber + " with " + betAmount + " stars.");
        }
        else if (rouletteNumbers.containsKey(37) && rouletteNumbers.get(37) > 0 && isNumberInArray(winningNumber, redNumbers)) {
            betAmount = rouletteNumbers.get(37);
            totalEarnings = betAmount * 2;
            totalProfit = totalEarnings - totalBet;
        }
        else if (rouletteNumbers.containsKey(38) && rouletteNumbers.get(38) > 0 && isNumberInArray(winningNumber, blackNumbers)) {
            betAmount = rouletteNumbers.get(38);
            totalEarnings = betAmount * 2;
            totalProfit = totalEarnings - totalBet;
        }

        String formattedStake = RouletteBet.getFormatAmount(totalBet);
        String formattedEarnings = RouletteBet.getFormatAmount(totalEarnings);
        String formattedProfit = RouletteBet.getFormatAmount(totalProfit);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&D&l&nTOTAL GAME RESULTS"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l=================="));
        announceResult(winningNumber, player);

        totalProfit = totalEarnings - totalBet;
        if(totalProfit <= 0){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lYOU &4&lDIDN'T &d&lWIN THIS ROUND"));
        }
        if (totalProfit > 0){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lYOU &2&lHAVE WON &d&lTHIS ROUND"));

        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lTOTAL STAKE: &e&l" + formattedStake));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lTOTAL EARNINGS: &e&l" + formattedEarnings));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lNET PROFIT: &e&l" + formattedProfit));

        if (totalEarnings >= 0) {
            Stars.giveStars(player, totalEarnings);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lYou have been given: &e&l" + formattedEarnings + " " + "&d&lStars"));
        }
    }
}
