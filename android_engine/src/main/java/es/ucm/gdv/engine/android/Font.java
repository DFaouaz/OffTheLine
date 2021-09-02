package es.ucm.gdv.engine.android;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import es.ucm.gdv.engine.IFont;

public class Font implements IFont {

    private Typeface font = null;
    private int size = 0;
    private boolean bold = false;

    public Font(AssetManager assetManager, String filename, int size, boolean isBold) {
        font = Typeface.createFromAsset(assetManager, filename);
        bold = isBold;
        this.size = size;
    }

    public Typeface getFont() {
        return font;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isBold() {
        return bold;
    }

    @Override
    public boolean isItalic() {
        return font.isItalic();
    }

    @Override
    public String getName() {
        return font.toString();
    }
}
