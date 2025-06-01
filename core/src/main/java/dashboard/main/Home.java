package dashboard.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import dashboard.apps.bouncingBalls.BouncingBallsApp;
import dashboard.apps.clockApp.ClockApp;
import dashboard.apps.testApps.BoundingBoxTestApp;
import dashboard.apps.testApps.TextureTestApp;
import dashboard.miscDataObjects.AppInfo;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.Tuple;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class Home implements Screen {

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private List<AppInfo> apps;
    private HashSet<AppInfo> brokenApps;
    private Matrix4 matrix = new Matrix4();
    private BitmapFont debugFont;
    private final int appPadding = 10;


    @Override
    public void show() {
        brokenApps = new HashSet<>();
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        debugFont = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        Texture libgdxTestTexture = new Texture(Gdx.files.internal("libgdx128.png"));

        apps = new ArrayList<>();
        apps.add(new AppInfo(new ClockApp(), 0,0,1,1));
        apps.add(new AppInfo(new BoundingBoxTestApp(), 1,0,1,1));
        apps.add(new AppInfo(new TextureTestApp(libgdxTestTexture), 0,1,2,1));
        apps.add(new AppInfo(new BouncingBallsApp(), 2,1,1,1));
        apps.add(new AppInfo(new BouncingBallsApp(), 0,2,3,1));
        apps.add(new AppInfo(new TextureTestApp(libgdxTestTexture), 2,0,1,1));
        calculateAppInfo();
    }

    private void calculateAppInfo() {
        Tuple<Integer, Integer> horizontalVerticalCellCounts = findMaxNumberOfRowsAndColumnsNeeded(apps);
        int cellWidth = Gdx.graphics.getWidth() / horizontalVerticalCellCounts.left;
        int cellHeight = Gdx.graphics.getHeight() / horizontalVerticalCellCounts.right;

        for(AppInfo info : apps) {
            info.xScreenCoordinate = info.xCell * cellWidth;
            info.yScreenCoordinate = info.yCell * cellHeight;

            BoundingBox appBounds = new BoundingBox(
                0,
                0,
                (info.xCellCount * cellWidth) - appPadding,
                (info.yCellCount * cellHeight) - appPadding
            );
            info.app.setNewBounds(appBounds);
            info.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int)appBounds.getWidth(), (int)appBounds.getHeight(), false);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        String lastAppRendered = "";
        String lastAppUpdated = "";
        RenderInfo renderInfo = new RenderInfo();
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.delta = delta;
        // Render each app to its own framebuffer offscreen
        // This is done so that app don't need to know where they sit on the home page. All rendering can be done from x=0, y=0.
        // The framebuffer will later be pasted in the correct position on the home page
        for(AppInfo info : apps) {
            info.frameBuffer.begin();
            renderInfo.debugFont = debugFont;
            if (!brokenApps.contains(info)) {
                spriteBatch.setColor(Color.WHITE);
                Gdx.gl.glClearColor(0, 0, 0, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                try {
                    lastAppUpdated = info.app.getAppName();
                    info.app.update(updateInfo);
                } catch (Exception e) {
                    String errorMessage = lastAppUpdated + ": " + e.getMessage();
                    info.registerErrorMessage(errorMessage);
                    brokenApps.add(info);
                }

                // If one of the apps fails for any reason, don't block other apps
                try {
                    lastAppRendered = info.app.getAppName();
                    info.app.renderDebugBackground(renderInfo);
                    info.app.render(renderInfo);
                    info.app.renderDebugForeground(renderInfo);
                } catch (Exception e) {
                    String errorMessage = lastAppUpdated + ": " + e.getMessage();
                    info.registerErrorMessage(errorMessage);
                    brokenApps.add(info);
                }
            } else {

                renderAppErrors(info);
            }

            info.frameBuffer.end();
        }

        // Render all framebuffers to screen in their correct locations
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.begin();
        for(AppInfo info : apps) {
            TextureRegion textureRegion = new TextureRegion(info.frameBuffer.getColorBufferTexture());
            textureRegion.flip(false, true);
            if (info.hasErrorMessages()) {
                spriteBatch.setColor(Color.RED);
            } else {
                spriteBatch.setColor(Color.WHITE);
            }
            spriteBatch.draw(
                textureRegion,
                info.xScreenCoordinate + (appPadding / 2.0f),
                info.yScreenCoordinate + (appPadding / 2.0f)
            );
        }
        spriteBatch.end();
    }

    private void renderAppErrors(AppInfo info) {
        matrix.setToOrtho2D(0, 0, info.app.getBounds().getWidth(), info.app.getBounds().getHeight());
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.begin();
        info.renderErrorMessages(spriteBatch);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        info.app.getBounds().render(shapeRenderer);
        shapeRenderer.end();

    }

    private void renderDebugCellBorders() {
        spriteBatch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        // render vertical grid lines
        Tuple<Integer, Integer> horizontalVerticalCellCounts = findMaxNumberOfRowsAndColumnsNeeded(apps);
        for(int i = 0; i < horizontalVerticalCellCounts.left; i ++) {
            int xPos = i * (Gdx.graphics.getWidth() / horizontalVerticalCellCounts.left);
            shapeRenderer.line(xPos, 0, xPos, Gdx.graphics.getHeight());

        }

        // render horizontal grid lines
        for(int i = 0; i < horizontalVerticalCellCounts.right; i ++) {
            int yPos = i * (Gdx.graphics.getWidth() / horizontalVerticalCellCounts.left);
            shapeRenderer.line(0, yPos, Gdx.graphics.getWidth(), yPos);
        }

        shapeRenderer.end();
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
        calculateAppInfo();
        for(AppInfo info : apps) {
            if (info.hasErrorMessages()) {
                info.resizeErrorMessage();
            }
        }
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
//        return new Tuple<>(2,2);
        return new Tuple<>(maxRows, maxCols);
    }
}
