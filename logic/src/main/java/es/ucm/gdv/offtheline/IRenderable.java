package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IGraphics;

public interface IRenderable {

    void preRender(IGraphics graphics);
    void render(IGraphics graphics);
    void postRender(IGraphics graphics);

}
