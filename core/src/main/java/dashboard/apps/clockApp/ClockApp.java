package dashboard.apps.clockApp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dashboard.apps.BaseApp;
import dashboard.helper.StringHelper;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.graphs.BoundingBox;
import dashboard.rendering.TextBox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClockApp extends BaseApp {

    private enum ClockHourFormat {
        HOUR_12,
        HOUR_24
    }

    private TextBox clockBox;
    private ClockHourFormat format;

    public ClockApp(BoundingBox appBounds, String[] args) {
        super(appBounds,args);
        format = ClockHourFormat.HOUR_12;
    }

    @Override
    public void loadSettings(HashMap<String, String> loadedSettings) {
        if (loadedSettings.containsKey(ClockHourFormat.class.getName())) {
            String value = loadedSettings.get(ClockHourFormat.class.getName());
            if (value.compareTo(ClockHourFormat.HOUR_24.name()) == 0) {
                format = ClockHourFormat.HOUR_24;
            } else if (value.compareTo(ClockHourFormat.HOUR_12.name()) == 0) {
                format = ClockHourFormat.HOUR_12;
            }
        }
    }

    @Override
    public HashMap<String, String> saveSettings() {
        HashMap<String, String> settingsToSave = new HashMap<>();
        settingsToSave.put(ClockHourFormat.class.getName(), format.name());
        return settingsToSave;
    }

    @Override
    public void update(UpdateInfo updateInfo) {
        String formattedTime = "";
        switch (format) {
            case HOUR_12 -> {
                formattedTime = LocalDateTime.now().getHour() % 12 + "";
                formattedTime += ":";
                formattedTime += StringHelper.padLeft(String.valueOf(LocalDateTime.now().getMinute()), "0", 2);
                formattedTime += ":";
                formattedTime += StringHelper.padLeft(String.valueOf(LocalDateTime.now().getSecond()), "0", 2);
            }
            case HOUR_24 -> {
                formattedTime = LocalDateTime.now().getHour() + "";
                formattedTime += ":";
                formattedTime += StringHelper.padLeft(String.valueOf(LocalDateTime.now().getMinute()), "0", 2);
                formattedTime += ":";
                formattedTime += StringHelper.padLeft(String.valueOf(LocalDateTime.now().getSecond()), "0", 2);
            }
        }
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
    public void resizeApp() {
        clockBox = new TextBox("fonts/Roboto-Regular.ttf", new BoundingBox(appBounds, 5, 5,95, 95), "00:00:00 PM", textParameters);
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        List<Actor> widgets = new ArrayList<>();

        Skin skin = new Skin((Gdx.files.internal("skins/metalui/metal-ui.json")));

        Table table = new Table();

        TextButton use12HourButton = new TextButton("12hr", skin);
        use12HourButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                format = ClockHourFormat.HOUR_12;
            }
        });
        table.add(use12HourButton);

        TextButton use24HourButton = new TextButton("24hr", skin);
        use24HourButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                format = ClockHourFormat.HOUR_24;
            }
        });
        table.add(use24HourButton);

        widgets.add(table);
        return widgets;
    }
}
