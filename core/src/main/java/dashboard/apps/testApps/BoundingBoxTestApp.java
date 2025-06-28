package dashboard.apps.testApps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.AppConfigs;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.graphs.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoundingBoxTestApp extends BaseApp {

    private List<BoundingBox> testBoundingBoxes;
    private List<Color> boxColors;

    public BoundingBoxTestApp(BoundingBox appBounds, AppConfigs configs) {
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
        for(int i = 0; i < testBoundingBoxes.size(); i++) {
            shapeRenderer.setColor(boxColors.get(i));
            testBoundingBoxes.get(i).render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void resizeApp() {
        testBoundingBoxes = new ArrayList<>();
        boxColors = new ArrayList<>();

        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 0, 10, 10));
        boxColors.add(Color.RED);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 10, 20, 20));
        boxColors.add(Color.YELLOW);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 20, 30, 30));
        boxColors.add(Color.BLUE);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 30, 40, 40));
        boxColors.add(Color.GREEN);

        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 40, 50, 50));
        boxColors.add(Color.RED);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 50, 60, 60));
        boxColors.add(Color.YELLOW);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 60, 70, 70));
        boxColors.add(Color.BLUE);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 70, 80, 80));
        boxColors.add(Color.GREEN);

        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 80, 90, 90));
        boxColors.add(Color.RED);
        testBoundingBoxes.add(new BoundingBox(appBounds, 0, 90, 100, 100));
        boxColors.add(Color.YELLOW);
    }

    @Override
    public HashMap<String, String> saveSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return List.of();
    }
}
