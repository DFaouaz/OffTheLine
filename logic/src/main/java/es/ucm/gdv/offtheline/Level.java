package es.ucm.gdv.offtheline;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.IAudio;
import es.ucm.gdv.engine.IEngine;
import es.ucm.gdv.engine.IFont;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Level extends GameObject {
    private Player player = null;
    private String name = "_level_name_";
    private Path path = null;
    private List<Coin> coins = null;
    private List<Enemy> enemies = null;

    private IFont font = null;
    private int numLevel = 0;

    double zoomScale = 1.25f;
    double incScale = 2f;
    double scale = zoomScale;

    LevelHUD levelHUD = null;

    // True cuando el nivel ha sido completado
    private boolean completed;

    public Level(IFont font) {
        path = new Path();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();

        this.font = font;
        levelHUD = new LevelHUD();
        levelHUD.setFont(font);
    }

    public List<Line> getPath() {
        return path.segments;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.setPath(path);
        player.setCoins(coins);
        player.setEnemies(enemies);
        levelHUD.setPlayer(player);
    }

    public void setName(String name) {
        this.name = name;
        levelHUD.setLevelName(name);
    }

    // Añade una linea al camino indicado por pathIndex con las propiedades dadas
    public void addLineToPath(double fromX, double fromY, double toX, double toY, double jumpDirX, double jumpDirY, double dirX, double dirY, int pathIndex) {
        Line line = new Line();
        line.setPoints(fromX, fromY, toX, toY);
        line.setJumpDirection(jumpDirX, jumpDirY);
        line.setDirection(dirX, dirY);

        addLineToPath(line, pathIndex);
    }

    // Añade una moneda con las propiedades dadas
    public void addCoin(double xPosition, double yPosition, double radius, double speed, double angle, IAudio audio) {
        Coin coin = new Coin();
        coin.setInitialPosition(xPosition, yPosition);
        coin.setPosition(xPosition, yPosition);
        coin.setRadius(radius);
        coin.setSpeed(speed);
        coin.setInitialAngle(angle);
        coin.setAudio(audio);

        addCoin(coin);
    }

    // Añade un enemigo con las propiedades dadas
    public void addEnemy(double xPosition, double yPosition, double length, double angle, double speed, double offsetX, double offsetY, double time1, double time2) {
        Enemy enemy = new Enemy();

        enemy.setPosition(xPosition, yPosition);
        enemy.setInitialPosition(xPosition, yPosition);
        enemy.setInitialAngle(angle);
        enemy.setLength(length);
        enemy.setAngle(angle);
        enemy.setSpeed(speed);
        enemy.setOffset(offsetX, offsetY);
        enemy.setTime1(time1);
        enemy.setTime2(time2);

        addEnemy(enemy);
    }

    public void addLineToPath(Line line, int pathIndex) {
        path.addLineToPath(line, pathIndex);
    }

    public void addCoin(Coin coin) {
        coins.add(coin);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void setNumLevel(int numLevel) {
        this.numLevel = numLevel;
        levelHUD.setNumLevel(numLevel);
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.scale(scale, scale);
        graphics.scale(1.0, -1.0); // flip vertically

        double shakeX = 0.0;
        double shakeY = 0.0;

        // Shake effect
        if (player.hasReachedLine()) {
            shakeX = Math.random() * 4 - 2.0f;
            shakeY = Math.random() * 4 - 2.0f;
            graphics.translate(-shakeX, -shakeY);
        }

        // Path
        path.render(graphics);

        // Enemies
        for (Enemy enemy : enemies) {
            enemy.preRender(graphics);
            enemy.render(graphics);
            enemy.postRender(graphics);
        }

        // Coin
        for (Coin coin : coins) {
            coin.preRender(graphics);
            coin.render(graphics);
            coin.postRender(graphics);
        }

        // Player
        if (player != null) {
            player.preRender(graphics);
            player.render(graphics);
            player.postRender(graphics);
        }

        // El orden importa
        if (player.hasReachedLine()) {
            graphics.translate(shakeX, shakeY);
        }
        graphics.scale(1.0, -1.0);
        graphics.scale(1 / scale, 1 / scale);

        // HUD
        if (levelHUD != null) {
            levelHUD.preRender(graphics);
            levelHUD.render(graphics);
            levelHUD.postRender(graphics);
        }
    }

    @Override
    public void preUpdate(double deltaTime) {
        // HUD

        // Path
        path.preUpdate(deltaTime);

        // Enemies
        for (Enemy enemy : enemies)
            enemy.preUpdate(deltaTime);

        // Coin
        for (Coin coin : coins)
            coin.preUpdate(deltaTime);

        // Player
        if (player != null)
            player.preUpdate(deltaTime);
    }

    @Override
    public void update(double deltaTime) {
        // Initial zoom
        if (scale > 1.0f)
            scale -= incScale * deltaTime;
        else if(scale < 1.0f) scale = 1.0f;

        // Path
        path.update(deltaTime);

        if (player.isDead() && player.getLives() > 0) {
            reset();
        }

        // Si no quedan monedas en el nivel actual,
        // se empieza el timer para pasar al siguiente nivel
        if (!player.isDying && getNonTakenCoinsCount() == 0) {
            completed = true;
        } else completed = false;

        // Enemies
        for (Enemy enemy : enemies)
            enemy.update(deltaTime);

        // Coin
        for (Coin coin : coins)
            coin.update(deltaTime);

        // Player
        if (player != null)
            player.update(deltaTime);
    }

    @Override
    public void postUpdate(double deltaTime) {
        // HUD

        // Path
        path.postUpdate(deltaTime);

        // Enemies
        for (Enemy enemy : enemies)
            enemy.postUpdate(deltaTime);

        // Coin
        for (Coin coin : coins)
            coin.postUpdate(deltaTime);

        // Player
        if (player != null)
            player.postUpdate(deltaTime);
    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {
        // HUD

        // Path
        path.handleEvent(event);

        // Enemies
        for (Enemy enemy : enemies)
            enemy.handleEvent(event);

        // Coin
        for (Coin coin : coins)
            coin.handleEvent(event);

        // Player
        if (player != null)
            player.handleEvent(event);
    }

    // Devuelve el numero de monedas sin agarrar
    private int getNonTakenCoinsCount() {
        int count = 0;
        for (Coin coin : coins)
            count = coin.isCollected() ? count : count + 1;
        return count;
    }

    // Devuelve true si el nivel esta completo
    public boolean isCompleted() {
        return completed;
    }

    // Resetea el nivel y los elementos que contiene
    public void reset() {
        completed = false;

        scale = zoomScale;

        player.reset();

        for (Coin coin : coins)
            coin.reset();

        for (Enemy enemy : enemies)
            enemy.reset();
    }
}
