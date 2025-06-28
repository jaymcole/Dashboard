package dashboard.miscDataObjects;

import java.util.HashMap;
import java.util.Map;

public class AppConfigs {
    public static final String APP_CLASS_KEY = "appClass";
    public static final String APP_ARGS_KEY = "appArgs";

    public final String appClass;
    public final Object appArgs;

    public AppConfigs(Map<String, Object> configs) {
        appClass = (String)configs.getOrDefault(APP_CLASS_KEY, "missing");
        appArgs = configs.getOrDefault(APP_ARGS_KEY, null);
    }

    public static AppConfigs getDefaultEmptyConfigs() {
        HashMap<String, Object> configs = new HashMap<>();
        configs.put(APP_CLASS_KEY, "default");
        configs.put(APP_ARGS_KEY, null);
        return new AppConfigs(configs);
    }
}
