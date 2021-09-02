package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IAudio;
import es.ucm.gdv.engine.IGraphics;

public abstract class GameObject implements IRenderable, IUpdatable, IInputHandler {

    // Transform
    protected double xPosition = 0.0f;
    protected double yPosition = 0.0f;
    protected double xScale = 1.0f;
    protected double yScale = 1.0f;
    protected double rotation = 0.0f;

    IAudio audio = null;

    public void setAudio(IAudio audio){
        this.audio = audio;
    }

    final public void setPosition(double x, double y) {
        xPosition = x;
        yPosition = y;
    }

    public double getPositionX() {
        return xPosition;
    }

    public double getPositionY() {
        return yPosition;
    }

    final public void setScale(double x, double y) {
        xScale = x;
        yScale = y;
    }

    public double getScaleX() {
        return xScale;
    }

    public double getScaleY() {
        return yScale;
    }

    final public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    final public void setAngle(double angle) {
        rotation = angle;
    }

    public double getRotation() {
        return rotation;
    }

    final public void translate(double x, double y) {
        xPosition += x;
        yPosition += y;
    }

    final public void scale(double x, double y) {
        xScale *= x;
        yScale *= y;
    }

    final public void scale(double s) {
        scale(s, s);
    }

    final public void rotate(double angle) {
        rotation += angle;
    }

    // Transformaciones iniciales comunesa todos los gameObjects
    @Override
    final public void preRender(IGraphics graphics) {
        graphics.save();
        // Llevar al origen
        graphics.translate(xPosition, yPosition);
        // Escalar
        graphics.scale(xScale, yScale);
        // Rotar
        graphics.rotate(rotation);
    }

    @Override
    public abstract void render(IGraphics graphics);

    // Restore de las transformaciones
    @Override
    final public void postRender(IGraphics graphics) {
        graphics.restore();
    }

    @Override
    public abstract void update(double deltaTime);
}
