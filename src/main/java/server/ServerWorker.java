package server;

import query.Query;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerWorker extends Thread {
    private final Server server;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String userName;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | ClassNotFoundException e) {
            try {
                handleLogout();
            } catch (IOException ex) {
                System.out.println(userName + " s'est déconnecté");
                ex.printStackTrace();
            }
            server.getWorkers().remove(this);
        }
    }

    private void handleClientSocket() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(clientSocket.getInputStream());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());

        Query query;
        while ((query = (Query) ois.readObject()) != null) {
            String cmd = query.getCmd();
            switch (cmd) {
                case Query.LOGIN:
                    handleLogin(query);
                    break;
                case Query.LOGOUT:
                    handleLogout();
                    return;
                case Query.SEND:
                    handleSend(query);
                    break;
                case Query.REQUEST:
                    handleRequest(query);
                    break;
                case Query.ACCEPT_REQUEST:
                    handleAcceptRequest(query);
                    break;
                case Query.DECLINE_REQUEST:
                    handleDeclineRequest(query);
                    break;
            }
        }
    }

    private void handleDeclineRequest(Query query) throws IOException {
        Query response = new Query();
        for (ServerWorker worker : server.getWorkers()) {
            if (query.getReceiverLogin().equals(worker.userName)) {
                response.setCmd(Query.RECEIVE_DECLINE_REQUEST);
                response.setSenderLogin(query.getReceiverLogin());
                response.setReceiverLogin(query.getSenderLogin());
                worker.send(response);
            }
        }
    }

    private void handleAcceptRequest(Query query) throws IOException {
        Query response = new Query();
        for (ServerWorker worker : server.getWorkers()) {
            if (query.getReceiverLogin().equals(worker.userName)) {
                response.setCmd(Query.RECEIVE_ACCEPT_REQUEST);
                response.setSenderLogin(query.getReceiverLogin());
                response.setReceiverLogin(query.getSenderLogin());
                worker.send(response);
            }
        }
    }

    private void handleRequest(Query query) throws IOException {
        Query response = new Query();
        for (ServerWorker worker : server.getWorkers()) {
            if (query.getReceiverLogin().equals(worker.userName)) {
                response.setCmd(Query.RECEIVE_REQUEST);
                response.setSenderLogin(query.getReceiverLogin());
                response.setReceiverLogin(query.getSenderLogin());
                worker.send(response);
            }
        }
    }

    private void handleSend(Query query) throws IOException {
        Query response = new Query();
        for (ServerWorker worker : server.getWorkers()) {
            if (query.getSenderLogin().equals(worker.userName)) {
                response.setCmd(Query.RECEIVE);
                worker.send(response);
            }
        }
    }

    private void handleLogout() throws IOException {
        List<ServerWorker> workers = server.getWorkers();
        for (ServerWorker worker : workers) {
            if (worker.userName != null) {
                if (!userName.equals(worker.userName)) {
                    Query query = new Query();
                    query.setCmd(Query.OFFLINE);
                    send(query);
                }
            }
        }

        for (ServerWorker worker : workers) {
            if (worker.userName != null) {
                Query query = new Query();
                query.setCmd(Query.OFFLINE);
                worker.send(query);
            }
        }
        workers.remove(this);
        clientSocket.close();
    }

    private void handleLogin(Query query) throws IOException {
        userName = query.getSenderLogin();
        Query response = new Query();
        response.setCmd(Query.LOGIN_OK);
        response.setSenderLogin(userName);
        oos.writeObject(response);

        System.out.println(String.format("%s est connecté", userName));

        List<ServerWorker> workers = server.getWorkers();

        for (ServerWorker worker : workers) {
            if (worker.userName != null) {
                if (!userName.equals(worker.userName)) {
                    Query query1 = new Query();
                    query1.setCmd(Query.ONLINE);
                    query1.setSenderLogin(worker.userName);
                    send(query1);
                }
            }
        }

        for (ServerWorker worker : workers) {
            if (worker.userName != null) {
                if (!userName.equals(worker.userName)) {
                    Query query1 = new Query();
                    query1.setCmd(Query.ONLINE);
                    query1.setSenderLogin(userName);
                    worker.send(query1);
                }
            }
        }
    }

    private void send(Query query) throws IOException {
        if (userName != null) {
            oos.writeObject(query);
        }
    }

    public String getUserName() {
        return userName;
    }
}
