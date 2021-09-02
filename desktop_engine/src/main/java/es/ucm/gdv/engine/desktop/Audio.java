package es.ucm.gdv.engine.desktop;

import java.awt.image.BufferStrategy;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import es.ucm.gdv.engine.IAudio;
import es.ucm.gdv.engine.IEngine;

public class Audio implements IAudio {

    IEngine engine = null;
    private Map<String, Clip> sounds = null;
    private Map<String, Boolean> soundsLoop = null;
    private Map<String, AudioInputStream> audioInputs = null;

    public Audio(IEngine engine) {
        this.engine = engine;
        sounds = new HashMap<>();
        soundsLoop = new HashMap<>();
        audioInputs = new HashMap<>();
    }

    // Carga el sonido y lo mete en el mapa de sonidos
    @Override
    public void loadSound(String filename, String name, boolean loop) {

        try {
            BufferedInputStream is = new BufferedInputStream(engine.openInputStream(filename));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
            Clip clip = null;
            clip = AudioSystem.getClip();
            clip.open(audioIn);

            soundsLoop.put(name, loop);
            sounds.put(name, clip);
            audioInputs.put(name, audioIn);

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Error cargando el sonido: " + e);
        }
    }

    // Reproduce desde el inicio el sonido dado
    @Override
    public void play(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) {
            System.out.println("Sound " + name + " can't found");
            return;
        }

        clip.setMicrosecondPosition(0);
        clip.start();

        if(soundsLoop.get(name))
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Reanuda el sonido desde la posicion actual
    @Override
    public void resume(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) {
            System.out.println("Sound " + name + " can't found");
            return;
        }
        clip.start();
        if(soundsLoop.get(name))
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Pausa el sonido en la posicion actual
    @Override
    public void pause(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) {
            System.out.println("Sound " + name + "can't found");
            return;
        }
        clip.stop();
    }

    // Detiene el sonido y resetea la posicion al inicio
    @Override
    public void stop(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) {
            System.out.println("Sound " + name + "can't found");
            return;
        }
        clip.stop();
        clip.setMicrosecondPosition(0);
    }

}
