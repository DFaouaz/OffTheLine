package es.ucm.gdv.engine.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import es.ucm.gdv.engine.IAudio;

public class Audio implements IAudio {

    private Context context;
    private AssetManager assetManager;

    static Map<String, MediaPlayer> mediaPlayers = null;

    AssetFileDescriptor afd = null;
    FileDescriptor fd = null;

    public Audio(Context context, AssetManager assetManager) {
        this.context = context;
        this.assetManager = assetManager;

        mediaPlayers = new HashMap<>();
    }

    // Carga el sonido y lo mete en el mapa de sonidos
    @Override
    public void loadSound(String filename, final String name, boolean loop) {

        final MediaPlayer mediaPlayer = getMediaPlayer(context);
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            afd = assetManager.openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fd = afd.getFileDescriptor();

        try {
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.setLooping(loop);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            mediaPlayers.put(name, mediaPlayer);
        }
    }

    // Crea un sonido vacio
    static MediaPlayer getMediaPlayer(Context context){

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
            Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );

            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            }
            catch (IllegalAccessException e) {return mediaplayer;}
            finally {
                f.setAccessible(false);
            }

            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
            //Log.e("", "subtitle is setted :p");
        } catch (Exception e) {}

        return mediaplayer;
    }

    // Reproduce desde el principio el sonido
    @Override
    public void play(String name) {
        MediaPlayer sound = null;
        synchronized (this) {
            sound = mediaPlayers.get(name);
        }
        if(sound == null)
            return;

        if (sound.isPlaying()) {
            sound.stop();
            try {
                sound.prepare();
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                return;
            }
        }
        sound.start();
    }

    // Reanuda el sonido desde la posicion en la que estaba parado
    @Override
    public void resume(String name) {
        MediaPlayer sound = null;
        synchronized (this) {
            sound = mediaPlayers.get(name);
        }
        if(sound == null)
            return;

        sound.start();
    }

    // Pausa sonido en la posicion actual
    @Override
    public void pause(String name) {
        MediaPlayer sound = null;
        synchronized (this) {
            sound = mediaPlayers.get(name);
        }
        if(sound == null)
            return;

        sound.pause();
    }

    // Detiene y reinicia la posicion del sonido
    @Override
    public void stop(String name) {
        MediaPlayer sound = null;
        synchronized (this) {
            sound = mediaPlayers.get(name);
        }
        if(sound == null)
            return;

        sound.stop();
    }

    // Devuelve true si el sonido name se esta reproduciendo
    public boolean isPlaying(String name) {
        MediaPlayer sound = null;
        synchronized (this) {
            sound = mediaPlayers.get(name);
        }
        if(sound == null)
            return false;

        return sound.isPlaying();
    }
}
