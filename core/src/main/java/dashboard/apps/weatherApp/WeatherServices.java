package dashboard.apps.weatherApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import dashboard.apps.weatherApp.weatherDataObjects.ForecastPeriod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WeatherServices {
//    https://www.weather.gov/documentation/services-web-api

    public static HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<String> getGovWeatherForecastAsync(String office, String gridX, String gridY, boolean hourly) {
        String urlString = "https://api.weather.gov/gridpoints/" + office + "/" + gridX + "," + gridY + "/forecast";
        if (hourly) {
            urlString += "/hourly";
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(urlString))
            .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }

    public static List<ForecastPeriod> parseGovWeatherForecastPeriodsFromResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response, Map.class);

        ArrayList<Map<String, Object>> periodObjects = (ArrayList<Map<String, Object>>)(((Map<String, Object>)map.get("properties")).get("periods"));
        List<ForecastPeriod> periods = periodObjects.stream()
            .map(pObject -> new ForecastPeriod(pObject))
            .sorted(Comparator.comparingInt(ForecastPeriod::getNumber))
            .collect(Collectors.toList());
        return periods;
    }

//    public static List<ForecastPeriod> getGovWeatherForecast(String office, String gridX, String gridY, boolean hourly) throws IOException {
//        String urlString = "https://api.weather.gov/gridpoints/" + office + "/" + gridX + "," + gridY + "/forecast";
//        if (hourly) {
//            urlString += "/hourly";
//        }
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        InputStream is = conn.getInputStream();
//        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String,Object> map = mapper.readValue(response, Map.class);
//
//        ArrayList<Map<String, Object>> periodObjects = (ArrayList<Map<String, Object>>)(((Map<String, Object>)map.get("properties")).get("periods"));
//        List<ForecastPeriod> periods = periodObjects.stream()
//            .map(pObject -> new ForecastPeriod(pObject))
//            .sorted(Comparator.comparingInt(ForecastPeriod::getNumber))
//            .collect(Collectors.toList());
//        return periods;
//    }
}
