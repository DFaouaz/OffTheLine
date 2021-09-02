package es.ucm.gdv.engine;

import java.io.InputStream;

public interface IEngine {

    void run();
    void update(double deltaTime);
    void render();
    void handleEvents();

    IGraphics getGraphics();
    IAudio getAudio();
    IInput getInput();
    InputStream openInputStream(String filename);

    void setLogic(ILogic logic);
    void toogleFullscreen();
}