package library;

import java.io.Serializable;
import java.util.Set;

public class Bet implements Serializable {
    private Player player;
    private int payout;
    private Double amount;
    private Set<Integer> numbers;
    private String description;
    private String status;

    public Bet(Double amount, int payout, Set<Integer> numbers, String description, Player player) {
        this.payout = payout;
        this.amount = amount;
        this.numbers = numbers;
        this.description = description;
        this.player = player;
        this.status = "pending";
    }

    public int getPayout() {
        return payout;
    }

    public Double getAmount() {
        return amount;
    }

    public boolean cameTrue(int resultOfSpin) {
        return numbers.contains(resultOfSpin);
    }

    public String getDescription() {
        return description;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "player=" + player +
                ", payout=" + payout +
                ", amount=" + amount +
                ", numbers=" + numbers +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

