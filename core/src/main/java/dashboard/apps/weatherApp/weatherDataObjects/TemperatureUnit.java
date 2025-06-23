package dashboard.apps.weatherApp.weatherDataObjects;

public enum TemperatureUnit {
    Fahrenheit,
    Celsius,
    Kelvin;

    public static TemperatureUnit parseTemperatureUnit(String unit) {
        if (unit.toLowerCase().compareTo("f") == 0 || unit.toLowerCase().compareTo("fahrenheit") == 0) {
            return Fahrenheit;
        }
        if (unit.toLowerCase().compareTo("c") == 0 || unit.toLowerCase().compareTo("Celsius") == 0) {
            return Celsius;
        }
        return Kelvin;
    }
}
