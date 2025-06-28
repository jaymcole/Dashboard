package dashboard.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.miscDataObjects.Stat;
import dashboard.rendering.graphs.BoundingBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineGraph {

    private static final Color[] colors = new Color[] {
        Color.BLUE,
        Color.RED,
        Color.GREEN,
        Color.PINK,
        Color.YELLOW,
        Color.CYAN,
        Color.PURPLE
    };

    private BoundingBox graphMaxBounds;
    private final Map<String, List<Stat>> statsToPlot;
    private final Map<String, Color> statsColors;

    private final int timeframe;
    private final int yCeiling;

    public LineGraph(BoundingBox bounds, Map<String, List<Stat>> statsToPlot, int timeframe, int yCeiling) {
        this.statsToPlot = statsToPlot;
        this.timeframe = timeframe;
        this.yCeiling = yCeiling;
        resize(bounds);

        statsColors = new HashMap<>();
        int colorIndex = 0;
        for (Map.Entry<String, List<Stat>> entry : statsToPlot.entrySet()) {
            statsColors.put(entry.getKey(), colors[colorIndex % colors.length]);
            colorIndex++;
        }
    }


    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        float lineCount = 10;
        float distanceBetweenLines = graphMaxBounds.getWidth() / lineCount;
        for(int i = 0; i < lineCount; i++) {
            float x1 = i * distanceBetweenLines + graphMaxBounds.getX();
            float y1 = graphMaxBounds.getY();
            float y2 = graphMaxBounds.getY() + graphMaxBounds.getHeight();
            shapeRenderer.line(x1, y1, x1, y2);
        }

        distanceBetweenLines = graphMaxBounds.getHeight() / lineCount;
        for(int i = 0; i < lineCount; i++) {
            float x1 = graphMaxBounds.getX();
            float x2 = graphMaxBounds.getX() + graphMaxBounds.getWidth();
            float y1 = graphMaxBounds.getY() + (i * distanceBetweenLines);

            shapeRenderer.line(x1, y1, x2, y1);
        }

        for (Map.Entry<String, List<Stat>> entry : statsToPlot.entrySet()) {
            List<Stat> stats = entry.getValue();
            shapeRenderer.setColor(statsColors.get(entry.getKey()));

            long currentTime = System.currentTimeMillis();
            for(int i = entry.getValue().size()-1; i > 1; i--) {

                float x1 = (stats.get(i).timeStamp - currentTime) / 10.0f;
                float y1 = stats.get(i).statMetric / yCeiling;

                float x2 = (stats.get(i-1).timeStamp - currentTime) / 10.0f ;
                float y2 = stats.get(i-1).statMetric / yCeiling;

                x1 += graphMaxBounds.getWidth();
                y1 *= graphMaxBounds.getHeight();

                x2 += graphMaxBounds.getWidth();
                y2 *= graphMaxBounds.getHeight();

                shapeRenderer.setColor(Color.MAGENTA);

                x1 += graphMaxBounds.getX();
                y1 += graphMaxBounds.getY();

                x2 += graphMaxBounds.getX();
                y2 += graphMaxBounds.getY();

                if (i == entry.getValue().size()-1) {
                    // Clamps first point to right side of graph
                    x1 = graphMaxBounds.getX() + graphMaxBounds.getWidth();
                }

                y1 = Math.min(y1, graphMaxBounds.getHeight() + graphMaxBounds.getY());
                y1 = Math.max(y1, graphMaxBounds.getY());
                y2 = Math.min(y2, graphMaxBounds.getHeight() + graphMaxBounds.getY());
                y2 = Math.max(y2, graphMaxBounds.getY());

                shapeRenderer.line(Math.max(x1, graphMaxBounds.getX()), y1, Math.max(x2, graphMaxBounds.getX()), y2);
                if (x1 < graphMaxBounds.getX()) {
                    break;
                }
            }
        }
    }

    public void resize(BoundingBox bounds) {
        graphMaxBounds = bounds;
    }
}
