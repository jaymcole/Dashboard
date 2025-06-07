package dashboard.apps.clockApp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.helper.StringHelper;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;
import dashboard.rendering.TextBox;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class ClockApp extends BaseApp {
    private TextBox clockBox;

    public ClockApp(BoundingBox appBounds) {
        super(appBounds);
    }

    @Override
    public void loadSettings(HashMap<String, String> savedSettings) {

    }

    @Override
    public void update(UpdateInfo updateInfo) {
        String formattedTime = LocalDateTime.now().getHour() + "";
        formattedTime += ":";
        formattedTime += StringHelper.padLeft(String.valueOf(LocalDateTime.now().getMinute()), "0", 2);
        formattedTime += ":";
        formattedTime += StringHelper.padLeft(String.valueOf(LocalDateTime.now().getSecond()), "0", 2);
        clockBox.setTextWithoutFontResize(formattedTime);
    }

    @Override
    public void render(RenderInfo renderInfo) {
        matrix.setToOrtho2D(0, 0, appBounds.getWidth(), appBounds.getHeight());
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.begin();
        clockBox.render(spriteBatch);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.CORAL);
        clockBox.getBounds().render(shapeRenderer);
        shapeRenderer.end();

    }

    @Override
    public void resize(BoundingBox newBounds) {
        this.appBounds = newBounds;
        clockBox = new TextBox("fonts/Roboto-Regular.ttf", new BoundingBox(appBounds, 5, 5,95, 95), "00:00:00 PM", textParameters);
    }

    @Override
    public HashMap<String, String> getCurrentAppSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return List.of();
    }
}
