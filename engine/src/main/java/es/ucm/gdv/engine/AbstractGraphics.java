package es.ucm.gdv.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphics implements IGraphics {

    // Recalcula y escala/translada el canvas al centro de la pantalla (y ajustado)
    @Override
    public void recalculateLogicSize(double logicSizeX, double logicSizeY) {
        double windowWidth = getWidth();
        double windowHeight = getHeight();

        double ratioWidth = windowWidth / logicSizeX;
        double ratioHeight = windowHeight / logicSizeY;

        if (ratioWidth >= ratioHeight) // bandas negras verticales
        {
            translate((windowWidth - logicSizeX * ratioHeight) / 2.0, 0.0);
            scale(ratioHeight, ratioHeight);
        }
        else // bandas negras horizontales
        {
            translate(.0, (windowHeight - logicSizeY * ratioWidth) / 2.0);
            scale(ratioWidth, ratioWidth);
        }

        //save();
    }

    // Transforma las coordenadas de pantalla en coordenadas de logica
    // xRef/yRef entran siendo posiciones de canvas y salen siendo de logica
    @Override
    public List<Double> fromCanvasToLogic(double x, double y, double logicSizeX, double logicSizeY) {
        double windowWidth = getWidth();
        double windowHeight = getHeight();

        double ratioWidth = windowWidth / logicSizeX;
        double ratioHeight = windowHeight / logicSizeY;

        double offsetX = 0.0;
        double offsetY = 0.0;

        if(ratioWidth >= ratioHeight) // bandas negras verticales
            offsetX = (windowWidth - logicSizeX * ratioHeight) / 2.0;
        else // bandas negras horizontales
            offsetY = (windowHeight - logicSizeY * ratioWidth) / 2.0;

        x -= offsetX;
        x /=  ((windowWidth - 2 * offsetX) / logicSizeX);

        y -= offsetY;
        y /= ((windowHeight - 2 * offsetY) / logicSizeY);

        List<Double> result = new ArrayList<>();
        result.add(x);
        result.add(y);
        return result;
    }

    // Renderiza las bandas negras
    public void renderBlackStrips(double logicSizeX, double logicSizeY) {
        float windowWidth = getWidth();
        float windowHeight = getHeight();

        float ratioWidth = (float)(windowWidth / logicSizeX);
        float ratioHeight = (float)(windowHeight / logicSizeY);

        float offsetX = (float)(windowWidth - logicSizeX * ratioHeight) / 2.0f;
        float offsetY = (float)(windowHeight - logicSizeY * ratioWidth) / 2.0f;

        // A coordenadas de canvas
        offsetX /= ratioHeight;
        offsetY /= ratioWidth;

        setColor(0x000000FF);
        if (ratioWidth >= ratioHeight) // bandas negras verticales
        {
            fillRect(-offsetX, 0.0f, 0.0f, (float)logicSizeY);
            fillRect((float)logicSizeX, 0.0f, (float)logicSizeX + offsetX, (float)logicSizeY);
        }
        else // bandas negras horizontales
        {
            fillRect(0.0f, -offsetY, (float)logicSizeX, 0.0f);
            fillRect(0.0f, (float)logicSizeY, (float)logicSizeX, (float)logicSizeY + offsetY);
        }

    }

}
