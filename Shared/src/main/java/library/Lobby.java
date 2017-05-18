package library;

import java.io.Serializable;
import java.util.List;

public interface Lobby extends Serializable {

    public List<ServerGame> lookupGame(String name, int limit);

    public ServerGame addGame(String name);

}
