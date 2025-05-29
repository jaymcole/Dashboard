package dashboard.apps.testApps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import dashboard.apps.BaseApp;
import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.UpdateInfo;

public class DebugBordersApp extends BaseApp {

    private Color debugColor;

    public DebugBordersApp(Color debugColor) {
        super();
        this.debugColor = debugColor;
        appName = "Debug Test App";

    }

    @Override
    public void update(UpdateInfo updateInfo) {
    }

    @Override
    public void render(RenderInfo renderInfo) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        matrix.setToOrtho2D(0, 0, renderInfo.maxWidth,renderInfo.maxHeight);
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(debugColor);
        renderDebugBorder(renderInfo);
        renderDebugX(renderInfo);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.begin();
        renderAppName(renderInfo);
        spriteBatch.end();
    }

    private void renderDebugBorder(RenderInfo renderInfo) {
        shapeRenderer.rect(1,1, renderInfo.maxWidth-1, renderInfo.maxHeight-1);

    }

    private void renderDebugX(RenderInfo renderInfo) {
        shapeRenderer.line(1,1,renderInfo.maxWidth , renderInfo.maxHeight);
        shapeRenderer.line(1,renderInfo.maxHeight,renderInfo.maxWidth, 1);

    }


}
