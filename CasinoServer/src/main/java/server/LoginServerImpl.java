//package server;
//
//
//import library.Lobby;
//
//import javax.jms.Session;
//import java.rmi.RemoteException;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//
//
//public class LoginServerImpl  {
//
////	private SessionFactory sessionFactory;
//	private Lobby lobby;
////	private BitcoindInterface bitcoin;
//
//	protected LoginServerImpl( SessionFactory sessionFactory)  {
//		super();
//		this.sessionFactory = sessionFactory;
//		this.lobby = new LobbyImpl(sessionFactory);
//	}
//
//	@Override
//	public synchronized boolean register(String username, String password)  {
//
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		boolean available = ((Long) session.createCriteria(AuthPlayerImpl.class)
//				.add(Restrictions.eq("username", username))
//				.setProjection(Projections.rowCount())
//				.uniqueResult()) == 0;
//
//		session.getTransaction().commit();
//
//		System.out.println("Available " + available);
//
//		if (available) {
//
//			/* Hash/salt password */
//			String hashedPassword;
//			try {
//				hashedPassword = PasswordHash.createHash(password);
//			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//				session.close();
//				e.printStackTrace();
//				return false;
//			}
//
//			if(sessionFactory == null){
//				System.out.println("wtf");
//			}
//			AuthPlayerImpl p = new AuthPlayerImpl(username, hashedPassword);
//
//			session.beginTransaction();
//			session.save(p);
//			session.getTransaction().commit();
//			session.close();
//		}
//
//		return available;
//	}
//
//	@Override
//	public Object[] login(String username, String password)  {
//
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		AuthPlayerImpl p = (AuthPlayerImpl) session
//				.createCriteria(AuthPlayerImpl.class)
//				.add(Restrictions.eq("username", username))
//				.uniqueResult();
//
//		session.getTransaction().commit();
//		session.close();
//
//		if (p == null) {
//			System.err.printf("No user: %s", username);
//			return null;
//		}
//
//		try {
//			if (PasswordHash.validatePassword(password, p.getPassword())) {
//				System.out.println("valid login");
//				return new Object[] { p, lobby };
//			} else {
//				System.out.println("wrong password");
//				return null;
//			}
//		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//			System.out.println("err");
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//}
