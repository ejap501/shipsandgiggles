package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.HUDmanager;
import net.shipsandgiggles.pirate.conf.Configuration;

/**
 * Death Screen
 * Displays the death screen
 * Displays upon player death
 * Adds the ui for the death screen
 *
 * @author Team 23
 * @version 1.0
 */
public class DeathScreen {
    // Main data store
    public Stage stage;
    private Viewport viewport;
    public float score = 0;
    public float Gold = 0;

    // Labelling
    public Label scoreLabel;
    public Label gold;
    public Label gameOver;

    /**
     * Constructs the death screen ui
     *
     * @param batch : The batch of spite data
     */
    public DeathScreen(SpriteBatch batch){
        // Construction and setting of the labels
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);
        Table endGameTable = new Table();
        gameOver = new Label( "Game Over!", Configuration.SKIN, "title");
        scoreLabel = new Label( "Score : " + score, Configuration.SKIN, "big");
        gold = new Label( "Final Gold Count : " + Gold, Configuration.SKIN, "big");

        // Formatting of labels
        gameOver.setFontScale(1.5f);
        scoreLabel.setFontScale(0.75f);
        gold.setFontScale(0.75f);
        endGameTable.add(gameOver);
        endGameTable.row();
        endGameTable.add(scoreLabel);
        endGameTable.row();
        endGameTable.add(gold);

        endGameTable.setPosition(Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight()/2 + 100);

        stage.addActor(endGameTable);
    }

    /**
     * Updates the variables and check for which victory achived
     *
     * @param hud : The hud display
     * @param victorykind : Type of victory achieved
     */
    public void update(HUDmanager hud, int victorykind){
        if(victorykind == 1){
            gameOver.setText("Pacifist Victory!");
        }
        if(victorykind == 2){
            gameOver.setText("Domination Victory!");
        }
        if(victorykind == 3){
            gameOver.setText("Combination Victory!");
        }
        score = hud.score;
        Gold = hud.gold;
        scoreLabel.setText("Score: " + score);
        gold.setText("Final Gold Count: " + Gold);
    }
}
