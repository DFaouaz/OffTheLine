package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Particle extends GameObject {

    int length = 6;
    float rotationVelocity = 180.0f;
    float incAngle = 180.0f;
    float speed = 120.0f;
    float incSpeed = 120.0f;

    float dirX = 0.0f;
    float dirY = 0.0f;

    @Override
    public void render(IGraphics graphics) {
        graphics.translate(-xPosition, -yPosition);
        graphics.drawLine((int) xPosition - (length - 1) / 2, (int) yPosition - (length - 1) / 2,
                (int) xPosition + (length - 1) / 2, (int) yPosition + (length - 1) / 2);
        graphics.translate(xPosition, yPosition);
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {

        if (rotationVelocity > 0)
            rotationVelocity -= incAngle * deltaTime;

        if (speed > 0)
            speed -= incSpeed * deltaTime;

        rotate(rotationVelocity * deltaTime);
        translate(dirX * speed * deltaTime, dirY * speed * deltaTime);
    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {

    }

    public void setDir(float dirX, float dirY) {
        this.dirX = dirX;
        this.dirY = dirY;
    }

    public void setSpeed(float speed){
        this.speed = speed;
        this.incSpeed = speed;
    }

    // Resetear la particula
    public void reset()
    {
        xPosition = 0.0f;
        yPosition = 0.0f;

        rotationVelocity = 180.0f;
        incAngle = 180.0f;

        float dirX = (float) Math.random() * 2 - 1;
        float dirY = (float) Math.random() * 2 - 1;

        float magnitude = (float) Math.sqrt(Math.pow(dirX, 2.0f) + Math.pow(dirY, 2.0f));

        setRotation(Math.random() * 360.0);
        setSpeed((float) Math.random() * 60.0f + 30.0f);
        setDir(dirX / magnitude, dirY / magnitude);
    }
}
