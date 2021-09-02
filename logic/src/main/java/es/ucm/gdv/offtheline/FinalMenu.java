package es.ucm.gdv.offtheline;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.IFont;
import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class FinalMenu extends GameObject {

    IFont font;
    boolean win = false;

    // Win menu
    Text completeText;
    Text winText;
    List<Text> winTexts;

    double timer = 0.1;
    boolean blue = false;

    // GameOver menu
    Text scoreText;
    Text modeText;
    List<Text> gameOverTexts;

    public FinalMenu(IFont font) {
        this.font = font;

        // Create win texts
        winTexts = new ArrayList<>();

        winText = new Text("CONGRATULATIONS!", font);
        winText.setPosition(50, 120);
        winTexts.add(winText);

        completeText = new Text("EASY MODE COMPLETE", font);
        completeText.setScale(0.6, 0.6);
        completeText.setPosition(160, 170);
        winTexts.add(completeText);

        Text clickText = new Text("CLICK TO QUIT TO MAIN MENU", font);
        clickText.setScale(0.4, 0.4);
        clickText.setPosition(170, 200);
        winTexts.add(clickText);

        // Create gameOver texts
        gameOverTexts = new ArrayList<>();

        Text gameOverText = new Text("GAME OVER", font, 0xFF0000FF);
        gameOverText.setPosition(170, 120);
        gameOverTexts.add(gameOverText);

        modeText = new Text("EASY MODE", font);
        modeText.setScale(0.5, 0.5);
        modeText.setPosition(240, 170);
        gameOverTexts.add(modeText);

        scoreText = new Text("SCORE: " + 0, font);
        scoreText.setScale(0.5, 0.5);
        scoreText.setPosition(250, 200);
        gameOverTexts.add(scoreText);
    }

    @Override
    public void render(IGraphics graphics) {
        if (font == null) return;

        // Fondo
        graphics.setColor(0x333333FF);
        graphics.fillRect(0, 60, Logic.WIDTH, 220);

        // Titulo
        if (win) {
            winText.setTextColor(blue ? 0x0000FFFF : 0xFFFF00FF);

            for (Text t : winTexts) {
                t.preRender(graphics);
                t.render(graphics);
                t.postRender(graphics);
            }
        } else {
            for (Text t : gameOverTexts) {
                t.preRender(graphics);
                t.render(graphics);
                t.postRender(graphics);
            }
        }
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {
        timer -= deltaTime;

        if (timer < 0.0) {
            timer = 0.1;
            blue = !blue;
        }
    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {
    }

    public void setMode(int mode) {
        completeText.setText((mode == 1 ? "EASY" : "HARD") + " MODE COMPLETE");
        modeText.setText((mode == 1 ? "EASY" : "HARD") + " MODE");
    }

    public void setLevel(int level) {
        scoreText.setText("SCORE: " + (level + 1));
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
