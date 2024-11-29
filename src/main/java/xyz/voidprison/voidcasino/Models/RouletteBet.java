package xyz.voidprison.voidcasino.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RouletteBet {
    private long totalBetAmount;
    private List<String> colors;
    private Map<Integer, Long> numbersBetOn;

    public RouletteBet(long totalBetAmount, Map<Integer, Long> numbersBetOn, List<String> colors){
        this.totalBetAmount = totalBetAmount;
        this.colors = (colors != null && !colors.isEmpty()) ? colors : null;
        this.numbersBetOn = (numbersBetOn != null) ? numbersBetOn : new HashMap<>();
    }

    public void addNumberBet(int number, long amount ){
        numbersBetOn.merge(number, amount, Long::sum);
        totalBetAmount += amount;
    }
    public void addColor(String color) {
        if (color != null && !color.isEmpty()) {
            colors.add(color.toLowerCase());
        }
    }

    public long getBetForNumber(int number){
        return numbersBetOn.getOrDefault(number, 0L);
    }

    public void adjustTotalBetAmount(long totalBetAmount){
        this.totalBetAmount = totalBetAmount;
    }

    public long getTotalBetAmount(){
        return totalBetAmount;
    }
    public List<String> getColor(){
        return colors;
    }

    public Map<Integer, Long> getNumbersBetOn(){
        return numbersBetOn;
    }

    public static String getFormatAmount(long amount){
        if( amount  >= 1000000L && amount < 1000000000L){
            return String.format("%dM",amount / 1000000);
        }
        else if ( amount >= 1000000000L && amount < 1000000000000L){
            return String.format("%.2fB", amount /1000000000.0);
        }
        return String.valueOf(amount);
    }
}

