package es.ucm.gdv.offtheline;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.ucm.gdv.engine.IEngine;
import es.ucm.gdv.engine.IFont;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;
import es.ucm.gdv.engine.ILogic;

public class Logic implements ILogic {

    enum LogicStates {MAINMENU, GAME, GAMEOVER, WIN}

    LogicStates state = LogicStates.MAINMENU;

    IEngine engine;
    List<GameObject> gameObjects;
    List<Level> levels;
    Level currentLevel;
    Player player;

    MainMenu mainMenu;
    FinalMenu finalMenu;

    // Logic size
    static int WIDTH = -1;
    static int HEIGHT = -1;

    // Fuentes
    IFont menuFont;
    IFont levelFont;

    int level = -1; //Level -1 es el menu

    // timer para pasar de nivel
    float time = 0.0f;
    float nextLevelTimer = 2.0f;

    int mode = 0;

    public Logic(IEngine engine) {
        this.engine = engine;
        this.engine.setLogic(this);
        this.gameObjects = new ArrayList<>();
        this.levels = new ArrayList<>();
        Logic.WIDTH = 640;
        Logic.HEIGHT = 480;

        // ------------- RESOURCES LOAD ----------
        menuFont = engine.getGraphics().newFont("fonts/Bungee-Regular.ttf", 50, false);
        levelFont = engine.getGraphics().newFont("fonts/BungeeHairline-Regular.ttf", 20, false);

        engine.getAudio().loadSound("sounds/music.wav", "Music", true);

        engine.getAudio().loadSound("sounds/coin.wav", "Coin", false);
        engine.getAudio().loadSound("sounds/die.wav", "Die", false);
        engine.getAudio().loadSound("sounds/jump.wav", "Jump", false);
        engine.getAudio().loadSound("sounds/start.wav", "Start", false);
        engine.getAudio().loadSound("sounds/click.wav", "Click", false);
        engine.getAudio().loadSound("sounds/win.wav", "Win", false);
        engine.getAudio().loadSound("sounds/gameOver.wav", "GameOver", false);


        // ------------- GAME --------------
        // Player is needed to be created after any screen
        this.player = new Player(250, 1500, 10);

        player.setAudio(engine.getAudio());

        // MAIN MENU
        mainMenu = new MainMenu(menuFont, player);
        mainMenu.setAudio(engine.getAudio());
        gameObjects.add(mainMenu);

        // FINAL MENU
        finalMenu = new FinalMenu(menuFont);

        // PLAY MUSIC
        engine.getAudio().play("Music");


        // -------- READ LEVEL -------------
        loadAndCreateLevels();
    }

    @Override
    public void preUpdate(double deltaTime) {
        for (IUpdatable updatable : gameObjects)
            updatable.preUpdate(deltaTime);
    }

    @Override
    public void update(double deltaTime) {
        for (IUpdatable updatable : gameObjects)
            updatable.update(deltaTime);

        switch (state) {
            case MAINMENU:
                mainMenuState();
                break;

            case GAME:
                gameState(deltaTime);
                break;

            default:
                break;
        }
    }

    // Update del menu (configura el jugador cuando se pulsa un boton)
    void mainMenuState() {
        mode = mainMenu.isConfigured();
        if (mode > 0) {
            gameObjects.remove(mainMenu);
            level = 0; // Nivel inicial
            currentLevel = levels.get(level);
            currentLevel.setPlayer(player);
            currentLevel.reset();
            gameObjects.add(currentLevel);
            finalMenu.setMode(mode);

            state = LogicStates.GAME;
        }
    }

    // Update de los niveles (cambia de nivel o al menu principal)
    void gameState(double deltaTime) {
        if (currentLevel.isCompleted()) {
            if (time < nextLevelTimer)
                time += deltaTime;
            else {
                level++;
                // Termina los niveles -> pasa al WIN
                if (level >= levels.size()) {
                    gameObjects.add(finalMenu);
                    engine.getAudio().play("Win");
                    finalMenu.setWin(true);
                    state = LogicStates.WIN;
                    return;
                }
                gameObjects.remove(currentLevel);
                currentLevel = levels.get(level);
                currentLevel.setPlayer(player);
                currentLevel.reset();
                gameObjects.add(currentLevel);

                time = 0;
            }
            // Se queda sin vidas -> pasa al GAMEOVER
        } else if (player.lives == 0) {
            //gameObjects.remove(player);
            gameObjects.add(finalMenu);
            finalMenu.setLevel(level);
            finalMenu.setWin(false);
            engine.getAudio().play("GameOver");
            state = LogicStates.GAMEOVER;
        }
        else if(time != 0)
            time = 0;
    }

    @Override
    public void postUpdate(double deltaTime) {
        for (IUpdatable updatable : gameObjects)
            updatable.postUpdate(deltaTime);
    }

