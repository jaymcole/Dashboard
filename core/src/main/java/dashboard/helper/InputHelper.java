package dashboard.helper;

import com.badlogic.gdx.Gdx;
import dashboard.miscDataObjects.Coordinate2D;

public class InputHelper {
    // Libgdx uses different coordinate systems for input vs graphics.
    // The y-axis for input is inverted. Subtract screen height to un-project coordinates.
    public static Coordinate2D getMouseScreenPosition() {
        return new Coordinate2D(Gdx.input.getX(), Gdx.graphics.getHeight() -  Gdx.input.getY());
    }
}
