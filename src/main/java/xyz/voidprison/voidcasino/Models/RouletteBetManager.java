package xyz.voidprison.voidcasino.Models;

import java.util.HashMap;
import java.util.Map;

public class RouletteBetManager {
    private final Map<String, Map<Integer, RouletteBet>> playerBets = new HashMap<>();

    // Toevoegen van een bet
    public void addBet(String playerName, int rouletteNumber, int betAmount, String color) {
        playerBets.computeIfAbsent(playerName, k -> new HashMap<>()).compute(rouletteNumber, (key, existingBet) -> {
                    if (existingBet == null) {
                        // Als er nog geen bet bestaat, maak een nieuwe
                        return new RouletteBet(rouletteNumber, betAmount, color);
                    } else {
                        // Als er al een bet bestaat, voeg het bedrag toe
                        existingBet.addAmount(betAmount);
                        return existingBet;
                    }
                });
    }

    public void clearPlayerBets(String playerName) {
        playerBets.remove(playerName);
    }

    //public String getTotalBetOnColor(String playerName, String color){
    //    Map<Integer, RouletteBet> playerBetsMap = playerBets.get(playerName);
    //    if (playerBetsMap != null){
    //        return playerBetsMap.get(color).getColor();
    //    }
    //}
    public void resetBetOnNumber(String playerName, int number) {
        Map<Integer, RouletteBet> playerBetsMap = playerBets.get(playerName);
        if (playerBetsMap != null) {
            playerBetsMap.remove(number);
        }
    }
    public int getTotalBetOnNumber(String playerName, int rouletteNumber) {
        Map<Integer, RouletteBet> playerBetsMap = playerBets.get(playerName);
        if (playerBetsMap != null && playerBetsMap.containsKey(rouletteNumber)) {
            return playerBetsMap.get(rouletteNumber).getAmount();
        }
        return 0;
    }

    public int getTotalBets(String playerName){
        if(playerBets.containsKey(playerName)){
            Map<Integer, RouletteBet> playerBetsMap = playerBets.get(playerName);
            return playerBetsMap.values().stream().mapToInt(RouletteBet::getAmount).sum();
        }
        return 0;
    }
    public String getPlayerBetsDebugInfo() {
        return playerBets.toString();
    }
}
