package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public abstract class Button extends GameObject implements IButton {
    int width = 0;
    int height = 0;

    int fillColor = 0x000000FF;
    int borderColor = 0xFFFFFFFF;

    public Button(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setColor(fillColor);
        graphics.fillRect(0, 0, width, height);

        graphics.setColor(borderColor);
        graphics.drawRect(0, 0, width, height);
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

    @Override
    public void handleEvent(IInput.TouchEvent event) {
        if(event.type == IInput.TouchEvent.TouchType.PRESSED)
        {
            int x = event.x;
            int y = event.y;
            if(x >= xPosition && x <= xPosition + width && y >= yPosition && y <= yPosition + height) {
                onPressed();
                audio.play("Click");
            }
        }

        else if(event.type == IInput.TouchEvent.TouchType.RELEASED)
        {
            int x = event.x;
            int y = event.y;
            if(x >= xPosition && x <= xPosition + width && y >= yPosition && y <= yPosition + height)
                onReleased();
        }

    }
}

 interface IButton {
    void onPressed();
    void onReleased();
 }
