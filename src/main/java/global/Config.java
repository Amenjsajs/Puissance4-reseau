package global;

import java.awt.*;

public final class Config {
    public static final class Grid {
        public static final int CELL_SIZE = 60;
        public static final int ROW_COUNT = 6;
        public static final int COLUMN_COUNT = 7;
        public static final int CELL_BORDER_WIDTH = 2;
        public static final Color CELL_BORDER_COLOR = new Color(250, 100, 10);
    }

    public static final class Scene{
        public static final void setRendering(Graphics2D graphics2D){
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        public static final Color SCENE_BG_COLOR = new Color(10, 255, 100);
    }
}
