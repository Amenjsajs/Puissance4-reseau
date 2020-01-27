package client;

import query.Query;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private String login;
    private String host;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<SendListener> sendListeners = new ArrayList<>();
    private ArrayList<RequestStatusListener> requestStatusListeners = new ArrayList<>();

    public Client(String host, int serverPort) {
        this.host = host;
        this.serverPort = serverPort;
    }

    public void send(Query query) throws IOException {
        oos.writeObject(query);
    }

    public boolean login(String login) throws IOException, ClassNotFoundException {
        this.login = login;
        Query query = new Query();
        query.setSenderLogin(login);
        query.setCmd(Query.LOGIN);
        oos.writeObject(query);

        Query response = (Query) ois.readObject();

        if(Query.LOGIN_OK.equals(response.getCmd())){
            System.out.println(response.getSenderLogin() + ", vous êtes connecté");
            startSendReader();
            return true;
        }
        return false;
    }

    public void resquest(String login2) throws IOException {
        Query query = new Query();
        query.setSenderLogin(login);
        query.setReceiverLogin(login2);
        query.setCmd(Query.REQUEST);
        oos.writeObject(query);
    }

    public void acceptRequest(String login2) throws IOException {
        Query query = new Query();
        query.setSenderLogin(login);
        query.setReceiverLogin(login2);
        query.setCmd(Query.ACCEPT_REQUEST);
        oos.writeObject(query);
    }

    public void declineRequest(String login2) throws IOException {
        Query query = new Query();
        query.setSenderLogin(login);
        query.setReceiverLogin(login2);
        query.setCmd(Query.DECLINE_REQUEST);
        oos.writeObject(query);
    }

    public void logout() throws IOException {
        Query query = new Query();
        query.setCmd(Query.LOGOUT);
        oos.writeObject(query);
    }

    private void startSendReader() {
        Thread t = new Thread(this::readSendLooop);
        t.start();
    }

    private void readSendLooop() {
        try {
            Query query;
            while ((query = (Query) ois.readObject()) != null) {
                String cmd = query.getCmd();
                switch (cmd){
                    case Query.ONLINE:
                        handleOnline(query);
                        break;
                    case Query.OFFLINE:
                        handleOffline(query);
                        break;
                    case Query.RECEIVE:
                        handleSend(query);
                        break;
                    case Query.RECEIVE_REQUEST:
                        handleReceiveRequest(query);
                        break;
                    case Query.RECEIVE_ACCEPT_REQUEST:
                        handleReceiveAcceptRequest(query);
                        break;
                    case Query.RECEIVE_DECLINE_REQUEST:
                        handleReceiveDeclineRequest(query);
                        break;
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleReceiveDeclineRequest(Query query) {
        for (RequestStatusListener listener: requestStatusListeners){
            listener.decline(query.getReceiverLogin());
        }
    }

    private void handleReceiveAcceptRequest(Query query) {
        for (RequestStatusListener listener: requestStatusListeners){
            listener.accept(query.getReceiverLogin());
        }
    }

    private void handleReceiveRequest(Query query) {
        for (RequestStatusListener listener: requestStatusListeners){
            listener.receive(query.getReceiverLogin());
        }
    }

    private void handleSend(Query query) {
        for (SendListener listener : sendListeners) {
            listener.onSend(query);
        }
    }

    private void handleOffline(Query query) {
        String senderLogin = query.getSenderLogin();
        if (senderLogin != null) {
            for (UserStatusListener listener : userStatusListeners) {
                listener.offline(senderLogin);
            }
        }
    }

    private void handleOnline(Query query) {
        String sendLogin = query.getSenderLogin();
        if (sendLogin != null) {
            for (UserStatusListener listener : userStatusListeners) {
                listener.online(sendLogin);
            }
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(host, serverPort);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLogin() {
        return login;
    }

    public void addUserStatusListener(UserStatusListener listerner) {
        userStatusListeners.add(listerner);
    }

    public void removeUserStatusListener(UserStatusListener listerner) {
        userStatusListeners.remove(listerner);
    }

    public void addSendListener(SendListener listener){
        sendListeners.add(listener);
    }

    public void removeSendListener(SendListener listener){
        sendListeners.remove(listener);
    }

    public void addRequestStatusListener(RequestStatusListener listener){
        requestStatusListeners.add(listener);
    }

    public void removeRequestStatusListener(RequestStatusListener listener){
        requestStatusListeners.remove(listener);
    }
}
