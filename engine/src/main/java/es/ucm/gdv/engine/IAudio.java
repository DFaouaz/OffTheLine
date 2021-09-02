package es.ucm.gdv.engine;

public interface IAudio {

    void loadSound(String filename, String name, boolean loop);

    // Reproduce desde el inicio
    void play(String name);
    // Reanuda
    void resume(String name);
    // Pausa
    void pause(String name);
    // Detiene
    void stop(String name);
}
