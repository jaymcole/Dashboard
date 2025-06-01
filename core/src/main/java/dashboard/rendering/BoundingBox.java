package dashboard.rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.miscDataObjects.Coordinate2D;

public class BoundingBox {
    private final float x, y, width, height;

    public BoundingBox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public BoundingBox(BoundingBox parent, float xStartPercentage, float yStartPercentage, float xEndPercentage, float yEndPercentage) {
        xStartPercentage /= 100.0f;
        yStartPercentage /= 100.0f;
        xEndPercentage /= 100.0f;
        yEndPercentage /= 100.0f;

        x = parent.x + (xStartPercentage * parent.width);
        y = parent.y + (yStartPercentage * parent.height);
        width = (xEndPercentage * parent.width) - x;
        height = (yEndPercentage * parent.height) - y;
    }


    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer.isDrawing()) {
            shapeRenderer.rect(x+1, y+1, width-2, height-2);
        }
    }

    public boolean isPointInBounds(Coordinate2D point) {
        if (point.getX() < x || point.getX() > x + width) {
            return false;
        }
        if (point.getY() < y || point.getY() > y + height) {
            return false;
        }
        return true;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
