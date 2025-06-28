package dashboard.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import dashboard.apps.bouncingBalls.BouncingBallsApp;
import dashboard.apps.clockApp.ClockApp;
import dashboard.apps.graphApps.AppPerformanceMonitor;
import dashboard.apps.testApps.BoundingBoxTestApp;
import dashboard.apps.testApps.DebugBordersApp;
import dashboard.apps.testApps.TextureTestApp;
import dashboard.apps.weatherApp.WeatherApp;
import dashboard.miscDataObjects.AppInfo;
import dashboard.rendering.graphs.BoundingBox;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLoader {
    private static final String LinePartsDelimiter = " ";

    private static final String EmptySpace = "----";
    private static final String SeenSpace = "____";

    // App codes
    public static final String BouncingBallAppShorthand = "BB_A";
    public static final String ClockAppShorthand = "C__A";
    public static final String TextureTextAppShorthand = "TT_A";
    public static final String BoundingBoxTestAppShorthand = "BBTA";
    public static final String WeatherAppShorthand = "WA_A";
    public static final String PerformanceMonitorShorthand = "APMA";

    private static final String SettingsKeyValueDelimiter = "greatbarrierreef";
    private static final String AppSettingsFolder = "DashboardApp/appSettings/";

    public static int Total_Rows = 1;
    public static int Total_Columns = 1;

    public static void SaveAppSettings(AppInfo info) {
        FileHandle settingsFile = Gdx.files.external(AppSettingsFolder + info.getSaveFileName());
        if (settingsFile.exists()) {
            settingsFile.delete();
        }
        createSettingsFile(settingsFile, info);
        try {
            FileWriter writer = new FileWriter(settingsFile.file());
            HashMap<String, String> settings =  info.app.saveSettings();

            if (settings == null) {
                System.err.println(info.app.getAppName() + " failed to save settings. getCurrentAppSettings returned null map.");
                return;
            }

            for(Map.Entry<String, String> entry : settings.entrySet()) {
                writer.append(entry.getKey() + SettingsKeyValueDelimiter + entry.getValue());
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to save settings for " + info.getSaveFileName());
        }
    }

    public static List<AppInfo> loadAppsFromLayoutFile(String appLayoutCSVPath) throws IOException {
        List<AppInfo> apps = new ArrayList<>();
        List<String[]> layout = readLayoutFile(Gdx.files.internal(appLayoutCSVPath));

        Total_Rows = layout.get(0).length;
        Total_Columns = layout.size();

        for(int row = 0; row < layout.size(); row++) {
            for(int col = 0; col < layout.get(row).length; col++) {
                if (layout.get(row)[col].compareTo(EmptySpace) != 0 && layout.get(row)[col].compareTo(SeenSpace) != 0) {
                    exploreLayoutAndInstantiateApp(apps, layout, row, col);
                }
            }
        }
        return apps;
    }

    private static void exploreLayoutAndInstantiateApp(List<AppInfo> apps, List<String[]> layout, int row, int col) {
        String entry = layout.get(row)[col];
        int endColumn = col;
        while (endColumn <= layout.get(row).length-2 && layout.get(row)[endColumn+1].compareTo(entry) == 0) {
            endColumn++;
        }

        int endRow = row;
        while (endRow <= layout.size()-2 && layout.get(endRow+1)[col].compareTo(entry) == 0) {
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

        entry = entry.trim();
        String shorthand = entry;
        String optionalSaveSuffix = "";
        if (entry.contains(LinePartsDelimiter)) {
            String[] parts = entry.split(LinePartsDelimiter);
            shorthand = parts[0];
            optionalSaveSuffix = parts[1];
        }

        apps.add(instantiateApp(shorthand, optionalSaveSuffix, Math.abs((layout.size()-1) - row), col, endColumn, endRow));
    }

    private static AppInfo instantiateApp(String shorthand, String optionalSaveSuffix, int row, int col, int horizontalCellCount, int verticalCellCount) {
        BoundingBox appBounds = calculateAppBoundingBox(horizontalCellCount, verticalCellCount);

        AppInfo info = switch (shorthand) {
            case BouncingBallAppShorthand ->
                new AppInfo(new BouncingBallsApp(appBounds, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
            case ClockAppShorthand -> new AppInfo(new ClockApp(appBounds, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
            case TextureTextAppShorthand ->
                new AppInfo(new TextureTestApp(appBounds, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
            case BoundingBoxTestAppShorthand ->
                new AppInfo(new BoundingBoxTestApp(appBounds, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
            case WeatherAppShorthand -> new AppInfo(new WeatherApp(appBounds, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
            case PerformanceMonitorShorthand -> new AppInfo(new AppPerformanceMonitor(appBounds, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
            default -> new AppInfo(new DebugBordersApp(appBounds, Color.BLUE, new String[]{}), col, row, horizontalCellCount, verticalCellCount, optionalSaveSuffix);
        };

        FileHandle settingsFile = Gdx.files.external(AppSettingsFolder + info.getSaveFileName());
        createSettingsFile(settingsFile, info);
        info.app.loadSettings(readSettingsFile(settingsFile));
        return info;
    }

    public static BoundingBox calculateAppBoundingBox(int horizontalCellCount, int verticalCellCount) {
        int cellWidth = Gdx.graphics.getWidth() / Total_Columns;
        int cellHeight = Gdx.graphics.getHeight() / Total_Rows;
        int boundsWidth = horizontalCellCount * cellWidth;
        int boundsHeight = verticalCellCount * cellHeight;
        BoundingBox bounds = new BoundingBox(0, 0, boundsWidth, boundsHeight);
        return bounds;
    }

    private static void createSettingsFile(FileHandle settingsFile, AppInfo info) {
        FileHandle settingsFolder = Gdx.files.external(AppSettingsFolder);
        if (!settingsFolder.exists()) {
            System.out.println("Directory Does not exist");
            settingsFolder.mkdirs();
        }

        if(settingsFile.exists()) {
            System.out.println("Found existing save file for " + info.getSaveFileName());
        } else {
            System.out.println("No save file found for " + info.getSaveFileName() + " Creating a new fresh file");
            try {
                settingsFile.file().createNewFile();
            } catch (IOException ex) {
                System.out.println("Failed to create save file for " + info.getSaveFileName());
            }
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

    private static HashMap<String, String> readSettingsFile(FileHandle settingsFile) {
        HashMap<String, String> settings = new HashMap<>();
        String text = settingsFile.readString();
        String[] lineArray = text.split("\\r?\\n");
        for(String line : lineArray) {
            String[] keyValuePair = line.split(SettingsKeyValueDelimiter);
            if (keyValuePair.length > 1) {
                settings.put(keyValuePair[0], keyValuePair[1]);
            }
        }
        return settings;
    }

}
