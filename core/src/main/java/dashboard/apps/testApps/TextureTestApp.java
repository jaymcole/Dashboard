package dashboard.apps.testApps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextureTestApp extends BaseApp {

    private final Texture texture;

    public TextureTestApp() {
        super();
        this.texture = new Texture(Gdx.files.internal("libgdx128.png"));
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
        spriteBatch.begin();
        spriteBatch.draw(texture, 0,0, appBounds.getWidth(), appBounds.getHeight());
        spriteBatch.end();
    }

    @Override
    public void resize() {

    }

    @Override
    public HashMap<String, String> getCurrentAppSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return new ArrayList<>();
    }
}
