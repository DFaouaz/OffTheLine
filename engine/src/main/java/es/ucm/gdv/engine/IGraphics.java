package es.ucm.gdv.engine;


import java.util.List;

public interface IGraphics {

    IFont newFont(String filename, int size, boolean isBold);

    void clear(int color);

    void translate(double x, double y);
    void scale(double x, double y);
    void rotate(double angle);
    void save();
    void restore();

    void setColor(int color);
    void setFont(IFont font);

    void drawLine(int x1, int y1, int x2, int y2);
    void drawRect(int x1, int y1, int x2, int y2);
    void fillRect(int x1, int y1, int x2, int y2);

    void drawLine(float x1, float y1, float x2, float y2);
    void drawRect(float x1, float y1, float x2, float y2);
    void fillRect(float x1, float y1, float x2, float y2);

    void setStrokeWidth(float width);

    void drawText(String text, int x, int y);
    void drawText(String text, float x, float y);

    int getWidth();
    int getHeight();

    void recalculateLogicSize(double logicSizeX, double logicSizeY);
    List<Double> fromCanvasToLogic(double x, double y, double logicSizeX, double logicSizeY);
}
