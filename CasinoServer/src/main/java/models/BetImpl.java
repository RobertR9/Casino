package models;

import library.Player;

import java.math.BigDecimal;
import java.util.Set;


public class BetImpl  {

    private static final long serialVersionUID = 1L;

    private long betId;

    private AuthPlayerImpl player;

    private ServerGameImpl serverGame;

    private int payout;

    private BigDecimal amount;

    private Set<Integer> winning;

    private String description;


    public BetImpl(BigDecimal amount, int payout, Set<Integer> winning, String description) {
        this.payout = payout;
        this.amount = amount;
        this.winning = winning;
        this.description = description;
    }

    public int getPayout() {
        return payout;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean cameTrue(int resultOfSpin) {
        return winning.contains(resultOfSpin);
    }

    public String getDescription() {
        return description;
    }

    public Player getPlayer() {
        return null;
    }

    public ServerGameImpl getServerGame() {
        return serverGame;
    }

}

