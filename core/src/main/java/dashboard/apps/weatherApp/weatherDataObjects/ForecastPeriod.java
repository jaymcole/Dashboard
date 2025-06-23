package dashboard.apps.weatherApp.weatherDataObjects;

import java.time.LocalDateTime;
import java.util.Map;

public class ForecastPeriod {


//    api.weather.gov

//    {
//        "number": 1,
//        "name": "This Afternoon",
//        "startTime": "2025-06-22T12:00:00-07:00",
//        "endTime": "2025-06-22T18:00:00-07:00",
//        "isDaytime": true,
//        "temperature": 72,
//        "temperatureUnit": "F",
//        "temperatureTrend": "",
//        "probabilityOfPrecipitation": {
//        "unitCode": "wmoUnit:percent",
//            "value": 10
//        },
//        "windSpeed": "5 mph",
//        "windDirection": "SW",
//        "icon": "https://api.weather.gov/icons/land/day/sct?size=medium",
//        "shortForecast": "Mostly Sunny",
//        "detailedForecast": "Mostly sunny. High near 72, with temperatures falling to around 70 in the afternoon. Southwest wind around 5 mph."
//    }

    public static final String DEFAULT_MISSING = "<missing>";
    public static final String DEFAULT_DATETIME = "9999-88-77T00:00:00";
    public final int number;
    public final String name;
    public final LocalDateTime startTime;
    public final LocalDateTime endTime;
    public final boolean isDaytime;
    public final Integer temperature;
    public final TemperatureUnit temperatureUnit;
    public final float windSpeed;
    public final SpeedUnit windSpeedUnit;
    public final MapDirection windDirection;
    public final String shortForecast;
    public final String detailedForecast;


    public ForecastPeriod(Map<String, Object> period) {
        number = (Integer)period.getOrDefault("number", "0");
        name = (String)period.getOrDefault("name", DEFAULT_MISSING);

        String startTimeRaw = ((String)period.getOrDefault("startTime", DEFAULT_DATETIME)).substring(0, 19);
        startTime = LocalDateTime.parse(startTimeRaw);


        String endTimeRaw = ((String)period.getOrDefault("endTime", DEFAULT_DATETIME)).substring(0, 19);
        endTime = LocalDateTime.parse(endTimeRaw);

        isDaytime = (Boolean)period.getOrDefault("isDaytime", "false");
        temperature = (Integer)period.getOrDefault("temperature", 0);
        temperatureUnit = TemperatureUnit.parseTemperatureUnit((String)period.getOrDefault("temperatureUnit", "F"));

        String[] windParts = ((String)period.getOrDefault("windSpeed", "0 mph")).split(" ");
        windSpeed = Float.parseFloat(windParts[0]);
        windSpeedUnit = SpeedUnit.parseSpeedUnit(windParts[1]);

        windDirection = MapDirection.parseMapDirection((String)period.getOrDefault("windDirection", "N"));
        shortForecast = (String)period.getOrDefault("shortForecast", DEFAULT_MISSING);
        detailedForecast = (String)period.getOrDefault("detailedForecast", DEFAULT_MISSING);
    }

    public int getNumber() {
        return number;
    }

}
