package dashboard.dataObjects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import dashboard.apps.IDashboardApp;

public class AppInfo {

    public final IDashboardApp app;
    public int xCell; // top-most row
    public int xCellCount; // in rows

    public int yCell; // left-most column
    public int yCellCount; // in columns

    public FrameBuffer frameBuffer;
    public int xScreenCoordinate;
    public int yScreenCoordinate;
    public int maxScreenWidth;
    public int maxScreenHeight;

    public AppInfo (IDashboardApp app, int xCell, int yCell, int xCellCount, int yCellCount) {
        this.app = app;
        this.xCell = xCell;
        this.xCellCount = xCellCount;
        this.yCell = yCell;
        this.yCellCount = yCellCount;
    }

}
