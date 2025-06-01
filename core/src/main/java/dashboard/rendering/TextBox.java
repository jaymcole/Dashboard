package dashboard.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import dashboard.apps.helper.FontHelper;
import dashboard.enums.TextHorizontalAlignment;
import dashboard.miscDataObjects.TextParameters;

public class TextBox {

    private final String fontFilePath;
    private final TextParameters parameters;
    private BitmapFont font;
    private BoundingBox bounds;
    private String text;

    private GlyphLayout layout;

    private float textX;
    private float textY;

    public TextBox (String fontFilePath, BoundingBox bounds, String text, TextParameters params) {
        this.parameters = params;
        this.bounds = bounds;
        this.text = text;
        this.fontFilePath = fontFilePath;
        findCorrectFontSize();
        setFontDrawCoordinates();
    }
    public void setTextWithoutFontResize(String newText) {
        this.text = newText;
        setFontDrawCoordinates();
    }

    // Warning: very expensive update
    public void setBounds(BoundingBox newBounds) {
        this.bounds = newBounds;
        findCorrectFontSize();
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    private void setFontDrawCoordinates() {
        //TODO update textX/textY positions based on parameters (align left/right etc)
        layout.setText(font, text);
        switch (parameters.horizontalAlignment) {
            case LEFT:
                break;
            case CENTER:
            textX = ((bounds.getWidth() - layout.width) / 2) + bounds.getX();
                break;
            case RIGHT:
                break;
        }
        switch (parameters.verticalAlignment) {
            case TOP:
                break;
            case CENTER:
                textY = ((bounds.getHeight() + layout.height) / 2) + bounds.getY();
                break;
            case BOTTOM:
                break;
        }
    }

    private void findCorrectFontSize() {
        int tooSmall = 5;
        int tooLarge = 500;
        int currentFontSize = (tooLarge + tooSmall) / 2;
        while (tooLarge - tooSmall > 1) {
            currentFontSize = (tooLarge + tooSmall) / 2;
            this.font = FontHelper.loadFont(fontFilePath, currentFontSize);
            layout = new GlyphLayout(font, text);
            if (layout.width > bounds.getWidth() || layout.height > bounds.getHeight()) {
                tooLarge = currentFontSize;
            } else if (layout.height <= bounds.getWidth() || layout.height <= bounds.getHeight()) {
                tooSmall = currentFontSize;
            }
            System.out.println("Text: " + text + " layout.width: " + layout.width + " bounds.getWidth: " + bounds.getWidth());
        }

        if (layout.width > bounds.getWidth()) {
            currentFontSize--;
            this.font = FontHelper.loadFont(fontFilePath, currentFontSize);
        }
    }

    public void render(SpriteBatch spriteBatch) {
        font.draw(spriteBatch, text, textX, textY);
    }

}
