package library;

import java.io.Serializable;
import java.util.Set;

public interface ServerGame extends Serializable {

    public Long getId();

    public String getName();

    public Set<Player> getPlayers();

    public Set<Bet> getBets();

}
