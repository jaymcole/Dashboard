package dashboard.miscDataObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import dashboard.apps.BaseApp;
import dashboard.enums.TextHorizontalAlignment;
import dashboard.enums.TextVerticalAlignment;
import dashboard.rendering.TextBox;

import java.util.ArrayList;
import java.util.List;

public class AppInfo {

    public final BaseApp app;
    public int xCell; // top-most row
    public int xCellCount; // in rows
    public int yCell; // left-most column
    public int yCellCount; // in columns
    public FrameBuffer frameBuffer;
    public int xScreenCoordinate;
    public int yScreenCoordinate;

    private TextBox errorMessageTextBox;
    private List<String> errorMessages;

    public AppInfo (BaseApp app, int xCell, int yCell, int xCellCount, int yCellCount) {
        errorMessages = new ArrayList<>();
        this.app = app;
        this.xCell = xCell;
        this.xCellCount = xCellCount;
        this.yCell = yCell;
        this.yCellCount = yCellCount;
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

    public void resizeErrorMessage() {
        TextParameters params = new TextParameters();
        params.horizontalAlignment = TextHorizontalAlignment.CENTER;
        params.verticalAlignment = TextVerticalAlignment.CENTER;
        errorMessageTextBox = new TextBox("fonts/Roboto-Regular.ttf", app.getBounds(), String.join("\n", errorMessages), params);

    }
}
