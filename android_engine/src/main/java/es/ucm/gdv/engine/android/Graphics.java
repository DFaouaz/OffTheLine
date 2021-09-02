package es.ucm.gdv.engine.android;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import es.ucm.gdv.engine.AbstractGraphics;
import es.ucm.gdv.engine.IFont;


public class Graphics extends AbstractGraphics {

    private Canvas canvas = null;
    private Paint paint;
    private SurfaceView surfaceView;
    private AssetManager assetManager;

    public Graphics(SurfaceView surfaceView, AssetManager assetManager) {

        paint = new Paint();
        paint.setColor(0xFFFFFFFF); // Blanco por defecto
        paint.setStrokeWidth(1.0f); // Ancho de 1 en las lineas por defecto
        paint.setStrokeCap(Paint.Cap.SQUARE);  // Dibujar lineas con esquinas (no redondeadas)

        this.surfaceView = surfaceView;
        this.assetManager = assetManager;
    }

    // Carga una fuente y la devuelve
    @Override
    public IFont newFont(String filename, int size, boolean isBold) {
        return (IFont) new Font(assetManager, filename, size, isBold);
    }

    // Pinta el canvas al completo de un color
    @Override
    public void clear(int color) {
        if(canvas == null) return;
        canvas.drawARGB(color & 0xFF, color >> 24 & 0xFF, color >> 16 & 0xFF, color >> 8 & 0xFF);
    }

    // Translada el canvas
    @Override
    public void translate(double x, double y) {
        if(canvas == null) return;
        canvas.translate((float)x, (float)y);
    }

    // Escala el canvas
    @Override
    public void scale(double x, double y) {
        if(canvas == null) return;
        canvas.scale((float)x, (float)y);
    }

    // Rota el canvas
    @Override
    public void rotate(double angle) {
        if(canvas == null) return;
        canvas.rotate((float)angle);
    }

    // Guarda el estado actual de la pila de transformaciones del canvas
    @Override
    public void save() {
        if(canvas == null) return;
        canvas.save();
    }

    // Restaura el estado de la matriz de transformacions del canvas que tenia
    // la ultima vez que se llamo al save()
    @Override
    public void restore() {
        if(canvas == null) return;
        canvas.restore();
    }

    // Establece el color en el que se va a colorear
    @Override
    public void setColor(int color) {
        if(paint == null) return;
        paint.setARGB(color & 0xFF, color >> 24 & 0xFF, color >> 16 & 0xFF, color >> 8 & 0xFF);
    }

    // Establece la fuente en la que se va a escribir (dibujar texto)
    @Override
    public void setFont(IFont font) {
        if(font == null) return;
        Font aFont = (Font)font;
        if(aFont == null) return;

        if(paint == null) return;
        paint.setTypeface(aFont.getFont());
        paint.setTextSize(aFont.getSize());
    }

    // Dibuja una linea desde (x1,y1) a (x2, y2)
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    // Dibuja un rectangulo desde (x1,y1) superior izquierda a (x2, y2) inferior derecha
    @Override
    public void drawRect(int x1, int y1, int x2, int y2) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    // Dibuja un rectangulo relleno desde (x1,y1) superior izquierda a (x2, y2) inferior derecha
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    @Override
    public void drawRect(float x1, float y1, float x2, float y2) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    // Establece el grueso de las lineas
    @Override
    public void setStrokeWidth(float width) {
        if(paint == null) return;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
    }

    // Escribe text en (x, y)
    @Override
    public void drawText(String text, int x, int y) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void drawText(String text, float x, float y) {
        if(canvas == null || paint == null) return;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(text, x, y, paint);
    }

    // Devuelve el ancho de la ventana
    @Override
    public int getWidth() {
        if(surfaceView == null) return -1;
        return surfaceView.getWidth();
    }

    // Devuelve el alto de la ventana
    @Override
    public int getHeight() {
        if(surfaceView == null) return -1;
        return surfaceView.getHeight();
    }

    // Should be called before logic render
    void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
