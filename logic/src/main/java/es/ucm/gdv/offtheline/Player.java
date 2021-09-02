package es.ucm.gdv.offtheline;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Player extends GameObject {

    int side = 12; // Tamaño en pixeles del lado
    double speed = 0;
    double jumpSpeed = 1500.0;
    double directionX = 0;
    double directionY = 0;
    double rotationVelocity = 180.0; // En grados
    boolean jumping = false;

    int maxLives = 10;
    int lives = maxLives;

    private int coinThreshold = 20;

    Path path = null;
    Line currentLine = null;

    List<Coin> coins = null;
    List<Enemy> enemies = null;

    boolean dead = false;
    boolean isDying = false;
    boolean inverse = false;

    float timer = 0.0f;
    float timeToDie = 1.0f;

    int numParticles = 10;

    List<Particle> particles = null;
    private boolean reachedLine = false;
    float timerReachedLine = 0.0f;
    float reachedLineTime = 0.1f;

    Player(double speed, double jumpSpeed, int lives) {
        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.rotationVelocity = 180.0;
        this.maxLives = lives;
        this.lives = maxLives;

        particles = new ArrayList<>();

        // Create particles
        for (int i = 0; i < numParticles; i++) {
            Particle p = new Particle();
            p.reset();
            particles.add(p);
        }
    }

    public void init(double walkSpeed, int lives) {
        this.speed = walkSpeed;
        this.jumpSpeed = 1500.0;
        this.lives = lives;
        this.maxLives = lives;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public void setPath(Path path) {
        this.path = path;
        setCurrentLine(path.segments.get(0));
    }

    // Establece al Player en linea cada
    private void setCurrentLine(Line currentLine) {
        this.currentLine = currentLine;
        double xPos = !inverse ? currentLine.getFromX() : currentLine.getToX();
        double yPos = !inverse ? currentLine.getFromY() : currentLine.getToY();
        setPosition(xPos, yPos);

        double xDir = currentLine.getDirX();
        double yDir = currentLine.getDirY();
        if (inverse) {
            xDir *= -1;
            yDir *= -1;
        }

        setDirection(xDir, yDir);
    }

    // Manda saltar al jugador
    private void jump() {
        if(jumping)
            return;

        directionX = currentLine.getJumpDirX();
        directionY = currentLine.getJumpDirY();
        jumping = true;

        audio.play("Jump");
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setColor(0x0088FFFF);
        graphics.setStrokeWidth(2.0f);
        if (isDying) {
            for (Particle p :
                    particles) {
                p.preRender(graphics);
                p.render(graphics);
                p.postRender(graphics);
            }
        } else {
            graphics.drawRect(-side / 2, -side / 2, side / 2, side / 2);
        }
    }

    @Override
    public void preUpdate(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {
        // Si ha llegado a la linea
        if (hasReachedLine()) {
            timerReachedLine += deltaTime;
            if (timerReachedLine >= reachedLineTime) {
                reachedLine = false;
                timerReachedLine = 0.0f;
            }
        }

        // Fuera de los limites
        if (isOutOfBounds() && !isDying)
            die();

        // Si esta muriendo (hace cosas de particulas)
        if (isDying) {
            for (Particle p :
                    particles) {
                p.preUpdate(deltaTime);
                p.update(deltaTime);
                p.postUpdate(deltaTime);
            }
            timer += deltaTime;

            if (timer > timeToDie) {
                dead = true;
                if (lives - 1 >= 0)
                    lives--;
            }
            return;
        }

        // Direccion es la de la linea o la de salto
        double currentSpeed = (jumping ? jumpSpeed : speed);
        double xDelta = directionX * currentSpeed * deltaTime;
        double yDelta = directionY * currentSpeed * deltaTime;

        // Enemy collision
        if (checkCollisionWithEnemies(xDelta, yDelta)) {
            die();
            return;
        }

        // Coin collision
        checkCollisionWithCoins(xDelta, yDelta);

        boolean translateEnable = true;
        // Path collision
        if (jumping)
            translateEnable = !checkPathCollision(xDelta, yDelta);
        else
            translateEnable = !checkNextLine(xDelta, yDelta);


        if (translateEnable)
            translate(xDelta, yDelta);
        rotate(rotationVelocity * deltaTime);
    }

    @Override
    public void postUpdate(double deltaTime) {

    }

    @Override
    public void handleEvent(IInput.TouchEvent event) {
        if (event.type == IInput.TouchEvent.TouchType.PRESSED)
            jump();
        else if (event.type == IInput.TouchEvent.TouchType.KEY_PRESSED && event.id == IInput.KeyCode.SPACE)
            jump();
    }

    // Comprueba su colision con las lineas del camino y devuelve todas con las que colisiona (distinto de current)
    private List<Pair<Line, Utils.Point>> checkCollisionWithLines(double deltaX, double deltaY) {
        List<Pair<Line, Utils.Point>> collisionedLines = new ArrayList<>();
        Utils.Point intersectionPoint = null;
        Utils.Segment playerLine = new Utils.Segment(xPosition, yPosition, xPosition + deltaX, yPosition + deltaY);
        for (Line line : path.segments) {
            if (line == currentLine || (line.getFromX() == currentLine.getToX() && line.getToX() == currentLine.getFromX() &&
                    (line.getFromY() == currentLine.getToY() && line.getToY() == currentLine.getFromY())))
                continue;

            intersectionPoint = Utils.segmentsIntersection(new Utils.Segment(line.getFromX(), line.getFromY(), line.getToX(), line.getToY()), playerLine);
            if (intersectionPoint != null) {
                collisionedLines.add(new Pair<Line, Utils.Point>(line, new Utils.Point(intersectionPoint.getX(), intersectionPoint.getY())));
                intersectionPoint = null;
            }
        }
        return collisionedLines;
    }

    private void checkCollisionWithCoins(double xDelta, double yDelta) {
        if (jumping) {
            for (Coin coin : coins) {
                if (coin.isCollected())
                    continue;

                Utils.Segment playerLine = new Utils.Segment(xPosition, yPosition, xPosition + xDelta, yPosition + yDelta);
                Utils.Point coinPos = new Utils.Point(coin.getPositionX(), coin.getPositionY());
                double distance = Math.sqrt(Utils.sqrDistancePointSegment(playerLine, coinPos));
                if (distance < coinThreshold)
                    coin.collect();
            }
        }
    }

    private boolean checkCollisionWithEnemies(double xDelta, double yDelta) {
        for (Enemy enemy : enemies) {
            Utils.Segment playerLine = new Utils.Segment(xPosition, yPosition, xPosition + xDelta, yPosition + yDelta);
            Utils.Segment enemyLine = new Utils.Segment(enemy.getFromX(), enemy.getFromY(), enemy.getToX(), enemy.getToY());
            Utils.Point intersection = Utils.segmentsIntersection(playerLine, enemyLine);
            if (intersection != null) {
                setPosition(intersection.getX(), intersection.getY());
                return true;
            }
        }
        return false;
    }

    // Devuelve true si se ha colisionado con una linea
    private boolean checkPathCollision(double xDelta, double yDelta) {
        List<Pair<Line, Utils.Point>> collided = checkCollisionWithLines(xDelta, yDelta);

        if (collided.size() == 0) return false;

        double xDir = currentLine.getDirX();
        double yDir = currentLine.getDirY();

        // Si estaba recorriendo la antigua linea de forma invertida
        if (inverse) {
            xDir *= -1;
            yDir *= -1;
        }

        Pair<Line, Utils.Point> next = null;
        int i = 0;
        while (next == null && i < collided.size()) {
            next = collided.get(i);
            // Que no intertecte con la siguiente (por si salta en el punto inicial/final de algun segmento)
            if ((!inverse && next.first == path.getPreviousLine(currentLine)) ||
                    (inverse && next.first == path.getNextLineInPath(currentLine)))
                next = null;

            i++;
        }

        if (next == null)
            return false;

        double xLineDir = next.first.getDirX();
        double yLineDir = next.first.getDirY();

        double magNormal = Math.sqrt(Math.pow(xDir + xLineDir, 2.0) + Math.pow(yDir + yLineDir, 2.0));
        double magInverse = Math.sqrt(Math.pow(xDir - xLineDir, 2.0) + Math.pow(yDir - yLineDir, 2.0));

        double maxMag = Math.max(magNormal, magInverse);

        inverse = (maxMag == magInverse);

        xDir = (maxMag == magNormal ? xLineDir : -xLineDir);
        yDir = (maxMag == magNormal ? yLineDir : -yLineDir);

        setCurrentLine(next.first);
        setPosition(next.second.getX(), next.second.getY());
        setDirection(xDir, yDir);
        jumping = false;
        reachedLine = true;
        return true;
    }

    // Devuelve true si se ha cambido de linea
    private boolean checkNextLine(double xDelta, double yDelta) {
        // Si ha llegado al final de la linea
        double diff = 0.0;
        if (inverse)
            diff = currentLine.getDistanceToB(xPosition + xDelta, yPosition + yDelta) - currentLine.getMagnitude();
        else
            diff = currentLine.getDistanceToA(xPosition + xDelta, yPosition + yDelta) - currentLine.getMagnitude();

        if (diff >= 0.0) {
            Line next = null;
            if (inverse)
                next = path.getPreviousLine(currentLine);
            else
                next = path.getNextLineInPath(currentLine);

            setCurrentLine(next);
            return true;
        }
        return false;
    }

    public boolean isOutOfBounds()
    {
        return xPosition < -Logic.WIDTH || yPosition < -Logic.HEIGHT || xPosition > Logic.WIDTH || yPosition > Logic.HEIGHT;
    }

    public void reset() {
        // Inicia la marcha desde el inicio
        inverse = false;
        setCurrentLine(path.segments.get(0));
        jumping = false;
        isDying = false;
        dead = false;
        timer = 0.0f;
        audio.play("Start");

        for (Particle p :
                particles) {
            p.reset();
        }
    }

    public void die() {
        isDying = true;
        audio.play("Die");
    }

    public boolean isDead() {
        return dead;
    }

    public int getLives() {
        return lives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public boolean hasReachedLine() {
        return reachedLine;
    }
}
