package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import net.shipsandgiggles.pirate.pref.GamePreferences;

/**
 * Sound Controller
 * Controls all the sound effects and music
 *
 * @author Team 23
 * @version 1.0
 */
public class SoundController{
        // Main data store
        public Music music;
        public Sound buttonPress;
        public Sound Explosion;
        public Sound cannonShot;
        public Music seaNoises;
        public float volume = 0;
        public float musicVolume = 0;

        /** Construction of the controller */
        public SoundController(){
                // Returns if already set
                if(music != null){if(music.isPlaying())return;}
                if(seaNoises!= null) if(seaNoises.isPlaying())return;

                // Setting the music and sound effects
                music = Gdx.audio.newMusic(Gdx.files.internal("models/music.mp3"));
                seaNoises = Gdx.audio.newMusic(Gdx.files.internal("models/seaSounds.mp3"));
                cannonShot = Gdx.audio.newSound(Gdx.files.internal("models/cannonShot.mp3"));
                buttonPress = Gdx.audio.newSound(Gdx.files.internal("models/buttonPress.mp3"));
                Explosion = Gdx.audio.newSound(Gdx.files.internal("models/explosion.mp3"));

                // Checks according to preferences and updates based on them
                if(GamePreferences.get().isMusicEnabled() && !music.isPlaying()){
                        music.setVolume(GamePreferences.get().getVolumeLevel());
                        music.setLooping(true);
                }
                if(GamePreferences.get().isVolumeEnabled() && !seaNoises.isPlaying()){
                        seaNoises.setVolume(GamePreferences.get().getVolumeLevel());
                        seaNoises.setLooping(true);
                }
        }

        /** Updates controls for all preferences */
        public void update(){
                if(!GamePreferences.get().isMusicEnabled()) music.stop();
                if(!GamePreferences.get().isVolumeEnabled()) seaNoises.stop();
                if(GamePreferences.get().isVolumeEnabled() && !music.isPlaying()){
                        music.play();
                }
                if(GamePreferences.get().isVolumeEnabled() && !seaNoises.isPlaying()){
                        seaNoises.play();
                }
                if(musicVolume != music.getVolume()) {
                        music.setVolume(GamePreferences.get().getVolumeLevel());
                        return;
                }
                if(volume != seaNoises.getVolume()) {
                        seaNoises.setVolume(GamePreferences.get().getVolumeLevel());
                        return;
                }
        }

        /** Play explosion noise*/
        public void playExplosion(){
                if(!GamePreferences.get().isVolumeEnabled()) return;
                long id = Explosion.play(GamePreferences.get().getVolumeLevel());
                Explosion.setPitch(id, 2);
                Explosion.setLooping(id,false);
        }

        /** Play button press noise*/
        public void playButtonPress(){
                if(!GamePreferences.get().isVolumeEnabled()) return;
                long id = buttonPress.play(GamePreferences.get().getVolumeLevel());
                buttonPress.setPitch(id, 2);
                buttonPress.setLooping(id,false);
        }

        /** Play cannon shot noise*/
        public void playCannonShot(){
                if(!GamePreferences.get().isVolumeEnabled()) return;
                long id = cannonShot.play(GamePreferences.get().getVolumeLevel());
                cannonShot.setPitch(id, 2);
                cannonShot.setLooping(id,false);
        }

        /** Pauses all noises */
        public void pauseAll() {
                music.pause();
                seaNoises.pause();
                music.dispose();
                seaNoises.dispose();
        }
}
