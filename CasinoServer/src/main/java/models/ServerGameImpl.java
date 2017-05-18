package models;

import library.Bet;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;


public class ServerGameImpl {

    private static final long serialVersionUID = -6114105589582888658L;

    private Long id;

    private String name;

    private Set<AuthPlayerImpl> players = new HashSet<AuthPlayerImpl>();

    private Set<BetImpl> bets = new HashSet<BetImpl>();

    public ServerGameImpl() throws RemoteException {
    }

    public ServerGameImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ServerGameImpl)) {
            return false;
        } else {
            ServerGameImpl other = (ServerGameImpl) o;
            try {
                return other.getName().equals(name);
            } catch (RemoteException e) {
                return false;
            }
        }
    }

    public int hashCode() {
        return name.hashCode();
    }

    public Set<OtherPlayer> getPlayers() {
        Set<OtherPlayer> s = new HashSet<>();
        for (AuthPlayerImpl a : players) {
            s.add((OtherPlayer) a);
        }
        return s;
    }

    public Long getId() {
        return id;
    }

    public Set<Bet> getBets() {
        Set<Bet> s = new HashSet<>();
        for (BetImpl b : bets) {
            s.add(b);
        }
        return s;
    }
}
