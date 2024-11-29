package xyz.voidprison.voidcasino.Functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.voidprison.voidcasino.VoidCasino;

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

        setupGUI();
        startSpin();
    }

    private void setupGUI() {
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, createRouletteItem(Material.BLACK_STAINED_GLASS_PANE, " "));
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
        if (number == 0) return Material.GREEN_WOOL;
        if (number % 2 == 0) return Material.RED_WOOL;
        return Material.BLACK_WOOL;
    }

    private void startSpin() {
        new BukkitRunnable() {
            private int spinTicks = 0;
            private int currentIndex = random.nextInt(rouletteWheel.length);

            @Override
            public void run() {
                if (spinTicks >= 60) {
                    int winningNumber = rouletteWheel[currentIndex - 1];
                    announceResult(winningNumber);
                    cancel();
                    return;
                }

                for (int i = 0; i < 9; i++) {
                    int index = (currentIndex + i - 4 + rouletteWheel.length) % rouletteWheel.length;
                    int number = rouletteWheel[index];
                    Material colorMaterial = getColorMaterial(number);

                    int slot = 9 + i; // Posities in de GUI (9 t/m 17)
                    gui.setItem(slot, createRouletteItem(colorMaterial, ChatColor.GOLD + "Number: " + number));
                }

                int middleNumber = rouletteWheel[currentIndex];
                Material middleMaterial = getColorMaterial(middleNumber);
                gui.setItem(13, createRouletteItem(middleMaterial, ChatColor.GOLD + "number: " + middleNumber));

                player.playSound(player.getLocation(), "block.note_block.pling", 1f, 1f);

                currentIndex = (currentIndex + 1) % rouletteWheel.length;

                spinTicks++;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("voidCasino"), 0L, 2L); // Start direct, interval: 2 ticks
    }


    private void announceResult(int winningNumber) {
        String color = (winningNumber == 0) ? "green" :
                (winningNumber % 2 == 0 ? "red" : "black");

        player.sendMessage(ChatColor.GREEN + "The winning number is " + winningNumber + " (" + color + ")!");

    }
}
