package xyz.voidprison.voidcasino.Functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Random;

public class RouletteSpinAnimationGUI {

    private final Player player;
    private final long totalBet;
    private final int rouletteNumbers;
    private final String chosenColors;
    private final Inventory gui;
    private final Random random = new Random();
    private int[] rouletteWheel = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36
    };

    public RouletteSpinAnimationGUI(Player betCreator, long totalBet, int rouletteNumbers, String colors){

        gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Roulette Spin");

        this.player = betCreator;
        this.totalBet = totalBet;
        this.rouletteNumbers = rouletteNumbers;
        this.chosenColors = colors;

        startSpin();
    }

    public void startSpin(){

    }
}
