package piece;

import global.Config;
import scene.PieceScene;

import java.awt.*;
import java.io.Serializable;

public class Piece {
    private int x;
    private int y;
    private int size;
    private boolean current;
    private boolean comingDown;
    private boolean cameDown;
    private Color color;

    public static final Color EVEN_COLOR = new Color(78, 250, 0);
    public static final Color ODD_COLOR = new Color(255, 0, 75);
    public static final Color NEUTRAL_COLOR = new Color(255, 255, 255);

    public Piece() {
    }

    public Piece(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.size = Config.Grid.CELL_SIZE;
        this.color = color;
        this.current = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setInNeutralColor() {
        this.color = NEUTRAL_COLOR;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public void move(Direction direction) {
        if (!comingDown) {
            switch (direction) {
                case LEFT:
                    if (x == 0)
                        x = Config.Grid.CELL_SIZE * (Config.Grid.COLUMN_COUNT - 1);
                    else
                        x -= Config.Grid.CELL_SIZE;
                    break;
                case RIGHT:
                    if (x == Config.Grid.CELL_SIZE * (Config.Grid.COLUMN_COUNT - 1))
                        x = 0;
                    else
                        x += Config.Grid.CELL_SIZE;
                    break;
                case TOP:
                case BOTTOM:
                    Piece[][] gridColumn = PieceScene.getGridColumn();
                    int ord = x / Config.Grid.CELL_SIZE;
                    for (int i = 0; i < Config.Grid.ROW_COUNT; i++) {
                        if (gridColumn[i][ord] == null) {
                            int limit = (Config.Grid.ROW_COUNT - i) * Config.Grid.CELL_SIZE;
                            Thread t = new Thread(() -> {
                                comingDown = true;
                                while (y < limit) {
                                    try {
                                        y += 1;
                                        if (y == limit) {
                                            cameDown = true;
                                        }
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                comingDown = false;
                            });
                            t.start();
                            break;
                        }
                    }
                default:
                    break;
            }
        }
    }

    public boolean isCameDown() {
        return this.cameDown;
    }

    public enum Direction implements Serializable {
        LEFT, TOP, RIGHT, BOTTOM;
        private static final long serialVersionUID = -4425643939276759191L;
    }
}
