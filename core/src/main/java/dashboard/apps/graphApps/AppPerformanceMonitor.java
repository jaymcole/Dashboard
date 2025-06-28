package dashboard.apps.graphApps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.main.Home;
import dashboard.miscDataObjects.AppConfigs;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.Stat;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.LineGraph;
import dashboard.rendering.graphs.BoundingBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppPerformanceMonitor extends BaseApp {

    private LineGraph graph;
    private BoundingBox graphBounds;

    public AppPerformanceMonitor(BoundingBox appBounds, AppConfigs configs) {
        super(appBounds, configs);
    }

    @Override
    public void loadSettings(HashMap<String, String> savedSettings) {

    }

    @Override
    public void update(UpdateInfo updateInfo) {

    }

    @Override
    public void render(RenderInfo renderInfo) {
        matrix.setToOrtho2D(0, 0, appBounds.getWidth(), appBounds.getHeight());

        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        graph.render(shapeRenderer);
        shapeRenderer.end();

        matrix.setToOrtho2D(0, 0, appBounds.getWidth(), appBounds.getHeight());
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        graphBounds.render(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public HashMap<String, String> saveSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return List.of();
    }

    @Override
    protected void resizeApp() {
        Map<String, List<Stat>> statsToPlot = new HashMap<>();
        statsToPlot.put("FPS", Home.FPSHistory);
        graphBounds = new BoundingBox(appBounds, 5, 5, 95, 95);
        graph = new LineGraph(graphBounds, statsToPlot, 1000, 100000);
    }
}
