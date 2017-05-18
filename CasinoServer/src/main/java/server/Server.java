package server;

import jms.TopicSenderGateway;
import library.Player;

import java.util.List;

public class Server {
    private List<Player> players;
    public static void main(String[] args) {
	        try {
            TopicSenderGateway topicSenderGateway = new TopicSenderGateway("CasinoLobby");
        } catch (Exception e) {
            System.err.println("Exception binding RouletteServer:");
            e.printStackTrace();
        }

    }
}
