package es.ucm.gdv.engine.desktop;

import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import es.ucm.gdv.engine.IAudio;
import es.ucm.gdv.engine.IEngine;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;
import es.ucm.gdv.engine.ILogic;

public class Engine implements IEngine {

    private java.awt.Graphics canvas = null;
    private BufferStrategy bufferStrategy = null;

    private Graphics graphics = null;
    private Input input = null;
    private ILogic logic = null;
    private IAudio audio = null;

    private int backgroundColor = 0x000000FF; //Negro por defecto

    public Engine(String name, int width, int height) {
        graphics = new Graphics(name, width, height);
        input = new Input();
        audio = new Audio(this);

        graphics.window.addMouseListener(input);
        graphics.window.addMouseMotionListener(input);
        graphics.window.addKeyListener(input);

        bufferStrategy = graphics.getBufferStrategy();
    }

    // Bucle principal
    public void run() {
        long lastFrameTime = System.nanoTime();

        while(graphics.windowActive()) {
            // Calculamos deltaTime
            long currentTime = System.nanoTime();
            long deltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Update
            update((double)deltaTime  / 1.0E9);
            // Render
            render();
            // HandleEvents
            handleEvents();
        }

        graphics.dispose();
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
        if(bufferStrategy == null) return;

        do {
            do {
                canvas = bufferStrategy.getDrawGraphics();
                graphics.setCanvas(canvas);

                graphics.clear(backgroundColor);
                graphics.recalculateLogicSize(logic.getWidth(), logic.getHeight());

                // Render
                if(logic != null)
                    logic.render(graphics);

                graphics.renderBlackStrips(logic.getWidth(), logic.getHeight());

                canvas.dispose();
            } while (bufferStrategy.contentsRestored());
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
    }

    // Envia a la logica los eventos de input
    // Se transforman las coordenadas de pantalla a coordenadas de logica
    @Override
    public void handleEvents() {
        List<IInput.TouchEvent> inputEvents = input.getTouchEvents();

        for(IInput.TouchEvent event : inputEvents) {
            List<Double> position = graphics.fromCanvasToLogic(event.x, event. y, logic.getWidth(), logic.getHeight());
            event.x = (int) position.get(0).doubleValue();
            event.y = (int) position.get(1).doubleValue();
        }

        for(IInput.TouchEvent event : inputEvents)
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

    // Abre un InputStream pero no lo cierra
    @Override
    public InputStream openInputStream(String filename) {
        try
        {
            InputStream inputStream = new FileInputStream(filename);
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

    // Toggle del Fullscreen
    @Override
    public void toogleFullscreen() {
        Window window = graphics.device.getFullScreenWindow();
        graphics.device.setFullScreenWindow(window == null ? graphics.window : null);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

}