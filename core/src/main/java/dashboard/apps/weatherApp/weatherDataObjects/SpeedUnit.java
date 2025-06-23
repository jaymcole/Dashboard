package dashboard.apps.weatherApp.weatherDataObjects;

public enum SpeedUnit {
    MPH,
    KPH;

    public static SpeedUnit parseSpeedUnit(String unit) {
        if (unit.toLowerCase().compareTo("mph") == 0) {
            return MPH;
        }
        return KPH;
    }
}
