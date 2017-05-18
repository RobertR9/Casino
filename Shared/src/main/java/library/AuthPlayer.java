package library;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashSet;

public interface AuthPlayer {

	public BigDecimal getBalance() throws RemoteException;
	
	public Bet makeBet(ServerGame g, BigDecimal amount, int payout, HashSet<Integer> winning, String description) throws RemoteException;

	public String withdraw(String address, BigDecimal amount) throws RemoteException;
	
	public void joinGame(ServerGame sg) throws RemoteException;

}
