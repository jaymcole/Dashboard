package dashboard.apps.weatherApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import dashboard.apps.weatherApp.weatherDataObjects.ForecastPeriod;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeatherServices {
//    https://www.weather.gov/documentation/services-web-api

    public static List<ForecastPeriod> getGovWeatherForecast(String office, String gridX, String gridY, boolean hourly) throws IOException {
        String urlString = "https://api.weather.gov/gridpoints/" + office + "/" + gridX + "," + gridY + "/forecast";
        if (hourly) {
            urlString += "/hourly";
        }
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response, Map.class);

        ArrayList<Map<String, Object>> periodObjects = (ArrayList<Map<String, Object>>)(((Map<String, Object>)map.get("properties")).get("periods"));
        List<ForecastPeriod> periods = periodObjects.stream()
            .map(pObject -> new ForecastPeriod(pObject))
            .sorted(Comparator.comparingInt(ForecastPeriod::getNumber))
            .collect(Collectors.toList());
        return periods;
    }

}
