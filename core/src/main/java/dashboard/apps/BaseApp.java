package dashboard.apps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import dashboard.enums.TextHorizontalAlignment;
import dashboard.enums.TextVerticalAlignment;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.TextParameters;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;
import dashboard.rendering.TextBox;
import org.w3c.dom.Text;

public abstract class BaseApp implements IDashboardApp {

    protected SpriteBatch spriteBatch;
    protected ShapeRenderer shapeRenderer;
    protected Matrix4 matrix = new Matrix4();
    protected BoundingBox appBounds;
    private TextBox appNameTextBox;
    protected TextParameters textParameters;

    public BaseApp() {
        textParameters = new TextParameters();
        textParameters.horizontalAlignment = TextHorizontalAlignment.CENTER;
        textParameters.verticalAlignment = TextVerticalAlignment.CENTER;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public abstract void update(UpdateInfo updateInfo);

    public void renderDebugBackground(RenderInfo renderInfo) {

    }

    @Override
    public abstract void render(RenderInfo renderInfo);

    public void renderDebugForeground(RenderInfo renderInfo) {
        spriteBatch.setProjectionMatrix(matrix);
        spriteBatch.begin();
        renderAppName(renderInfo);
        spriteBatch.end();
    }

    public void setNewBounds(BoundingBox newBounds) {
        this.appBounds = newBounds;

        appNameTextBox = new TextBox("fonts/Roboto-Regular.ttf",
            new BoundingBox(newBounds, 5, 90, 90, 99),
            getAppName(),
            textParameters);
        resize();
    }

    @Override
    public abstract void resize();

    @Override
    public String getAppName() {
        return this.getClass().getSimpleName();
    }

    protected void renderAppName(RenderInfo renderInfo) {
        if (spriteBatch.isDrawing()) {
            appNameTextBox.render(spriteBatch);
        } else {
            System.err.println("renderAppName was called before spriteBatch.begin() !!!");
        }
    }

    public BoundingBox getBounds() {
        return appBounds;
    }
}
