package models;

import library.Bet;
import library.ServerGame;
import library.Transaction;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthPlayerImpl {

    private static final long serialVersionUID = 2772558738676419928L;

    private BigDecimal balance;
    private Set<TransactionImpl> transactions = new HashSet<TransactionImpl>();
    private Set<BetImpl> bets = new HashSet<BetImpl>();
    private Set<ServerGameImpl> games = new HashSet<ServerGameImpl>();

    public AuthPlayerImpl() throws RemoteException {
        super();
    }

    public AuthPlayerImpl(String username, String password) throws RemoteException {
        this.balance = BigDecimal.ZERO;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
//		List<Transaction> l = new ArrayList<Transaction>();
//		for (Transaction t : transactions) {
//			l.add(t);
//		}
        return null;
    }


    public String withdraw(String address, BigDecimal amount) throws RemoteException {

//		if (!bitcoin.validateaddress(address).isIsvalid()) {
//			throw new RemoteException("Invalid address");
//		} else if(balance.compareTo(amount) < 0){
//			throw new RemoteException("Insufficient funds");
//		}
//
//		String hash = bitcoin.sendtoaddress(address, amount);
//		System.out.println(hash);
//
        return "hash";
    }

    public Bet makeBet(ServerGame g, BigDecimal amount, int payout, HashSet<Integer> winning, String description)
            throws RemoteException {

        BetImpl b = new BetImpl(amount, payout, winning, description);
        bets.add(b);

//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		session.save(b);
//		session.update(this);
//		session.getTransaction().commit();
//		session.close();

        return b;
    }

    public void joinGame(ServerGame sg) throws RemoteException {

//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		ServerGameImpl game = (ServerGameImpl)session
//			.createCriteria(ServerGameImpl.class)
//			.add(Restrictions.idEq(sg.getId()))
//			.setMaxResults(1)
//			.uniqueResult();
//
//		if(games.contains(game)){
//			System.out.println("Already in game");
//		} else {
//			games.add(game);
//			session.update(this);
//		}
//
//		session.getTransaction().commit();
//		session.close();
//	}
    }