    @Override
    public void render(IGraphics graphics) {
        for (IRenderable renderable : gameObjects) {
            renderable.preRender(graphics);
            renderable.render(graphics);
            renderable.postRender(graphics);
        }
    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {
        if (event.type == IInput.TouchEvent.TouchType.KEY_PRESSED && event.id == IInput.KeyCode.F)
            engine.toogleFullscreen();

        // Si hace click cuando esta en game over o en win, vuelve al menu principal
        if ((state == LogicStates.WIN || state == LogicStates.GAMEOVER) && event.type == IInput.TouchEvent.TouchType.PRESSED) {

            gameObjects.remove(finalMenu);

            gameObjects.add(mainMenu);
            state = LogicStates.MAINMENU;
            gameObjects.remove(currentLevel);
            currentLevel = null;

            return;
        }

        for (IInputHandler inputHandler : gameObjects)
            inputHandler.handleEvent(event);
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void pause() {
        engine.getAudio().pause("Music");
    }

    @Override
    public void resume() {
        engine.getAudio().resume("Music");
    }

    // Carga los niveles
    private void loadAndCreateLevels() {
        try (InputStreamReader reader = new InputStreamReader(engine.openInputStream("levels.json"))) {
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray levelsData = (JSONArray) obj;

            int numLevel = 1;

            // Iteramos por los niveles codificados
            for (Object levelData : levelsData) {
                // Creamos la lista de GameObject del nivel
                Level level = new Level(levelFont);
                levels.add(level);
                level.setPosition(WIDTH / 2.0, HEIGHT / 2.0);

                JSONObject levelObj = (JSONObject) levelData;

                // Cogemos el nombre del nivel
                String name = (String) levelObj.get("name");
                level.setName(name);
                level.setNumLevel(numLevel);

                // Cogemos los caminos del nivel
                int indexPath = 0;
                JSONArray pathsData = (JSONArray) levelObj.get("paths");
                if (pathsData == null) continue;
                for (Object pathData : pathsData) {
                    JSONArray vertex = (JSONArray) ((JSONObject) pathData).get("vertices");
                    JSONArray directions = (JSONArray) ((JSONObject) pathData).get("directions");

                    if (vertex == null) continue;
                    int numVertex = vertex.size();
                    for (int i = 0; i < numVertex; i++) {
                        // Extremos de la linea
                        double fromX = ((Number) Objects.requireNonNull(((JSONObject) vertex.get(i)).get("x"))).doubleValue();
                        double fromY = ((Number) Objects.requireNonNull(((JSONObject) vertex.get(i)).get("y"))).doubleValue();
                        double toX = ((Number) Objects.requireNonNull(((JSONObject) vertex.get((i + 1) % numVertex)).get("x"))).doubleValue(); // Modulo para que el ultimo vaya con el primero
                        double toY = ((Number) Objects.requireNonNull(((JSONObject) vertex.get((i + 1) % numVertex)).get("y"))).doubleValue();

                        // Direccion si se especifica o es por defecto (especificado : por defecto)
                        double magnitude = Math.sqrt(Math.pow(toX - fromX, 2.0) + Math.pow(toY - fromY, 2.0));
                        double dirX = (toX - fromX) / magnitude;
                        double dirY = (toY - fromY) / magnitude;
                        double jumpDirX = directions != null ? ((Number) Objects.requireNonNull(((JSONObject) directions.get(i)).get("x"))).doubleValue() : dirY;
                        double jumpDirY = directions != null ? ((Number) Objects.requireNonNull(((JSONObject) directions.get(i)).get("y"))).doubleValue() : -dirX;

                        level.addLineToPath(fromX, fromY, toX, toY, jumpDirX, jumpDirY, dirX, dirY, indexPath);
                    }
                    indexPath++;
                }

                // Iteramos por las monedas si existen
                JSONArray items = (JSONArray) levelObj.get("items");
                if (items != null) {
                    for (Object item : items) {
                        Object optional;
                        double x = ((Number) Objects.requireNonNull(((JSONObject) item).get("x"))).doubleValue();
                        double y = ((Number) Objects.requireNonNull(((JSONObject) item).get("y"))).doubleValue();
                        double radius = (((optional = ((JSONObject) item).get("radius"))) == null) ? 0.0 : ((Number) optional).doubleValue();
                        double speed = (((optional = ((JSONObject) item).get("speed"))) == null) ? 0.0 : ((Number) optional).doubleValue();
                        double angle = (((optional = ((JSONObject) item).get("angle"))) == null) ? 0.0 : ((Number) optional).doubleValue();

                        level.addCoin(x, y, radius, speed, angle, engine.getAudio());
                    }
                }

                // Iteramos por los enemigos si existen
                JSONArray enemies = (JSONArray) levelObj.get("enemies");
                if (enemies != null) {
                    for (Object enemy : enemies) {
                        Object optional;
                        double x = ((Number) Objects.requireNonNull(((JSONObject) enemy).get("x"))).doubleValue();
                        double y = ((Number) Objects.requireNonNull(((JSONObject) enemy).get("y"))).doubleValue();
                        double length = ((Number) Objects.requireNonNull(((JSONObject) enemy).get("length"))).doubleValue();
                        double angle = ((Number) Objects.requireNonNull(((JSONObject) enemy).get("angle"))).doubleValue();
                        //Optional
                        double speed = (((optional = ((JSONObject) enemy).get("speed"))) == null) ? 0.0 : ((Number) optional).doubleValue();
                        double offsetX = (optional = ((JSONObject) enemy).get("offset")) == null ? 0.0 : ((Number) Objects.requireNonNull(((JSONObject) optional).get("x"))).doubleValue();
                        double offsetY = (optional = ((JSONObject) enemy).get("offset")) == null ? 0.0 : ((Number) Objects.requireNonNull(((JSONObject) optional).get("y"))).doubleValue();
                        double time1 = (((optional = ((JSONObject) enemy).get("time1"))) == null) ? 0.0 : ((Number) optional).doubleValue();
                        double time2 = (((optional = ((JSONObject) enemy).get("time2"))) == null) ? 0.0 : ((Number) optional).doubleValue();

                        level.addEnemy(x, y, length, angle, speed, offsetX, offsetY, time1, time2);
                    }
                }

                numLevel++;
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

}