package dashboard.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dashboard.helper.FontHelper;
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
        Color.PURPLE,
        Color.LIME
    };

    private BoundingBox graphMaxBounds;
    private BoundingBox graphBox;
    private BoundingBox keyBox;
    private List<TextBox> keyTexts;
    private final Map<String, List<Stat>> statsToPlot;
    private final Map<String, Color> statsColors;

    private final int timeframe;
    private final int yCeiling;

    private BitmapFont debugFont;

    public LineGraph(BoundingBox bounds, Map<String, List<Stat>> statsToPlot, int timeframe, int yCeiling) {
        this.statsToPlot = statsToPlot;
        this.timeframe = timeframe;
        this.yCeiling = yCeiling;
        resize(bounds);
        debugFont = FontHelper.loadFont("fonts/Roboto-Regular.ttf", 16);
        statsColors = new HashMap<>();
        int colorIndex = 0;
        for (Map.Entry<String, List<Stat>> entry : statsToPlot.entrySet()) {
            statsColors.put(entry.getKey(), colors[colorIndex % colors.length]);
            colorIndex++;
        }
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);
        float lineCount = 10;
        float distanceBetweenLines = graphMaxBounds.getWidth() / lineCount;
        for (int i = 0; i < lineCount; i++) {
            float x1 = i * distanceBetweenLines + graphMaxBounds.getX();
            float y1 = graphMaxBounds.getY();
            float y2 = graphMaxBounds.getY() + graphMaxBounds.getHeight();
            shapeRenderer.line(x1, y1, x1, y2);
        }

        distanceBetweenLines = graphMaxBounds.getHeight() / lineCount;
        for (int i = 0; i < lineCount; i++) {
            float x1 = graphMaxBounds.getX();
            float x2 = graphMaxBounds.getX() + graphMaxBounds.getWidth();
            float y1 = graphMaxBounds.getY() + (i * distanceBetweenLines);

            shapeRenderer.line(x1, y1, x2, y1);
        }


        for (Map.Entry<String, List<Stat>> entry : statsToPlot.entrySet()) {
            List<Stat> stats = entry.getValue();
            shapeRenderer.setColor(statsColors.get(entry.getKey()));

            long currentTime = System.currentTimeMillis();
            for (int i = entry.getValue().size() - 1; i > 1; i--) {

                float x1 = (stats.get(i).timeStamp - currentTime) / 10.0f;
                float y1 = stats.get(i).statMetric / yCeiling;

                float x2 = (stats.get(i - 1).timeStamp - currentTime) / 10.0f;
                float y2 = stats.get(i - 1).statMetric / yCeiling;

                x1 += graphMaxBounds.getWidth();
                y1 *= graphMaxBounds.getHeight();

                x2 += graphMaxBounds.getWidth();
                y2 *= graphMaxBounds.getHeight();

                x1 += graphMaxBounds.getX();
                y1 += graphMaxBounds.getY();

                x2 += graphMaxBounds.getX();
                y2 += graphMaxBounds.getY();

                if (i == entry.getValue().size() - 1) {
                    // Clamps first point to right side of graph
                    x1 = graphMaxBounds.getX() + graphMaxBounds.getWidth();
                }

                y1 = Math.min(y1, graphMaxBounds.getHeight() + graphMaxBounds.getY());
                y1 = Math.max(y1, graphMaxBounds.getY());
                y2 = Math.min(y2, graphMaxBounds.getHeight() + graphMaxBounds.getY());
                y2 = Math.max(y2, graphMaxBounds.getY());

                shapeRenderer.rectLine(new Vector2(Math.max(x1, graphMaxBounds.getX()), y1), new Vector2(Math.max(x2, graphMaxBounds.getX()), y2), 1);
                if (x1 < graphMaxBounds.getX()) {
                    break;
                }
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setColor(new Color(1,1,1,0.01f));
        float height = 120;
        shapeRenderer.rect(5, graphMaxBounds.getY() + graphMaxBounds.getHeight() - height - 5, 230, height);
        shapeRenderer.end();

        spriteBatch.begin();
        float startX = 10;
        float startY = graphMaxBounds.getY() + graphMaxBounds.getHeight() - 10;
        float textHeight = 16;
        for (Map.Entry<String, List<Stat>> entry : statsToPlot.entrySet()) {
            debugFont.setColor(statsColors.get(entry.getKey()));
            debugFont.draw(spriteBatch, entry.getKey(), startX, startY);
            startY -= textHeight;
        }
        spriteBatch.end();
    }

    public void resize(BoundingBox bounds) {
        graphMaxBounds = bounds;
    }
}
