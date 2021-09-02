package es.ucm.gdv.engine;

public interface ILogic {

    // Update
    void preUpdate(double deltaTime);
    void update(double deltaTime);
    void postUpdate(double deltaTime);

    // Render
    void render(IGraphics graphics);

    // HandleEvent
    void handleEvent(IInput.TouchEvent event);

    int getWidth();
    int getHeight();

    void resume();
    void pause();
}
