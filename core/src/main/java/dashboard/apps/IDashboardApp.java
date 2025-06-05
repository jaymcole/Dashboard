package dashboard.apps;


import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;

import java.util.HashMap;
import java.util.List;

public interface IDashboardApp {
    public void update(UpdateInfo updateInfo);
    public void render(RenderInfo renderInfo);
    public void resize();
    public String getAppName();

    public HashMap<String, String> getCurrentAppSettings();
    public List<Actor> getSettingsUiActors();
}
