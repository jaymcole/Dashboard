package dashboard.miscDataObjects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import dashboard.apps.BaseApp;
import dashboard.apps.IDashboardApp;

public class AppInfo {

    public final BaseApp app;
    public int xCell; // top-most row
    public int xCellCount; // in rows
    public int yCell; // left-most column
    public int yCellCount; // in columns
    public FrameBuffer frameBuffer;
    public int xScreenCoordinate;
    public int yScreenCoordinate;

    public AppInfo (BaseApp app, int xCell, int yCell, int xCellCount, int yCellCount) {
        this.app = app;
        this.xCell = xCell;
        this.xCellCount = xCellCount;
        this.yCell = yCell;
        this.yCellCount = yCellCount;
    }

}
