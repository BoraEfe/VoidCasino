package xyz.voidprison.voidcasino.Models;

import org.bukkit.entity.Player;

public class Bet {
    private String betCreator;
    private String selectedColor;
    private long amount;

    public Bet(String betCreator, String selectedColor, long amount){
        this.betCreator = betCreator;
        this.selectedColor = selectedColor;
        this.amount = amount;
    }

    public String getBetCreator(){
        return betCreator;
    }

    public String getSelectedColor(){
        return selectedColor;
    }

    public String getFormatAmount(){
        if( amount  >= 1000000L && amount < 1000000000L){
            return String.format("%dM",amount / 1000000);
        }
        else if ( amount >= 1000000000L && amount < 1000000000000L){
            return String.format("%.2fB", amount /1000000000.0);
        }
        return String.valueOf(amount);
    }
}
