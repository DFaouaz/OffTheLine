package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Enemy extends GameObject {
    private double initialPositionX = 0.0;
    private double initialPositionY = 0.0;
    private double initialAngle = 0.0;
    private double speed = 0.0;
    private double length = 0.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double time1 = 0.0;
    private double time2 = 0.0;

    private double timer1 = 0.0;
    private double timer2 = 0.0;
    private int inverted = 1;

    @Override
    public void render(IGraphics graphics) {
        graphics.setColor(0xFF0000FF);
        graphics.setStrokeWidth(1.5f);
        graphics.drawLine((int) (-length / 2.0), 0, (int) (length / 2.0), 0);
        graphics.setColor(0xFFFFFFFF);
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    // Movimiento y rotacion de los enemigos
    @Override
    public void update(double deltaTime) {
        rotate(speed * deltaTime);

        if (timer2 >= time2) {

            timer1 += deltaTime * inverted;
            double ratio = timer1 / time1;

            if (ratio >= 1.0) {
                ratio = 1.0;
                timer1 = time1;
                timer2 = 0.0;
                inverted *= -1;
            } else if (ratio <= 0.0) {
                ratio = 0.0;
                timer1 = 0.0;
                timer2 = 0.0;
                inverted *= -1;
            }

            double lerpX = initialPositionX + offsetX * ratio;
            double lerpY = initialPositionY + offsetY * ratio;

            setPosition(lerpX, lerpY);
        } else
            timer2 += deltaTime;

    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    public double getInitialPositionX() {
        return initialPositionX;
    }

    public double getInitialPositionY() {
        return initialPositionY;
    }

    public void setInitialPosition(double xPosition, double yPosition) {
        initialPositionX = xPosition;
        initialPositionY = yPosition;
    }

    public void setInitialAngle(double angle) {
        initialAngle = angle;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffset(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public double getTime1() {
        return time1;
    }

    public void setTime1(double time1) {
        this.time1 = time1;
    }

    public double getTime2() {
        return time2;
    }

    public void setTime2(double time2) {
        this.time2 = time2;
        this.timer2 = time2;
    }

    public double getFromX() {
        return xPosition - (length - 1) / 2 * Math.cos(Math.toRadians(rotation));
    }

    public double getFromY() {
        return yPosition - (length - 1) / 2 * Math.sin(Math.toRadians(rotation));
    }

    public double getToX() {
        return xPosition + (length - 1) / 2 * Math.cos(Math.toRadians(rotation));
    }

    public double getToY() {
        return yPosition + (length - 1) / 2 * Math.sin(Math.toRadians(rotation));
    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {

    }

    public void reset() {
        setPosition(initialPositionX, initialPositionY);
        setAngle(initialAngle);

        timer1 = 0.0;
        timer2 = time2;
    }
}
