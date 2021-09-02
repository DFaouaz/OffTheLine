package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IFont;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Text extends GameObject {
    private String text = "";
    private IFont font = null;
    private int textColor = 0xFFFFFFFF;

    public Text(String text, IFont font) {
        this.text = text;
        this.font = font;
    }

    public Text(String text, IFont font, int color) {
       this.text = text;
       this.font = font;
       this.textColor = color;
    }

    @Override
    public void render(IGraphics graphics) {
        if (font == null) return;

        graphics.setColor(textColor);
        graphics.setFont(font);

        graphics.drawText(text, 0, 0);
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IFont getFont() {
        return font;
    }

    public void setFont(IFont font) {
        this.font = font;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {

    }
}
