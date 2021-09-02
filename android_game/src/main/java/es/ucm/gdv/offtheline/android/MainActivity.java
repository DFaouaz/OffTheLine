package es.ucm.gdv.offtheline.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import es.ucm.gdv.engine.android.Engine;
import es.ucm.gdv.offtheline.Logic;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView = null;
    Engine engine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        engine = new Engine(surfaceView, this.getAssets());
        engine.setLogic(new Logic(engine));
        engine.start();
    }

    protected void onResume() {
        super.onResume();
        engine.resume();
    }

    protected void onPause() {
        super.onPause();
        engine.pause();
    }
}
