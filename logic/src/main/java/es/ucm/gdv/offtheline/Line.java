package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Line extends GameObject {

    private double fromX;
    private double fromY;
    private double toX;
    private double toY;

    //Jump direction
    private double jumpDirX;
    private double jumpDirY;

    private double dirX;
    private double dirY;

    public double getFromX() {return fromX;}
    public double getFromY() {return fromY;}

    public double getToX() {return toX;}
    public double getToY() {return toY;}

    public double getJumpDirX(){ return jumpDirX;}
    public double getJumpDirY(){ return jumpDirY;}

    public double getDirX(){ return dirX;}
    public double getDirY(){ return dirY;}

    // Establece extremos de la linea
    public void setPoints(double fromX, double fromY, double toX, double toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    // Set jump direction
    public void setJumpDirection(double x, double y) {
        jumpDirX = x;
        jumpDirY = y;
    }

    // Establece direccion de recorrido
    public void setDirection(double x, double y) {
        dirX = x;
        dirY = y;
    }

    public double getDistanceToA(double x, double y)
    {
        return Math.sqrt(Math.pow((x - fromX), 2.0) + Math.pow((y - fromY), 2.0));
    }

    public double getDistanceToB(double x, double y)
    {
        return Math.sqrt(Math.pow((x - toX), 2.0) + Math.pow((y - toY), 2.0));
    }

    public double getMagnitude() {
        return getDistanceToA(toX, toY);
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setColor(0xFFFFFFFF);
        graphics.setStrokeWidth(2.0f);
        graphics.drawLine((int)fromX, (int)fromY, (int)toX, (int)toY);
        //graphics.setColor(0xFFFFFFFF); // Reset del color
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

    }
}
