package server;

public class ServerMain {
    public static void main(String[] args) {
        int serverPort = 8899;
        Server server = new Server(serverPort);
        server.start();
    }
}
