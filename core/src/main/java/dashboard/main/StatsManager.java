package dashboard.main;

import dashboard.miscDataObjects.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsManager {

    public static final int MAX_RECORDS_PER_SOURCE = 10000;

    private static Map<String, List<Stat>> stats = new HashMap<>();

    public static List<Stat> getStatList(String statsKey) {
        if (!stats.containsKey(statsKey)) {
            System.out.println("Stats record for " + statsKey + " does not exist. Creating it now.");
            stats.put(statsKey, new ArrayList<>());
        }
        return stats.get(statsKey);
    }

    public static void AddStatRecord(String statsKey, float value) {
        List<Stat> stats = getStatList(statsKey);
        stats.add(new Stat(value, System.currentTimeMillis()));
        if (stats.size() > MAX_RECORDS_PER_SOURCE) {
            stats.remove(0);
        }
    }

    public static void clearStatRecords(String statsKey) {
        getStatList(statsKey).clear();
    }

    public static Map<String, List<Stat>> getAllStatsWithSuffix(String suffix) {
        Map<String, List<Stat>> matchingStats = new HashMap<>();
        for(Map.Entry<String, List<Stat>> statList : stats.entrySet()) {
            if (statList.getKey().endsWith(suffix)) {
                matchingStats.put(statList.getKey(), statList.getValue());
            }
        }
        return matchingStats;
    }
}
