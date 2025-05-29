package dashboard.apps.testApps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import dashboard.apps.BaseApp;
import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.UpdateInfo;

public class TextureTestApp extends BaseApp {

    private final Texture texture;

    public TextureTestApp(Texture texture) {
        super();
        appName = "Texture Test App";
        this.texture = texture;
    }

    @Override
    public void update(UpdateInfo updateInfo) {
    }

    @Override
    public void render(RenderInfo renderInfo) {
        matrix.setToOrtho2D(0, 0, renderInfo.maxWidth,renderInfo.maxHeight);
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.begin();
        spriteBatch.draw(texture, 0,0, renderInfo.maxWidth,renderInfo.maxHeight);
        renderAppName(renderInfo);
        spriteBatch.end();
    }
}
