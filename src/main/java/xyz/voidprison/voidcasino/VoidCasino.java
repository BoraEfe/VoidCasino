package xyz.voidprison.voidcasino;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.voidprison.voidcasino.Commands.ListOfCoinflipBetsGUICommand;
import xyz.voidprison.voidcasino.Commands.SetRouletteBetsGUICommand;
import xyz.voidprison.voidcasino.Listeners.ListOfCoinflipBetsGUIListener;
import xyz.voidprison.voidcasino.Listeners.SetRouletteBetsGUIListener;

public final class VoidCasino extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("coinfliplist").setExecutor(new ListOfCoinflipBetsGUICommand());
        getServer().getPluginManager().registerEvents(new ListOfCoinflipBetsGUIListener(), this);

        this.getCommand("playroulette").setExecutor(new SetRouletteBetsGUICommand());
        getServer().getPluginManager().registerEvents(new SetRouletteBetsGUIListener(), this);
    }
}
