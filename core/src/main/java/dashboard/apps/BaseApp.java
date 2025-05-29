package dashboard.apps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.UpdateInfo;

public class BaseApp implements IDashboardApp {

    protected SpriteBatch spriteBatch;
    protected ShapeRenderer shapeRenderer;

    public BaseApp() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(UpdateInfo updateInfo) {

    }

    @Override
    public void render(RenderInfo renderInfo) {

    }
}
