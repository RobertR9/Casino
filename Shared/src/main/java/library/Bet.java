package library;

import java.util.Set;


public class Bet {

    private static final long serialVersionUID = 1L;

    private Player player;

    private int payout;

    private Double amount;

    private Set<Integer> winning;

    private String description;

    public Bet(Double amount, int payout, Set<Integer> winning, String description, Player player) {
        this.payout = payout;
        this.amount = amount;
        this.winning = winning;
        this.description = description;
        this.player = player;
    }

    public int getPayout() {
        return payout;
    }

    public Double getAmount() {
        return amount;
    }

    public boolean cameTrue(int resultOfSpin) {
        return winning.contains(resultOfSpin);
    }

    public String getDescription() {
        return description;
    }

    public Player getPlayer() {
        return this.player;
    }
}

