package dashboard.apps.testApps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;

public class DebugBordersApp extends BaseApp {

    private Color debugColor;

    public DebugBordersApp(Color debugColor) {
        super();
        this.debugColor = debugColor;
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
    public void resize() {}

    private void renderDebugBorder(RenderInfo renderInfo) {
        shapeRenderer.rect(1,1, appBounds.getWidth()-1, appBounds.getHeight()-1);

    }

    private void renderDebugX(RenderInfo renderInfo) {
        shapeRenderer.line(1,1,appBounds.getWidth(), appBounds.getHeight());
        shapeRenderer.line(1,appBounds.getHeight(), appBounds.getWidth(), 1);
    }


}
