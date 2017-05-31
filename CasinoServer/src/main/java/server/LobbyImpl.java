//package server;
//
//import library.Lobby;
//import library.ServerGame;
//import models.ServerGameImpl;
//
//public class LobbyImpl implements Lobby {
//
//    private static final long serialVersionUID = -3245535716307120524L;
//
//    protected LobbyImpl() {
//        super();
//    }
//
//    @Override
//    public ServerGame addGame(String name) {
//
//        ServerGameImpl g = new ServerGameImpl(name);
//
////        Session session = sessionFactory.openSession();
////        session.beginTransaction();
////        session.save(g);
////        session.getTransaction().commit();
////        session.close();
//
//        return (ServerGame) g;
//    }
//
//}
