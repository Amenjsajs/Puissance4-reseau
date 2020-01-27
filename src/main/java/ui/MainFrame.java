package ui;

import client.Client;
import client.RequestStatusListener;
import client.SendListener;
import client.UserStatusListener;
import query.Query;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame implements UserStatusListener, RequestStatusListener {
    private Container container;
    private Map<String, JButton> statusPaneMap = new HashMap<>();
    private Client client;

    public static void main(String[] args) {
        MainFrame frame = new MainFrame("Puissance 4");
    }

    MainFrame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        container = getContentPane();
        container.setLayout(new FlowLayout());

        client = new Client("localhost", 8899);
        client.connect();
        client.addUserStatusListener(this);
        client.addRequestStatusListener(this);

        String name = System.getProperty("user.name") + System.currentTimeMillis();
        try {
            if(client.login(name)){
                JLabel label = new JLabel(name);
                container.add(label);
                container.validate();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        setVisible(true);
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void online(String login) {
//        JButton statusButton = new JButton(login);
//        statusButton.addActionListener(e->{
//            try {
//                client.resquest(login);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });
//        container.add(statusButton);
//        container.validate();
//        statusPaneMap.put(login, statusButton);
    }

    @Override
    public void offline(String login) {
        JButton statusButton = statusPaneMap.get(login);
        container.remove(statusButton);
        container.validate();
        statusPaneMap.remove(login, statusButton);
    }


    @Override
    public void receive(String login) {
//        int re = JOptionPane.showConfirmDialog(this,String.format("%s vous invite à jouer",login),"Invitation",JOptionPane.YES_NO_OPTION);
//        try {
//            if(re == JOptionPane.YES_OPTION){
//                client.acceptRequest(login);
//            }else{
//                client.declineRequest(login);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void accept(String login) {
        JOptionPane.showMessageDialog(this,String.format("%s a acception votre invitation",login),"Réponse invitation",JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void decline(String login) {
        JOptionPane.showMessageDialog(this,String.format("%s a acception votre invitation",login),"Réponse invitation",JOptionPane.WARNING_MESSAGE);
    }
}
