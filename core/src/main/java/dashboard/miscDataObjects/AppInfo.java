package dashboard.miscDataObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import dashboard.apps.BaseApp;
import dashboard.enums.TextHorizontalAlignment;
import dashboard.enums.TextVerticalAlignment;
import dashboard.rendering.BoundingBox;
import dashboard.rendering.TextBox;

import java.util.ArrayList;
import java.util.List;

import static dashboard.apps.helper.InputHelper.getMouseScreenPosition;

public class AppInfo {
    public static final int APP_PADDING = 3;

    public final BaseApp app;
    public int xCell; // top-most row
    public int xCellCount; // in rows
    public int yCell; // left-most column
    public int yCellCount; // in columns
    public FrameBuffer frameBuffer;
    private BoundingBox onScreenBounds;

    public BoundingBox optionsButton;
    private int optionsButtonSize = 20;

    public boolean cursorOverApp = false;
    public boolean cursorOverOptions = false;

    private TextBox errorMessageTextBox;
    private final List<String> errorMessages;

    public AppInfo (BaseApp app, int xCell, int yCell, int xCellCount, int yCellCount) {
        errorMessages = new ArrayList<>();
        this.app = app;
        this.xCell = xCell;
        this.xCellCount = xCellCount;
        this.yCell = yCell;
        this.yCellCount = yCellCount;

        optionsButton = new BoundingBox(0, 0, 20, 20);
    }

    public void update() {
        Coordinate2D cursorPosition = getMouseScreenPosition();
        cursorOverApp = onScreenBounds.isPointInBounds(cursorPosition);
        cursorOverOptions = optionsButton.isPointInBounds(cursorPosition);
    }

    public void resize(BoundingBox newBounds) {
        onScreenBounds = newBounds;
        BoundingBox appBounds = new BoundingBox(
            APP_PADDING,
            APP_PADDING,
            newBounds.getWidth() - (APP_PADDING * 2.0f),
            newBounds.getHeight() - (APP_PADDING * 2.0f)
        );
        app.setNewBounds(appBounds);

        optionsButton = new BoundingBox(newBounds.getX() + onScreenBounds.getWidth() - optionsButtonSize, newBounds.getY() + onScreenBounds.getHeight() - optionsButtonSize, optionsButtonSize, optionsButtonSize);
        resizeErrorMessage();
    }

    public void resizeErrorMessage() {
        if (hasErrorMessages()) {
            TextParameters params = new TextParameters();
            params.horizontalAlignment = TextHorizontalAlignment.CENTER;
            params.verticalAlignment = TextVerticalAlignment.CENTER;
            errorMessageTextBox = new TextBox("fonts/Roboto-Regular.ttf", app.getBounds(), String.join("\n", errorMessages), params);
        }
    }

    public BoundingBox getBounds()  {
        return onScreenBounds;
    }

    public boolean hasErrorMessages () {
        return !errorMessages.isEmpty();
    }

    public void renderErrorMessages(SpriteBatch spriteBatch) {
        spriteBatch.setColor(Color.RED);
        errorMessageTextBox.render(spriteBatch);
    }

    public void registerErrorMessage(String error) {
        this.errorMessages.add(error);
        System.err.println(error);
        resizeErrorMessage();
    }

    public Coordinate2D getScreenCoordinates() {
        return new Coordinate2D(onScreenBounds.getX(), onScreenBounds.getY());
    }
}
