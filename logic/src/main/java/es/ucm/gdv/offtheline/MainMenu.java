package es.ucm.gdv.offtheline;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.IAudio;
import es.ucm.gdv.engine.IFont;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class MainMenu extends GameObject {

    List<GameObject> elements = null;

    Text gameTitle = null;
    Text subTitle = null;
    Button easyModeButton = null;
    Text easyModeText = null;
    Text easyDescription = null;
    Button hardModeButton = null;
    Text hardModeText = null;
    Text hardDescription = null;

    IFont font = null;
    Player playerRef;
    int configured = 0;

    public MainMenu(IFont font, Player player) {
        this.font = font;
        playerRef = player;

        elements = new ArrayList<>();

        // Titulo
        gameTitle = new Text("OFF THE LINE", font, 0x0088FFFF);
        gameTitle.setPosition(10.0, 100.0);
        subTitle = new Text("A GAME COPIED TO BRYAN PERFETTO", font, 0x0088FFFF);
        subTitle.setPosition(10.0, 135.0);
        subTitle.setScale(0.5, 0.5);

        // Modos
        easyModeButton = new Button(240, 50) {
            @Override
            public void onPressed() {
                playerRef.init(250.0, 10);
                configured = 1;
            }

            @Override
            public void onReleased() { }
        };
        easyModeButton.setBorderColor(0x00000000);
        easyModeButton.setPosition(10.0, 310.0);

        easyModeText = new Text("EASY MODE", font, 0xFFFFFFFF);
        easyModeText.setPosition(10.0, 350.0);
        easyModeText.setScale(0.8, 0.8);

        hardModeButton = new Button(250, 50) {
            @Override
            public void onPressed() {
                playerRef.init(400.0, 5);
                configured = 2;
            }

            @Override
            public void onReleased() { }
        };
        hardModeButton.setBorderColor(0x00000000);
        hardModeButton.setPosition(10.0, 360.0);

        hardModeText = new Text("HARD MODE", font, 0xFFFFFFFF);
        hardModeText.setPosition(10.0, 400.0);
        hardModeText.setScale(0.8, 0.8);

        // Descripcion
        easyDescription = new Text("(SLOW SPEED, 10 LIVES)", font, 0x888888FF);
        easyDescription.setPosition(260.0, 350.0);
        easyDescription.setScale(0.4, 0.4);

        hardDescription = new Text("(FAST SPEED, 5 LIVES)", font, 0x888888FF);
        hardDescription.setPosition(270.0, 400.0);
        hardDescription.setScale(0.4, 0.4);


        // Add to elements list
        elements.add(gameTitle);
        elements.add(subTitle);
        elements.add(easyModeButton);
        elements.add(easyModeText);
        elements.add(hardModeButton);
        elements.add(hardModeText);
        elements.add(easyDescription);
        elements.add(hardDescription);
    }


    @Override
    public void render(IGraphics graphics) {
        for(GameObject gameObject : elements) {
            gameObject.preRender(graphics);
            gameObject.render(graphics);
            gameObject.postRender(graphics);
        }
    }

    @Override
    public void preUpdate(double deltaTime) {
        for(GameObject gameObject : elements) {
            gameObject.preUpdate(deltaTime);
        }
    }

    @Override
    public void update(double deltaTime) {
        for(GameObject gameObject : elements) {
            gameObject.update(deltaTime);
        }
    }

    @Override
    public void postUpdate(double deltaTime) {
        for(GameObject gameObject : elements) {
            gameObject.postUpdate(deltaTime);
        }
    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {
        for(GameObject gameObject : elements) {
            gameObject.handleEvent(event);
        }
    }

    @Override
    public void setAudio(IAudio audio){
        this.audio = audio;
        easyModeButton.setAudio(audio);
        hardModeButton.setAudio(audio);
    }

    // Devuelve 1 o 2 la primera vez que se llama dependiendo del modo
    public int isConfigured() {
        int aux = configured;
        configured = 0;
        return aux;
    }
}
