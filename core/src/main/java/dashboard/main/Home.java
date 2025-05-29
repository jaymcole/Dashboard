package dashboard.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import dashboard.apps.testApps.DebugBordersApp;
import dashboard.apps.testApps.TextureTestApp;
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
    private Matrix4 matrix = new Matrix4();
    private BitmapFont debugFont;
    private final int appPadding = 10;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        Texture libgdxTestTexture = new Texture(Gdx.files.internal("libgdx128.png"));

        apps = new ArrayList<>();
        apps.add(new AppInfo(new DebugBordersApp(Color.BLUE), 0,0,1,1));
        apps.add(new AppInfo(new TextureTestApp(libgdxTestTexture), 0,1,2,1));
        apps.add(new AppInfo(new DebugBordersApp(Color.YELLOW), 0,2,3,1));
        apps.add(new AppInfo(new TextureTestApp(libgdxTestTexture), 2,0,1,1));
        calculateAppInfo();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        debugFont = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

    }

    private void calculateAppInfo() {
        Tuple<Integer, Integer> horizontalVerticalCellCounts = findMaxNumberOfRowsAndColumnsNeeded(apps);
        int cellWidth = Gdx.graphics.getWidth() / horizontalVerticalCellCounts.left;
        int cellHeight = Gdx.graphics.getHeight() / horizontalVerticalCellCounts.right;

        for(AppInfo info : apps) {
            info.xScreenCoordinate = info.xCell * cellWidth;
            info.yScreenCoordinate = info.yCell * cellHeight;

            info.maxScreenWidth = (info.xCellCount * cellWidth) - appPadding;
            info.maxScreenHeight = (info.yCellCount * cellHeight) - appPadding;
            info.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, info.maxScreenWidth, info.maxScreenHeight, false);
        }
    }

    @Override
    public void render(float delta) {

        RenderInfo renderInfo = new RenderInfo();
        // Render each app to its own framebuffer offscreen
        // This is done so that app don't need to know where they sit on the home page. All rendering can be done from x=0, y=0.
        // The framebuffer will later be pasted in the correct position on the home page
        for(AppInfo info : apps) {
            info.frameBuffer.begin();
            renderInfo.maxWidth = info.maxScreenWidth;
            renderInfo.maxHeight = info.maxScreenHeight;
            renderInfo.debugFont = debugFont;
            info.app.render(renderInfo);
            info.frameBuffer.end();
        }

        // Render all framebuffers to screen in their correct locations
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.begin();
        for(AppInfo info : apps) {
            TextureRegion textureRegion = new TextureRegion(info.frameBuffer.getColorBufferTexture());
            textureRegion.flip(false, true);
            spriteBatch.draw(
                textureRegion,
                info.xScreenCoordinate + (appPadding / 2),
                info.yScreenCoordinate + (appPadding / 2)
            );
        }
        spriteBatch.end();

        renderDebugCellBorders();
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
