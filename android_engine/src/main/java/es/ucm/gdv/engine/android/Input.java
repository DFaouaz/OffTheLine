package es.ucm.gdv.engine.android;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import es.ucm.gdv.engine.IInput;

public class Input implements IInput, View.OnTouchListener {

    private List<TouchEvent> touchEvents;

    public Input() {
        touchEvents = new ArrayList<>();
    }

    // Devuelve los eventos desde la ultima vez que se llamo
    @Override
    synchronized public List<TouchEvent> getTouchEvents() {
        List<TouchEvent> aux = touchEvents;
        touchEvents = new ArrayList<>();
        return aux;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        TouchEvent touchEvent = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchEvent = new TouchEvent(event.getActionIndex(), (int)event.getX(), (int)event.getY(), TouchEvent.TouchType.PRESSED);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = new TouchEvent(event.getActionIndex(), (int)event.getX(), (int)event.getY(), TouchEvent.TouchType.RELEASED);
                break;

            case MotionEvent.ACTION_MOVE:
                touchEvent = new TouchEvent(event.getActionIndex(), (int) event.getX(), (int) event.getY(), TouchEvent.TouchType.MOVEMENT);
                break;
            default: break;
        }

        synchronized (this) {
            if(touchEvent != null)
                touchEvents.add(touchEvent);
        }

        return true;
    }
}