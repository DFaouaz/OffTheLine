package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IFont;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class LevelHUD extends GameObject {

    private IFont font = null;
    private String levelName = null;
    private int numLevel = -1;
    private Player player = null;

    public LevelHUD() {

    }

    public void setFont(IFont font) {
        this.font = font;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setNumLevel(int numLevel) {
        this.numLevel = numLevel;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void render(IGraphics graphics) {
        // Level name
        graphics.setColor(0xFFFFFFFF);
        graphics.setFont(font);
        graphics.drawText("LEVEL " + numLevel + " - " + levelName, -640 / 2 + 30, -480 / 2 + 50);

        // Lives
        int nLives = player.getLives();
        int maxLives = player.getMaxLives();

        int livesPosX = 100;
        int livesPosY = -480 / 2 + 38;
        int gap = 20;

        graphics.setColor(0x0088FFFF);
        for (int i = 0; i < nLives; i++) {
            graphics.drawRect(livesPosX + gap * i, livesPosY, livesPosX + gap * i + 10, livesPosY + 10);
        }
        graphics.setColor(0xFF0000FF);
        for (int i = nLives; i < maxLives; i++) {
            graphics.drawLine(livesPosX + gap * i, livesPosY, livesPosX + gap * i + 10, livesPosY + 10);
            graphics.drawLine(livesPosX + gap * i, livesPosY + 10, livesPosX + gap * i + 10, livesPosY);
        }
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {

    }
}
