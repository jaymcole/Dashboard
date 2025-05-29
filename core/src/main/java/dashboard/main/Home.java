package dashboard.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.apps.testApps.DebugBordersApp;
import dashboard.dataObjects.AppInfo;
import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.Tuple;

import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class Home implements Screen {

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private List<AppInfo> apps;

    private int appPadding = 10;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        apps = new ArrayList<>();
        apps.add(new AppInfo(new DebugBordersApp(Color.RED), 0,0,1,1));
        apps.add(new AppInfo(new DebugBordersApp(Color.YELLOW), 1,0,1,1));
        apps.add(new AppInfo(new DebugBordersApp(Color.GREEN), 0,1,2,1));

        setupAppInfo();
    }

    private void setupAppInfo() {
        Tuple<Integer, Integer> neededDimensions = findMaxNumberOfRowsAndColumnsNeeded(apps);
        System.out.println("Rows: " + neededDimensions.left + " Cols: " + neededDimensions.right);
        int cellWidth = Gdx.graphics.getWidth() / neededDimensions.left;
        int cellHeight = Gdx.graphics.getHeight() / neededDimensions.right;

        for(AppInfo info : apps) {
            info.xScreenCoordinate = info.xCell * cellWidth;
            info.yScreenCoordinate = info.yCell * cellHeight;

            info.maxScreenWidth = info.xCellCount * cellWidth - appPadding;
            info.maxScreenHeight = info.yCellCount * cellHeight - appPadding;
            info.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, info.maxScreenWidth, info.maxScreenHeight, false);

            System.out.println("xWidth: " + info.maxScreenWidth + " frameWidth:" + info.frameBuffer.getWidth());
            System.out.println("xWidth: " + info.maxScreenHeight + " frameWidth:" + info.frameBuffer.getHeight());
            System.out.println("startX: " + info.xScreenCoordinate + ", startY: " + info.yScreenCoordinate);
        }
    }

    @Override
    public void render(float delta) {

        RenderInfo renderInfo = new RenderInfo();
        for(AppInfo info : apps) {
            info.frameBuffer.begin();
            renderInfo.maxWidth = info.maxScreenWidth;
            renderInfo.maxHeight = info.maxScreenHeight;
            info.app.render(renderInfo);
            info.frameBuffer.end();
        }

        spriteBatch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(1,1, Gdx.graphics.getWidth()-1, Gdx.graphics.getHeight()-1);
        shapeRenderer.circle(0,0,50);

//        shapeRenderer.circle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,15);
//        for(AppInfo info : apps) {
//            shapeRenderer.setColor(Color.YELLOW);
//            shapeRenderer.line(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, info.xScreenCoordinate, info.yScreenCoordinate);
//
//            shapeRenderer.setColor(Color.FOREST);
//            shapeRenderer.rect(info.xScreenCoordinate, info.yScreenCoordinate, info.frameBuffer.getWidth()-1, info.frameBuffer.getHeight()-1);
//
//        }

        shapeRenderer.end();
        spriteBatch.end();

        spriteBatch.begin();
        for(AppInfo info : apps) {
            spriteBatch.draw(
                info.frameBuffer.getColorBufferTexture(),
                info.xScreenCoordinate + (appPadding / 2),
                info.yScreenCoordinate + (appPadding / 2)
            );
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

    private Tuple<Integer, Integer> findMaxNumberOfRowsAndColumnsNeeded(List<AppInfo> apps) {
        int maxRows = 0;
        int maxCols = 0;
        for (AppInfo info : apps) {
            if (info.xCell + info.xCellCount > maxRows) {
                maxRows = info.xCell + info.xCellCount;
            }

            if (info.yCell + info.yCellCount > maxCols) {
                maxCols = info.yCell + info.yCellCount;
            }
        }
        return new Tuple<>(2,2);
//        return new Tuple<>(maxRows, maxCols);
    }
}
