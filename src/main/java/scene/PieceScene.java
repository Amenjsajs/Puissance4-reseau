package scene;

import global.Config;
import global.Logic;
import piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PieceScene extends JPanel {
    PieceScene self = this;
    private Piece[][] gridColumn = new Piece[Config.Grid.ROW_COUNT][Config.Grid.COLUMN_COUNT];
    private Piece currentPiece;
    List<Piece> pieces = new CopyOnWriteArrayList<>();
    int currentColumn = 0;

    private static ArrayList<PieceScene> pieceScenes = new ArrayList<>();

    private boolean focused = false;
    private static boolean turn;

    public PieceScene() {
        setBounds(0, 0, Grid.width, Grid.height + Config.Grid.CELL_SIZE + Config.Grid.CELL_BORDER_WIDTH);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handlePieceSceneClick();
            }
        });
        generatePiece();
        handlePieceAppearance();
        pieceScenes.add(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        Config.Scene.setRendering(graphics2D);
        drawPiece(graphics2D);
        repaint();
    }

    private void drawPiece(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(
                Config.Grid.CELL_BORDER_WIDTH,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL));
        graphics2D.setColor(Config.Grid.CELL_BORDER_COLOR);
        pieces.forEach(piece -> {
            graphics2D.setColor(piece.getColor());
            graphics2D.fillOval(piece.getX(), piece.getY(), piece.getSize(), piece.getSize());
        });
    }

    private void handlePieceSceneClick() {
        if (focused) return;
        pieceScenes.forEach(pieceScene -> {
            pieceScene.focused = false;
            pieceScene.setBackground(new Color(0, 100, 0));
        });
        self.setBackground(new Color(100, 200, 200));
        focused = true;
    }

    public static void moveCurrentPiece(Piece.Direction direction) {
        for (PieceScene pieceScene : pieceScenes) {
            pieceScene.currentPiece.move(direction);
            break;
        }
    }

    public void removePieceScene(PieceScene pieceScene) {
        pieceScenes.remove(pieceScene);
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public static ArrayList<PieceScene> getPieceScenes() {
        return pieceScenes;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    private void generatePiece() {
        Color color = pieces.size() % 2 == 1 ? Piece.EVEN_COLOR : Piece.ODD_COLOR;
        Piece piece;
        if (currentPiece == null) {
            piece = new Piece(0, 0, color);
        } else {
            piece = new Piece(currentPiece.getX(), 0, color);
        }
        currentPiece = piece;
        pieces.add(piece);
    }

    private void handlePieceAppearance() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    if (currentPiece.isCameDown()) {
                        int x = currentPiece.getX() / Config.Grid.CELL_SIZE;
                        List<Piece> winnerPieces = new ArrayList<>();

                        for (int i = 0; i < Config.Grid.ROW_COUNT; i++) {
                            if (gridColumn[i][x] == null) {
                                gridColumn[i][x] = currentPiece;
                                winnerPieces = Logic.getWinnerPieces(currentPiece, gridColumn);
                                winnerPieces.forEach(Piece::setInNeutralColor);
                                break;
                            }
                        }
                        if(winnerPieces.size()<4){
                            generatePiece();
                            turn = !turn;
                        }else{
                            break;
                        }
                    }
                    Thread.sleep(1);
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
        });
        t.start();
    }

    public static Piece[][] getGridColumn() {
        for (PieceScene pieceScene : pieceScenes) {
//            if (pieceScene.isFocused()) return pieceScene.gridColumn;
            return pieceScene.gridColumn;
        }
        return new Piece[][]{};
    }

    public static boolean isTurn() {
        return turn;
    }

    public static void setTurn(boolean turn) {
        PieceScene.turn = turn;
    }
}
