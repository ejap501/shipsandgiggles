package net.shipsandgiggles.pirate.task;

import com.badlogic.gdx.utils.Timer;

import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.screen.ScreenType;

/**
 * Change Screen Task
 * Changes the screen
 *
 * @author Team 23
 * */
public class ChangeScreenTask extends Timer.Task {
    // Screen data
    private final ScreenType screenType;

    /**
     * Sets new screen
     *
     * @param screenType : Screen to be set
     */
    public ChangeScreenTask(ScreenType screenType) {
        this.screenType = screenType;
    }

    /** Fetches the screen and applies it */
    @Override
    public void run() {
        PirateGame.get().changeScreen(this.screenType);
    }
}