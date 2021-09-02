package es.ucm.gdv.engine.desktop;

import java.io.FileInputStream;
import java.io.InputStream;

import es.ucm.gdv.engine.IFont;

public class Font implements IFont {

    private java.awt.Font font = null;

    public Font(String filename, int size, boolean isBold){
        java.awt.Font baseFont;
        try (InputStream is = new FileInputStream(filename)) {
            baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            System.err.println("Error cargando la fuente: " + e);
            return;
        }

        int bold = isBold ? java.awt.Font.BOLD : java.awt.Font.PLAIN;
        font = baseFont.deriveFont(bold, size);
    }

    public java.awt.Font getFont(){
        return font;
    }

    @Override
    public int getSize() {
        return font.getSize();
    }

    @Override
    public boolean isBold() {
        return font.isBold();
    }

    @Override
    public boolean isItalic() {
        return font.isItalic();
    }

    @Override
    public String getName() {
        return font.getName();
    }
}
