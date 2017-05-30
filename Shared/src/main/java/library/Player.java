package library;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Player implements Serializable {

    private Long id;
    private String username;
    private Double balance = 1000.00;
    private Set<Bet> bets = new HashSet<>();


    public Player(Long id, String username, Double balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Bet makeBet(Double amount, int payout, HashSet<Integer> winning, String description) {
        Bet bet = new Bet(amount, payout, winning, description, this);
        bets.add(bet);
        balance -= bet.getAmount();
        return bet;
    }

}
