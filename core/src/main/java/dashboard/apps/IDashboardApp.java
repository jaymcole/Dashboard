package dashboard.apps;


import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;

public interface IDashboardApp {
    public void update(UpdateInfo updateInfo);
    public void render(RenderInfo renderInfo);
    public void resize();
    public String getAppName();
}
