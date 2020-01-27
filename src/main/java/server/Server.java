package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private int serverPort;
    private List<ServerWorker> workers = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkers() {
        return workers;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                System.out.println("En attente d'une connexion !!!");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté " + clientSocket.getPort());
                ServerWorker serverWorker = new ServerWorker(this, clientSocket);
                workers.add(serverWorker);
                serverWorker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
