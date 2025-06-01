package dashboard.apps.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class FontHelper {

    private static final HashMap<String, BitmapFont> LoadedFonts = new HashMap<>();

    public static BitmapFont loadFont(String fontInternalFilePath, int size) {
        String fontKey = constructLoadedFontKey(fontInternalFilePath, size);
        if (LoadedFonts.containsKey(fontKey)) {
            return LoadedFonts.get(fontKey);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontInternalFilePath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        LoadedFonts.put(fontKey, font);
        return font;
    }

    private static String constructLoadedFontKey (String fontInternalFilePath, int size) {
        return fontInternalFilePath + size;
    }

}
