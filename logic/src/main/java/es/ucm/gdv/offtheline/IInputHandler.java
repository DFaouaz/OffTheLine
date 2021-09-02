package es.ucm.gdv.offtheline;

import es.ucm.gdv.engine.IInput;

public interface IInputHandler {

    void handleEvent(IInput.TouchEvent event);
}
