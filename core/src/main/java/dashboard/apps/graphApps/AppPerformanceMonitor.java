package dashboard.apps.graphApps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.main.StatsManager;
import dashboard.miscDataObjects.AppConfigs;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.Stat;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.LineGraph;
import dashboard.rendering.graphs.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppPerformanceMonitor extends BaseApp {

    private LineGraph graph;
    private BoundingBox graphBounds;
    private List<String> suffixes;
    private List<String> dataSources;
    private final int yCeiling;
    private final boolean autoscaleGraph;
    private Map<String, List<Stat>> dataSourceLists = new HashMap<>();

    public AppPerformanceMonitor(BoundingBox appBounds, AppConfigs configs) {
        super(appBounds, configs);
        yCeiling = (int)configs.appArgs.getOrDefault("yCeiling", 1);
        suffixes = (List<String>)configs.appArgs.getOrDefault("dataSourcesWithSuffix", new ArrayList<String>());
        dataSources = (List<String>)configs.appArgs.getOrDefault("dataSources", new ArrayList<String>());
        autoscaleGraph = (boolean)configs.appArgs.getOrDefault("autoscaling", false);

        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Starting wait");
                    Thread.sleep(1000);
                    System.out.println("Done waiting");
                    resizeApp();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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
        spriteBatch.setProjectionMatrix(matrix);
        shapeRenderer.setProjectionMatrix(matrix);

        graph.render(shapeRenderer, spriteBatch);

        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        graphBounds.render(shapeRenderer);
        shapeRenderer.end();}

    @Override
    public HashMap<String, String> saveSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return List.of();
    }

    private void refreshDataSources() {
        dataSourceLists = new HashMap<>();
        if (suffixes != null) {
            for(String suffix : suffixes) {
                dataSourceLists.putAll(StatsManager.getAllStatsWithSuffix(suffix));
            }
        }

        if (dataSources != null) {
            for(String dataSource : dataSources) {
                dataSourceLists.putAll(StatsManager.getAllStatsWithSuffix(dataSource));
            }
        }
    }

    @Override
    protected void resizeApp() {
        refreshDataSources();
        graphBounds = new BoundingBox(appBounds, 0, 0, 100, 100);
        graph = new LineGraph(graphBounds, dataSourceLists, 1000, yCeiling, autoscaleGraph);
    }
}
