package es.ucm.gdv.offtheline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.ucm.gdv.engine.IGraphics;
import es.ucm.gdv.engine.IInput;

public class Path extends GameObject {

    HashMap<Integer, ArrayList<Line>> paths;
    HashMap<Line, Integer> lineToComponent;
    List<Line> segments;

    public Path() {
        paths = new HashMap<>();
        lineToComponent = new HashMap<>();
        segments = new ArrayList<Line>();

    }

    // Añade linea al camino
    public void addLineToPath(Line line, int indexPath) {
        if (!paths.containsKey(indexPath))
            paths.put(indexPath, new ArrayList<Line>());

        paths.get(indexPath).add(line);
        lineToComponent.put(line, indexPath);
        segments.add(line);
    }

    // Devuelve el indice de la componente conexa
    public int getPathIndex(Line line) {
        if (lineToComponent.containsKey(line))
            return lineToComponent.get(line);

        return -1;
    }

    //Devuelde el indice de la linea dentro de su componente conexa
    public int getIndexInPath(Line line) {
        int i = getPathIndex(line);
        if (i == -1) return -1;

        return paths.get(i).indexOf(line);
    }

    //Devuelve la cantidad de segmentos en la componente conexa
    public int numLinesInPath(int indexPath) {
        if (!paths.containsKey(indexPath))
            return -1;
        return paths.get(indexPath).size();
    }

    // Devuelve siguiente linea a la line dada
    public Line getNextLineInPath(Line line) {
        int i = getPathIndex(line);
        if (i == -1)
            return null;

        List<Line> path = paths.get(i);
        int lineIndex = getIndexInPath(line);
        return path.get((lineIndex + 1) % path.size());
    }

    // Devuelve la anterior linea a la line dada
    public Line getPreviousLine(Line line) {
        int i = getPathIndex(line);
        if (i == -1)
            return null;

        List<Line> path = paths.get(i);
        int lineIndex = getIndexInPath(line);

        return path.get((lineIndex - 1 + path.size()) % path.size());
    }


    @Override
    public void render(IGraphics graphics) {
        // Render del grafo de segmentos
        for (Line line : segments) {
            line.preRender(graphics);
            line.render(graphics);
            line.postRender(graphics);
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
