package dashboard.miscDataObjects;

public class Coordinate2D {

    private float x, y;

    public Coordinate2D() {
        x = 0;
        y = 0;
    }

    public Coordinate2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }
}
