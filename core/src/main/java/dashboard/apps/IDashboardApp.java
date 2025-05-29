package dashboard.apps;


import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.UpdateInfo;

public interface IDashboardApp {

    public void update(UpdateInfo updateInfo);
    public void render(RenderInfo renderInfo);

}
