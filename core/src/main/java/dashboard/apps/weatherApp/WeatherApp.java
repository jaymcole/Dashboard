package dashboard.apps.weatherApp;

import com.badlogic.gdx.scenes.scene2d.Actor;
import dashboard.apps.BaseApp;
import dashboard.apps.weatherApp.weatherDataObjects.ForecastPeriod;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.graphs.BoundingBox;
import dashboard.rendering.TextBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WeatherApp extends BaseApp {

    private static final float FORECAST_UPDATE_FREQUENCY_IN_SECONDS = 30; // 3 minutes

    private float timeSinceLastUpdate;
    private List<ForecastPeriod> forecastPeriods;

    private TextBox temperatureTextBox;
    private TextBox shortForecastTextBox;
    private TextBox lastUpdateTextBox;

    public WeatherApp(BoundingBox appBounds, String[] args) {
        super(appBounds,args);
        timeSinceLastUpdate = FORECAST_UPDATE_FREQUENCY_IN_SECONDS;
        updateForecastIfOld();
    }

    @Override
    public void loadSettings(HashMap<String, String> savedSettings) {

    }

    @Override
    public void update(UpdateInfo updateInfo) {
        timeSinceLastUpdate += updateInfo.delta;
        updateForecastIfOld();
        if (forecastPeriods != null && !forecastPeriods.isEmpty()) {
            temperatureTextBox.setTextWithoutFontResize(forecastPeriods.get(0).temperature + "°F");
            shortForecastTextBox.setTextWithoutFontResize(forecastPeriods.get(0).shortForecast);
            lastUpdateTextBox.setTextWithoutFontResize(String.format("%.2f", FORECAST_UPDATE_FREQUENCY_IN_SECONDS - timeSinceLastUpdate) + "s");
        }
    }

    @Override
    public void render(RenderInfo renderInfo) {
        if (forecastPeriods != null && !forecastPeriods.isEmpty()) {
            matrix.setToOrtho2D(0, 0, appBounds.getWidth(), appBounds.getHeight());
            spriteBatch.setProjectionMatrix(matrix);
            spriteBatch.begin();
            temperatureTextBox.render(spriteBatch);
            shortForecastTextBox.render(spriteBatch);
            lastUpdateTextBox.render(spriteBatch);
            spriteBatch.end();
        }
    }

    @Override
    protected void resizeApp() {
        temperatureTextBox = new TextBox("fonts/Roboto-Regular.ttf",
            new BoundingBox(appBounds, 0, 50, 100, 100),
            "00000",
            textParameters);

        shortForecastTextBox = new TextBox("fonts/Roboto-Regular.ttf",
            new BoundingBox(appBounds, 0, 20, 100, 50),
            " Mostly something ",
            textParameters);

        lastUpdateTextBox = new TextBox("fonts/Roboto-Regular.ttf",
            new BoundingBox(appBounds, 0, 0, 100, 20),
            "000000000000",
            textParameters);
    }

    private void updateForecastIfOld() {
        if (timeSinceLastUpdate >= FORECAST_UPDATE_FREQUENCY_IN_SECONDS) {
            timeSinceLastUpdate = 0;
            WeatherServices.getGovWeatherForecastAsync("SEW", "125", "68", true).whenComplete((result, exception) -> {
                if (exception != null) {
                    System.err.println("Error in successFuture: " + exception.getMessage());
                } else {
                    try {
                        System.out.println("Received weather report update");
                        forecastPeriods = WeatherServices.parseGovWeatherForecastPeriodsFromResponse(result);
                        timeSinceLastUpdate = 0;
                    } catch (IOException e) {
                        System.err.println("Failed to retrieved weather forecasts: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    @Override
    public HashMap<String, String> saveSettings() {
        return null;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        return List.of();
    }
}
