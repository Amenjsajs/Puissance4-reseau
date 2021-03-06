package ui;

import client.*;
import piece.Piece;
import query.Query;
import scene.Layer;
import scene.PieceScene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Frame extends JFrame implements UserStatusListener, RequestStatusListener, SendListener, MoveStatusListener {
    private Container container;
    private JPanel bottomPanel;
    private Map<String, JButton> userConnectedMap = new HashMap<>();
    private Client client;

    public Frame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        container = getContentPane();
        container.setLayout(new BorderLayout());

        client = new Client("localhost", 8899);
        client.connect();
        client.addUserStatusListener(this);
        client.addRequestStatusListener(this);
        client.addSendListener(this);
        client.addMoveStatusListener(this);

        bottomPanel = new JPanel();
        container.add(bottomPanel, BorderLayout.SOUTH);

        String name = System.getProperty("user.name") + System.currentTimeMillis();
        try {
            if (client.login(name)) {
                JLabel label = new JLabel(name);
                container.add(label, BorderLayout.NORTH);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(e.getKeyCode());
                try {
                    if (PieceScene.isTurn()) {
                        Query query = new Query();
                        query.setSenderLogin(client.getLogin());
                        query.setCmd(Query.MOVE_PIECE);
                        switch (e.getKeyCode()) {
                            case 39:
                                PieceScene.moveCurrentPiece(Piece.Direction.RIGHT);
                                query.setDirection(Piece.Direction.RIGHT);
                                client.sendMove(query);
                                break;
                            case 37:
                                PieceScene.moveCurrentPiece(Piece.Direction.LEFT);
                                query.setDirection(Piece.Direction.LEFT);
                                client.sendMove(query);
                                break;
                            case 40:
                                PieceScene.moveCurrentPiece(Piece.Direction.BOTTOM);
                                query.setDirection(Piece.Direction.BOTTOM);
                                client.sendMove(query);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.logout();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame("Puissance 4");
    }

    @Override
    public void receive(String login) {
        int re = JOptionPane.showConfirmDialog(this, String.format("%s vous invite à jouer", login), "Invitation", JOptionPane.YES_NO_OPTION);
        try {
            if (re == JOptionPane.YES_OPTION) {
                Layer layer = new Layer();
                layer.setPieceSceneTurn(false);
                client.acceptRequest(login);
                container.add(layer);
                container.validate();
            } else {
                client.declineRequest(login);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(String login) {
//        JOptionPane.showMessageDialog(this,String.format("%s a acception votre invitation",login),"Réponse invitation",JOptionPane.INFORMATION_MESSAGE);
        Layer layer = new Layer();
        layer.setPieceSceneTurn(true);
        container.add(layer);
        container.validate();
        this.requestFocus();
    }

    @Override
    public void decline(String login) {
        JOptionPane.showMessageDialog(this, String.format("%s a acception votre invitation", login), "Réponse invitation", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void online(String login) {
        JButton statusButton = new JButton(login);
        statusButton.addActionListener(e -> {
            try {
                client.request(login);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        bottomPanel.add(statusButton);
        container.revalidate();
        userConnectedMap.put(login, statusButton);
    }

    @Override
    public void offline(String login) {
        JButton statusButton = userConnectedMap.get(login);
        bottomPanel.remove(statusButton);
        bottomPanel.revalidate();
        userConnectedMap.remove(login, statusButton);
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void onSend(Query query) {

    }

    @Override
    public void onMove(Piece.Direction direction) {
        PieceScene.moveCurrentPiece(direction);
    }
}
