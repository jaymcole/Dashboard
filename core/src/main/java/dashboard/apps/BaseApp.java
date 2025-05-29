package dashboard.apps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.UpdateInfo;

public abstract class BaseApp implements IDashboardApp {

    protected String appName = "DefaultAppName";
    protected SpriteBatch spriteBatch;
    protected ShapeRenderer shapeRenderer;
    protected Matrix4 matrix = new Matrix4();

    public BaseApp() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public abstract void update(UpdateInfo updateInfo);

    @Override
    public abstract void render(RenderInfo renderInfo);

    protected void renderAppName(RenderInfo renderInfo) {
        if (spriteBatch.isDrawing()) {
            renderInfo.debugFont.draw(spriteBatch, appName, 5, renderInfo.maxHeight - (renderInfo.debugFont.getXHeight()));
        } else {
            System.err.println("renderAppName was called before spriteBatch.begin() !!!");
        }
    }
}
