package xyz.voidprison.voidcasino.Models;

public class RouletteBet {
    private final int number;
    private int amount;
    private String color;

    public RouletteBet(int numbers, int amount, String color){
        this.number = numbers;
        this.amount = amount;
        this.color = (color != null) ? color.toLowerCase() : null;
    }
    public int getNumber(){
        return  number;
    }
    public int getAmount(){
        return amount;
    }
    public String getColor() {
        return color;
    }
    public void addAmount(int amount){
        this.amount += amount;
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

