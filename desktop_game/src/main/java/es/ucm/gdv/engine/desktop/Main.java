package es.ucm.gdv.engine.desktop;

import es.ucm.gdv.offtheline.Logic;


public class Main {
    static final int WIDTH = 640;
    static final int HEIGHT = 480;

    public static void main(String[] args){
        
        Engine engine = new Engine("OffTheLine", WIDTH, HEIGHT);
        Logic logic = new Logic(engine);
        engine.setLogic(logic);

        engine.run();
    }
}