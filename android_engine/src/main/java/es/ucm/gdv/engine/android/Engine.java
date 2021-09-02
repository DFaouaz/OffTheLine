package es.ucm.gdv.engine.android;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import es.ucm.gdv.engine.IAudio;
import es.ucm.gdv.engine.IEngine;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;
import es.ucm.gdv.engine.ILogic;

public class Engine implements IEngine, Runnable {

    private Graphics graphics = null;
    private Input input = null;
    private ILogic logic = null;
    private Audio audio = null;

    private SurfaceHolder surfaceHolder = null;
    private AssetManager assetManager = null;
    private Canvas canvas = null;

    private int backgroundColor = 0x000000FF;
    volatile private boolean exit = false;
    private Thread thread = null;

    public Engine(SurfaceView surfaceView, AssetManager assetManager) {
        graphics = new Graphics(surfaceView, assetManager);
        input = new Input();
        audio = new Audio(surfaceView.getContext(), assetManager);

        this.surfaceHolder = surfaceView.getHolder();
        this.assetManager = assetManager;

        surfaceView.setOnTouchListener(input);
    }

    // Inicia el bucle principal en otro hilo
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    // Reanuda el engine y la logica
    public void resume() {
        if(!exit) return;

        exit = false;
        this.start();
        logic.resume();
    }

    // Pausa el engine y la logica
    public void pause() {
        if(exit) return;

        exit = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logic.pause();
    }

    // Metodo redefinido de Runnable
    // Bucle principal
    @Override
    public void run() {
        long lastFrameTime = System.nanoTime();
        while(!exit) {
            // Calculamos deltaTime
            long currentTime = System.nanoTime();
            long deltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Update
            update((double)deltaTime  / 1.0E9);
            // Render
            render();
            // HandleEvent
            handleEvents();
        }
    }

    // Actualiza la logica con un deltaTime dado
    @Override
    public void update(double deltaTime) {
        if(logic == null) return;
        logic.preUpdate(deltaTime);
        logic.update(deltaTime);
        logic.postUpdate(deltaTime);
    }

    // Renderiza la logica
    @Override
    public void render() {
        if(logic == null) return;

        if(surfaceHolder == null) return;

        while(!surfaceHolder.getSurface().isValid())
            ;

        canvas = surfaceHolder.lockCanvas();
        if(canvas == null) return;
        graphics.setCanvas(canvas);

        graphics.clear(backgroundColor);
        graphics.recalculateLogicSize(logic.getWidth(), logic.getHeight());

        // Render
        if(logic != null)
            logic.render(graphics);

        graphics.renderBlackStrips(logic.getWidth(), logic.getHeight());

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    // Envia las interaciones con el movil a la logica
    // Coordenadas de pantalla transformadas a las de logica
    @Override
    public void handleEvents() {
        List<IInput.TouchEvent> touchEvents = input.getTouchEvents();

        for(IInput.TouchEvent event : touchEvents) {
            List<Double> position = graphics.fromCanvasToLogic(event.x, event. y, logic.getWidth(), logic.getHeight());
            event.x = (int) position.get(0).doubleValue();
            event.y = (int) position.get(1).doubleValue();
        }

        for(IInput.TouchEvent event : touchEvents)
            logic.handleEvent(event);

    }

    @Override
    public IGraphics getGraphics() {
        return graphics;
    }

    @Override
    public IAudio getAudio() {
        return audio;
    }

    @Override
    public IInput getInput() {
        return input;
    }

    // Abre un InputStream
    // IMPORTANTE: NO LO CIERRA (WARNING)
    @Override
    public InputStream openInputStream(String filename) {
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(filename);
            return inputStream;
        } catch (IOException e) {
            System.out.println("Error al abrir archivo " + filename);
        }
        return null;
    }

    @Override
    public void setLogic(ILogic logic) {
        this.logic = logic;
    }

    @Override
    public void toogleFullscreen() {
        // ANDROID NO TIENE FULLSCREEN
    }
}