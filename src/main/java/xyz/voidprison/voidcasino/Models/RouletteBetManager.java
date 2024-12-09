package xyz.voidprison.voidcasino.Models;

import java.util.*;

public class RouletteBetManager {
    private final Map<String, RouletteBet> playerBets = new HashMap<>();

    public void addBet(String playerName, int rouletteNumber, int betAmount, List <String> color) {

        playerBets.computeIfAbsent(playerName, k -> new RouletteBet(0, null, color))
                .addNumberBet(rouletteNumber, betAmount);
    }

    public void clearPlayerBets(String playerName) {
        playerBets.remove(playerName);
    }

    public List<String> getColor(String playerName){
        RouletteBet bet = playerBets.get(playerName);
        return bet != null ? bet.getColor() : null;
    }

    public void resetBetOnNumber(String playerName, int number) {
        RouletteBet bet = playerBets.get(playerName);
        if (bet != null) {
            long removedAmount = bet.getBetForNumber(number);
            bet.getNumbersBetOn().remove(number);
            bet.adjustTotalBetAmount(bet.getTotalBetAmount() - removedAmount);
        }
    }

    public long getTotalBetOnNumber(String playerName, int number){
        RouletteBet bet = playerBets.get(playerName);

        if(bet != null){
            return bet.getBetForNumber(number);
        }
        return 0L;
    }

    public boolean canPlaceBetOnNumber(String playerName, int number, int betAmount) {
        long currentBet = getTotalBetOnNumber(playerName, number);

        return (currentBet + betAmount) <= 50_000_000L;
    }
    public boolean canPlaceBetOnColor(String playerName, int number, int betAmount){
        long currentBet = getTotalBetOnNumber(playerName, number);
        return (currentBet + betAmount) <= 250_000_000L;
    }
    public long getTotalBetAmount(String playerName){
        RouletteBet bet = playerBets.get(playerName);
        return bet !=null ? bet.getTotalBetAmount() : 0L;
    }
    public Map<Integer, Long> getNumbersBetOn(String playerName) {
        RouletteBet bet = playerBets.get(playerName);
        if (bet != null) {
            return new HashMap<>(bet.getNumbersBetOn());
        }
        return new HashMap<>();
    }
}
