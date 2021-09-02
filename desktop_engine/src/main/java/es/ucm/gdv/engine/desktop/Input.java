package es.ucm.gdv.engine.desktop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.IInput;

public class Input implements IInput, MouseListener, MouseMotionListener, KeyListener {
    private List<TouchEvent> touchEvents;

    public Input() {
        touchEvents = new ArrayList<>();
    }

    // Devuelve eventos de input ocurridos desde la ultima llamada a este metodo
    @Override
    synchronized public List<TouchEvent> getTouchEvents() {
        List<TouchEvent> aux = touchEvents;
        touchEvents = new ArrayList<>();
        return aux;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        synchronized (this) {
            touchEvents.add(new TouchEvent(mouseEvent.getButton(), mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.PRESSED));
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        synchronized (this) {
            touchEvents.add(new TouchEvent(mouseEvent.getButton(), mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.RELEASED));
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // EMPTY
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        // EMPTY
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        // EMPTY
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //EMPTY
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        synchronized (this) {
            touchEvents.add(new TouchEvent(keyEvent.getKeyCode(), -1, -1, TouchEvent.TouchType.KEY_PRESSED));
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        synchronized (this) {
            touchEvents.add(new TouchEvent(keyEvent.getKeyCode(), -1, -1, TouchEvent.TouchType.KEY_RELEASED));
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        // EMPTY
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        synchronized (this) {
            touchEvents.add(new TouchEvent(mouseEvent.getID(), mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.MOVEMENT));
        }
    }
}
