package dashboard.apps.testApps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.graphs.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextureTestApp extends BaseApp {

    private final Texture texture;
    private BoundingBox squareBounds;

    public TextureTestApp(BoundingBox appBounds, String[] args) {
        super(appBounds,args);
        this.texture = new Texture(Gdx.files.internal("libgdx128.png"));
        squareBounds = appBounds.getLargestSquare();
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

        spriteBatch.draw(texture, squareBounds.getX(),squareBounds.getY(), squareBounds.getWidth(), squareBounds.getHeight());
        spriteBatch.end();
    }

    @Override
    protected void resizeApp() {
        squareBounds = appBounds.getLargestSquare();
    }

    @Override
    public HashMap<String, String> saveSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return new ArrayList<>();
    }
}
