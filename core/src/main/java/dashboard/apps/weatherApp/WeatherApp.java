package dashboard.apps.weatherApp;

import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;

import java.util.HashMap;
import java.util.List;

public class WeatherApp extends BaseApp {

    public WeatherApp(BoundingBox appBounds) {
        super(appBounds);
    }

    @Override
    public void loadSettings(HashMap<String, String> savedSettings) {

    }

    @Override
    public void update(UpdateInfo updateInfo) {

    }

    @Override
    public void render(RenderInfo renderInfo) {

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
        return List.of();
    }
}
