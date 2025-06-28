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

public class DebugBordersApp extends BaseApp {

    private Color debugColor;

    public DebugBordersApp(BoundingBox appBounds, Color debugColor, AppConfigs configs) {
        super(appBounds, configs);
        this.debugColor = debugColor;
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
        shapeRenderer.setColor(debugColor);
        renderDebugBorder(renderInfo);
        renderDebugX(renderInfo);
        shapeRenderer.end();
    }

    @Override
    protected void resizeApp() {

    }

    @Override
    public HashMap<String, String> saveSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return new ArrayList<>();
    }

    private void renderDebugBorder(RenderInfo renderInfo) {
        shapeRenderer.rect(1,1, appBounds.getWidth()-1, appBounds.getHeight()-1);

    }

    private void renderDebugX(RenderInfo renderInfo) {
        shapeRenderer.line(1,1,appBounds.getWidth(), appBounds.getHeight());
        shapeRenderer.line(1,appBounds.getHeight(), appBounds.getWidth(), 1);
    }
}
