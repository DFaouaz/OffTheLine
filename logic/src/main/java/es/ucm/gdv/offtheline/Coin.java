package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Coin extends GameObject {
    private double initialPositionX = 0;
    private double initialPositionY = 0;
    private double initialAngle = 0;

    private double radius = 0.0;
    private double speed = 0.0;
    private double angle = 0.0;

    private int side = 8;
    private double rotationVelocity = 180.0;

    boolean collected = false;
    boolean destroyed = false;

    float timer = 0.0f;
    float timeToDestroy = 0.5f;

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setInitialAngle(double angle) {
        this.angle = angle;
        this.initialAngle = angle;
    }

    @Override
    public void render(IGraphics graphics) {
        if (destroyed) return;

        graphics.setColor(0xFFFF00FF);
        graphics.setStrokeWidth(2.5f);

        graphics.drawRect(-side / 2.0f, -side /  2.0f, side /  2.0f, side /  2.0f);
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {
        if (destroyed) return;

        // Actualizamos movimiento alrededor del pivote
        double xPos = initialPositionX + (radius - 1) * Math.cos(Math.toRadians(angle));
        double yPos = initialPositionY + (radius - 1) * Math.sin(Math.toRadians(angle));
        setPosition(xPos, yPos);

        rotate(rotationVelocity * deltaTime);
        angle += speed * deltaTime;

        if (collected) {
            double scaleDeltaMax = 2.0;
            double scaleDelta = (scaleDeltaMax * (timer * 2 / timeToDestroy));
            if (scaleDelta < scaleDeltaMax) setScale(1.0 + scaleDelta, 1.0 + scaleDelta);
            timer += deltaTime;
            if (timer > timeToDestroy)
                destroyed = true;
        }

    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {

    }

    public void setInitialPosition(double xPosition, double yPosition) {
        initialPositionX = xPosition;
        initialPositionY = yPosition;
    }

    // Establece la moneda como "recogida"
    public void collect() {
        collected = true;
        audio.play("Coin");
    }

    public boolean isCollected() {
        return collected;
    }

    // Resetea la moneda a su estado inicial
    public void reset() {
        collected = false;
        destroyed = false;
        timer = 0.0f;
        angle = initialAngle;
        setScale(1.0, 1.0);
        setRotation(0.0);
    }
}
