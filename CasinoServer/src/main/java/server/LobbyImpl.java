//package server;
//
//import library.Lobby;
//import library.ServerGame;
//
//import javax.jms.Connection;
//import javax.jms.Session;
//import java.rmi.RemoteException;
//import java.util.List;
//
//public class LobbyImpl implements Lobby {
//
//	private static final long serialVersionUID = -3245535716307120524L;
//
//	protected LobbyImpl() throws RemoteException {
//		super();
//	}
//
//
//	/**
//	 * Return first <limit> games matching <name>
//	 */
//	@Override
//	public List<ServerGame> lookupGame(String name, int limit) throws RemoteException {
//
//		Session session = new Connection().createSession();
//		session.beginTransaction();
//
//		@SuppressWarnings("unchecked")
//		List<ServerGame> games = (List<ServerGame>)session
//			.createCriteria(ServerGameImpl.class)
//			.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE))
//			.setMaxResults(limit)
//			.list();
//
//		session.getTransaction().commit();
//		session.close();
//
//		return games;
//	}
//
//	@Override
//	public ServerGame addGame(String name) throws RemoteException {
//
//		ServerGameImpl g = new ServerGameImpl(name);
//
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		session.save(g);
//		session.getTransaction().commit();
//		session.close();
//
//		return g;
//	}
//
//}
