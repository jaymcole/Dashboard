package dashboard.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import dashboard.apps.bouncingBalls.BouncingBallsApp;
import dashboard.apps.clockApp.ClockApp;
import dashboard.apps.testApps.BoundingBoxTestApp;
import dashboard.apps.testApps.DebugBordersApp;
import dashboard.apps.testApps.TextureTestApp;
import dashboard.miscDataObjects.AppInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppLoader {
    private static final String EmptySpace = "----";
    private static final String SeenSpace = "____";

    public static final String BouncingBallAppShorthand = "BB_A";
    public static final String ClockAppShorthand = "C__A";

    public static final String TextureTextAppShorthand = "TT_A";
    public static final String BoundingBoxTestAppShorthand = "BBTA";

    public static List<AppInfo> loadAppsFromLayoutFile(String appLayoutCSVPath) throws IOException {
        List<AppInfo> apps = new ArrayList<>();
        List<String[]> layout = readLayoutFile(Gdx.files.internal(appLayoutCSVPath));

        int maxRows = layout.size()-1;
        int maxColumns = 0;

        for(int row = 0; row < layout.size(); row++) {
            for(int col = 0; col < layout.get(row).length; col++) {
                if (layout.get(row)[col].compareTo(EmptySpace) != 0 && layout.get(row)[col].compareTo(SeenSpace) != 0) {
                    exploreAndInstantiateApp(apps, layout, row, col);
                }
            }
        }
        return apps;
    }

    private static void exploreAndInstantiateApp(List<AppInfo> apps, List<String[]> layout, int row, int col) {
        String shorthand = layout.get(row)[col];
        int endColumn = col;
        while (endColumn <= layout.get(row).length-2 && layout.get(row)[endColumn+1].compareTo(shorthand) == 0) {
            endColumn++;
        }

        int endRow = row;
        while (endRow <= layout.size()-2 && layout.get(endRow+1)[col].compareTo(shorthand) == 0) {
            endRow++;
        }

        for(int clearRow = row; clearRow <= endRow; clearRow++ ) {
            for(int clearCol = col; clearCol <= endColumn; clearCol++) {
                layout.get(clearRow)[clearCol] = SeenSpace;
            }
        }

        endColumn = endColumn + 1 - col;
        endRow = endRow + 1 - row;
        row += endRow -1;
        apps.add(instantiateApp(shorthand, Math.abs((layout.size()-1) - row), col, endColumn, endRow));
    }

    private static AppInfo instantiateApp(String shorthand, int row, int col, int width, int height) {
        switch(shorthand) {
            case BouncingBallAppShorthand:
                return new AppInfo(new BouncingBallsApp(), col, row, width, height);
            case ClockAppShorthand:
                return new AppInfo(new ClockApp(), col, row, width, height);
            case TextureTextAppShorthand:
                return new AppInfo(new TextureTestApp(), col, row, width, height);
            case BoundingBoxTestAppShorthand:
                return new AppInfo(new BoundingBoxTestApp(), col, row, width, height);
            default:
                return new AppInfo(new DebugBordersApp(Color.BLUE), col, row, width, height);
        }
    }

    private static List<String[]> readLayoutFile(FileHandle layoutFile) {
        List<String[]> lines = new ArrayList<>();
        String text = layoutFile.readString();
        String[] lineArray = text.split("\\r?\\n");
        for(String line : lineArray) {
            lines.add(line.split(","));
        }
        return lines;
    }

}
