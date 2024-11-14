package xyz.voidprison.voidcasino.Models;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BetManager {
    private static Map<String, Bet> activeBets;

    public BetManager(){
        if (activeBets == null) {
            activeBets = new HashMap<>();
        }
    }
    public void addBet(Bet bet){
        activeBets.put(bet.getBetCreator(),bet);

    }

    public void removeBet(Player playerName){
        activeBets.remove(playerName.getName());
    }
    public boolean hasBet(String playerName){
        return activeBets.containsKey(playerName);
    }
    public Collection<Bet> getAllBets(){
        return activeBets.values();
    }

}