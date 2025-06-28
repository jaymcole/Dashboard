package dashboard.miscDataObjects;

public class Stat {
    public final float statMetric;
    public final long timeStamp;

    public Stat(float statMetric, long timeStamp) {
        this.statMetric = statMetric;
        this.timeStamp = timeStamp;
    }
}
