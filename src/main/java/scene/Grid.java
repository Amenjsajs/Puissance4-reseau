package scene;

import global.Config;

import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel {
    public static final int width = Config.Grid.COLUMN_COUNT * Config.Grid.CELL_SIZE;
    public static final int height = Config.Grid.ROW_COUNT * Config.Grid.CELL_SIZE;

    public Grid() {
        setBounds(0, Config.Grid.CELL_SIZE, Grid.width , Grid.height );
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        Config.Scene.setRendering(graphics2D);
        drawCells(graphics2D);
    }

    private void drawCells(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(
                Config.Grid.CELL_BORDER_WIDTH,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL));
        g2d.setColor(Config.Grid.CELL_BORDER_COLOR);
        for (int i = 0; i < Config.Grid.ROW_COUNT; i++) {
            for (int j = 0; j < Config.Grid.COLUMN_COUNT; j++) {
                g2d.drawOval(j * Config.Grid.CELL_SIZE, i * Config.Grid.CELL_SIZE, Config.Grid.CELL_SIZE, Config.Grid.CELL_SIZE);
            }
        }
    }
}
