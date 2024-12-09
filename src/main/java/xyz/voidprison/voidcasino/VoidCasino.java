package xyz.voidprison.voidcasino;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.voidprison.voidcasino.Commands.ListOfCoinflipBetsGUICommand;
import xyz.voidprison.voidcasino.Commands.SetRouletteBetsGUICommand;
import xyz.voidprison.voidcasino.Listeners.ListOfCoinflipBetsGUIListener;
import xyz.voidprison.voidcasino.Listeners.SetRouletteBetsGUIListener;
import xyz.voidprison.voidcasino.Models.BetManager;

public final class VoidCasino extends JavaPlugin {

    @Override
    public void onEnable() {
        BetManager betManager = new BetManager(this);
        ListOfCoinflipBetsGUICommand  listOfCoinflipBetsGUICommand = new ListOfCoinflipBetsGUICommand();
        // Plugin startup logic

        this.getCommand("coinfliplist").setExecutor(listOfCoinflipBetsGUICommand);
        getServer().getPluginManager().registerEvents(new ListOfCoinflipBetsGUIListener(betManager, listOfCoinflipBetsGUICommand), this);


        this.getCommand("playroulette").setExecutor(new SetRouletteBetsGUICommand());
        getServer().getPluginManager().registerEvents(new SetRouletteBetsGUIListener(), this);
    }
}
