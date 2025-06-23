package dashboard.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import dashboard.helper.AppLoader;
import dashboard.helper.FontHelper;
import dashboard.miscDataObjects.*;
import dashboard.rendering.BoundingBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static dashboard.miscDataObjects.AppInfo.APP_PADDING;

/** First screen of the application. Displayed after the application is created. */
public class Home implements Screen {

    private static final String APP_VERSION = "1.0.2";
    private static final String LAST_UPDATE_MESSAGE = "Make weather report call async";

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private List<AppInfo> apps;
    private HashSet<AppInfo> brokenApps;
    private Matrix4 matrix = new Matrix4();
    private BitmapFont debugFont;
    private AppInfo settingsOpened = null;
    private Stage stage;

    @Override
    public void show() {
        brokenApps = new HashSet<>();
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        debugFont = FontHelper.loadFont("fonts/Roboto-Regular.ttf", 12);

        apps = new ArrayList<>();
        try {
            apps = AppLoader.loadAppsFromLayoutFile("layouts/testAppLayout.csv");
            calculateAppInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void constructAppSettings(AppInfo info) {
        stage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setPosition(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f);

        for(Actor actor : info.app.getSettingsUiActors()) {
            root.add(actor).expand();
            root.row();
        }

        Skin skin = new Skin((Gdx.files.internal("skins/metalui/metal-ui.json")));
        TextButton textButton = new TextButton("Close Settings", skin);
        textButton.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                settingsOpened = null;
                AppLoader.SaveAppSettings(info);
            }
        });
        root.add(textButton);
        stage.addActor(root);
    }

    private void calculateAppInfo() {
        int cellWidth = Gdx.graphics.getWidth() / AppLoader.Total_Rows;
        int cellHeight = Gdx.graphics.getHeight() / AppLoader.Total_Columns;

        for(AppInfo info : apps) {
            info.resize(new BoundingBox(
                info.xCell * cellWidth,
                info.yCell * cellHeight,
                info.xCellCount * cellWidth,
                info.yCellCount * cellHeight
            ));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(AppInfo info : apps) {
            if (!brokenApps.contains(info)) {
                updateApp(info, delta);
                renderAppToFrameBuffer(info);
            }
        }

        renderAppsToHomeScreen();
        renderOptionsButtonBoundingBoxes();

        for(AppInfo info : apps) {
            if (brokenApps.contains(info)) {
                renderAppErrors(info);
            }
        }
        if (settingsOpened != null) {
            renderSettings();
        }

        List<String> informationLines = new ArrayList<>();
        informationLines.add("Message: " + LAST_UPDATE_MESSAGE);
        informationLines.add("Version: " + APP_VERSION);
        informationLines.add("FPS: " + Gdx.graphics.getFramesPerSecond());
        spriteBatch.begin();
        debugFont.setColor(Color.CYAN);
        debugFont.draw(spriteBatch, String.join("\n", informationLines), 10, Gdx.graphics.getHeight() - 10);
        spriteBatch.end();
    }

    private void renderSettings() {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,0.9f));
        shapeRenderer.rect(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        stage.act();
        stage.draw();
    }

    private void updateApp(AppInfo info, float delta) {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.delta = delta;

        try {
            info.update();
            info.app.update(updateInfo);

            if (settingsOpened == null && Gdx.input.justTouched() && info.cursorOverOptions) {
                constructAppSettings(info);
                settingsOpened = info;
            }
        } catch (Exception e) {
            String errorMessage = info.app.getAppName() + ": " + e.getMessage();
            info.registerErrorMessage(errorMessage);
            brokenApps.add(info);
        }
    }

    private void renderAppToFrameBuffer(AppInfo info) {
        RenderInfo renderInfo = new RenderInfo();
        info.frameBuffer.begin();
        renderInfo.debugFont = debugFont;
        spriteBatch.setColor(Color.WHITE);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        try {
            info.app.renderDebugBackground(renderInfo);
            info.app.render(renderInfo);
            info.app.renderDebugForeground(renderInfo);
        } catch (Exception e) {
            String errorMessage = info.app.getAppName() + ": " + e.getMessage();
            info.registerErrorMessage(errorMessage);
            brokenApps.add(info);
        }
        info.frameBuffer.end();
    }

    private void renderAppErrors(AppInfo info) {
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.begin();
        info.renderErrorMessages(spriteBatch);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        info.getBounds().render(shapeRenderer);
        shapeRenderer.line(0,0, info.getBounds().getX(), info.getBounds().getY());

        shapeRenderer.end();
    }

     private void renderAppsToHomeScreen() {
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
                 info.getScreenCoordinates().getX() + (APP_PADDING / 2.0f),
                 info.getScreenCoordinates().getY() + (APP_PADDING / 2.0f)
             );
         }
         spriteBatch.end();
     }

    private void renderOptionsButtonBoundingBoxes()
    {
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for(AppInfo info : apps) {
//            shapeRenderer.setColor(Color.BLUE);
//            if (info.cursorOverApp) {
//                shapeRenderer.setColor(Color.GREEN);
//            }
//            info.getBounds().render(shapeRenderer);

            shapeRenderer.setColor(Color.BLUE);
            if (info.cursorOverOptions) {
                shapeRenderer.setColor(Color.GREEN);
            }

            if (info == settingsOpened) {
                shapeRenderer.setColor(Color.MAGENTA);
            }

            info.optionsButton.render(shapeRenderer);
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        if (stage != null) {
            stage.getViewport().update(width, height, false);
        }
        // Resize your screen here. The parameters represent the new window size.
        calculateAppInfo();
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
}
