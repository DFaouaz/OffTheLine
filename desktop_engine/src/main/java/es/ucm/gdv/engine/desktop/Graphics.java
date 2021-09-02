package es.ucm.gdv.engine.desktop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;

import es.ucm.gdv.engine.AbstractGraphics;
import es.ucm.gdv.engine.IFont;

public class Graphics extends AbstractGraphics {

    JFrame window = null;
    GraphicsDevice device = null;
    java.awt.Graphics graphics = null;
    BufferStrategy bufferStrategy = null;

    class TransfromType {
        public static final int TRANSLATE = 0;
        public static final int SCALE = 1;
        public static final int ROTATE = 2;
    }

    Stack<Stack<List<Double>>> transformStack; //List[0] = TransfromType, List[1:] = valores de tranformacion
    Stack<List<Double>> lastTransfromStack = null;

    int width, height;

    public Graphics(String s, final int width, final int height) {
        this.width = width;
        this.height = height;

        window = new JFrame(s);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width,height);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
        //window.setUndecorated(true);

        window.setIgnoreRepaint(true);
        window.setVisible(true);

        int i = 100;
        while(i-- > 0 && window.getBufferStrategy() == null){
            window.createBufferStrategy(2);
        }

        bufferStrategy = window.getBufferStrategy();
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

        transformStack = new Stack<>();
        save(); // Primer save()
    }

    // Carga una nueva fuente y la devuelve
    @Override
    public IFont newFont(String filename, int size, boolean isBold) {
        return new Font(filename, size, isBold);
    }

    // Pinta el buffer entero en el color dado
    @Override
    public void clear(int color) {
        if(graphics == null) return;
        setColor(color);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    // Translada el canvas y mete en la pila una transformacion
    @Override
    public void translate(double x, double y) {
        if(graphics == null) return;
        Graphics2D graphics2D = (Graphics2D)graphics;
        if(graphics2D == null) return;

        graphics2D.translate(x, y);

        List<Double> translateInfo = new ArrayList<>();
        translateInfo.add((double)TransfromType.TRANSLATE);
        translateInfo.add(x);
        translateInfo.add(y);
        lastTransfromStack.add(translateInfo);
    }

    // Escala el canvas y mete en la pila una transformacion
    @Override
    public void scale(double x, double y) {
        if(graphics == null) return;
        Graphics2D graphics2D = (Graphics2D)graphics;
        if(graphics2D == null) return;

        graphics2D.scale(x, y);

        List<Double> scaleInfo = new ArrayList<>();
        scaleInfo.add((double)TransfromType.SCALE);
        scaleInfo.add(x);
        scaleInfo.add(y);
        lastTransfromStack.add(scaleInfo);
    }

    // Rota el canvas y mete en la pila una transformacion
    @Override
    public void rotate(double angle) {
        if(graphics == null) return;
        Graphics2D graphics2D = (Graphics2D)graphics;
        if(graphics2D == null) return;

        graphics2D.rotate(Math.toRadians(angle));

        List<Double> rotateInfo = new ArrayList<>();
        rotateInfo.add((double)TransfromType.ROTATE);
        rotateInfo.add(angle);
        lastTransfromStack.add(rotateInfo);
    }

    /*
     * Guarda el estado actual de la matriz de transformacion para que se restaure
     * en la proxima llamada al restore()
     */
    @Override
    public void save() {
        transformStack.add(new Stack<List<Double>>());
        lastTransfromStack = transformStack.get(transformStack.size() - 1);
    }

    /*
     * Restaura el valor por defecto o el previamente guardado con save()
     */
    @Override
    public void restore() {
        if(graphics == null) return;
        Graphics2D graphics2D = (Graphics2D)graphics;
        if(graphics2D == null) return;

        if(transformStack.size() <= 0) {
            System.out.println("No puedes vaciar la pila de transformaciones, demasiadas llamadas a restore()");
            return;
        }

        Stack<List<Double>> stack = transformStack.size() == 1 ? transformStack.peek() : transformStack.pop();

        while(!stack.isEmpty()) {
            List<Double> callInfo = stack.pop();
            int type = (int)callInfo.get(0).doubleValue();
            if(type == TransfromType.TRANSLATE) {
                graphics2D.translate(-callInfo.get(1), -callInfo.get(2));
            }
            else if(type == TransfromType.SCALE) {
                graphics2D.scale(1.0 / callInfo.get(1), 1.0 / callInfo.get(2));
            }
            else if(type == TransfromType.ROTATE){
                graphics2D.rotate(Math.toRadians(-callInfo.get(1)));
            }
        }
        lastTransfromStack = transformStack.get(transformStack.size() - 1);
    }

    // Establece el color de pintado
    @Override
    public void setColor(int color) {
        if (graphics == null) return;
        graphics.setColor(new Color(color >> 24 & 0xFF, color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF));
    }

    // Establece la fuente de escritura
    @Override
    public void setFont(IFont font) {
        if (graphics == null) return;
        graphics.setFont(((Font)font).getFont());
    }

    // Dibuja una linea de (x1, y1) a (x2, y2)
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if(graphics == null) return;
        graphics.drawLine(x1, y1, x2, y2);
    }

    // Dibuja un rectangulo de (x1, y1) a (x2, y2)
    @Override
    public void drawRect(int x1, int y1, int x2, int y2) {
        if(graphics == null) return;
        graphics.drawRect(x1, y1, x2 - x1, y2 - y1);
    }

    // Dibuja una rectangulo relleno de (x1, y1) a (x2, y2)
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        if(graphics == null) return;
        graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        if(graphics == null) return;
        graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }

    @Override
    public void drawRect(float x1, float y1, float x2, float y2) {
        if(graphics == null) return;
        graphics.drawRect((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1));
    }

    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {
        if(graphics == null) return;
        graphics.fillRect((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1));
    }

    // Establece ancho de pintado de linea
    @Override
    public void setStrokeWidth(float width) {
        if(graphics == null) return;
        Graphics2D graphics2D = (Graphics2D)graphics;
        if(graphics2D == null) return;

        graphics2D.setStroke(new BasicStroke(width));
    }

    // Dibuja un texto en (x, y)
    @Override
    public void drawText(String text, int x, int y) {
        if(graphics == null) return;
        graphics.drawString(text, x, y);
    }

    @Override
    public void drawText(String text, float x, float y) {
        if(graphics == null) return;
        graphics.drawString(text, (int)x, (int)y);
    }

    // Devuelve el ancho de la ventana
    @Override
    public int getWidth() {
        if (window == null) return -1;
        return window.getWidth();
    }

    // Devuelve el alto de la ventana
    @Override
    public int getHeight() {
        if (window == null) return -1;
        return window.getHeight();
    }

    public boolean windowActive() {
        return window != null;
    }

    // Cierra ventana
    public void dispose() {
        window.setVisible(false);
        window.dispose();
    }

    BufferStrategy getBufferStrategy() {
        return bufferStrategy;
    }

    void setCanvas(java.awt.Graphics graphics) {
        this.graphics = graphics;
    }
}
