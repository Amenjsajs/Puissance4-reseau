package scene;

import global.Config;

import javax.swing.*;
import java.awt.*;

public class Layer extends JPanel {
    public Layer() {
        setBackground(Color.cyan);
        createLayerPane();
    }

    public void createLayerPane(){
        JLayeredPane layeredPane;
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        layeredPane.setBackground(Color.red);
        layeredPane.setPreferredSize(new Dimension(Grid.width, Grid.height + Config.Grid.CELL_SIZE + Config.Grid.CELL_BORDER_WIDTH));

        layeredPane.add(new PieceScene(),JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(new Grid(), JLayeredPane.POPUP_LAYER);
        add(layeredPane);
    }
}
