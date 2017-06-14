package models;

import library.Bet;
import library.Player;

import java.util.HashSet;
import java.util.Set;


public class ServerGame {

    private static final long serialVersionUID = -6114105589582888658L;

    private Long id;

    private String name;

    private Set<Player> players = new HashSet<>();

    private Set<Bet> bets = new HashSet<>();

    public ServerGame() {
    }

    public ServerGame(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ServerGame)) {
            return false;
        } else {
            ServerGame other = (ServerGame) o;
            try {
                return other.getName().equals(name);
            } catch (Exception e) {
                return false;
            }
        }
    }

    public int hashCode() {
        return name.hashCode();
    }

    public Set<Player> getPlayers() {
        Set<Player> s = new HashSet<>();
        for (Player a : players) {
            s.add(a);
        }
        return s;
    }

    public Long getId() {
        return id;
    }

    public Set<Bet> getBets() {
        Set<Bet> s = new HashSet<>();
        for (Bet b : bets) {
            s.add(b);
        }
        return s;
    }
}
