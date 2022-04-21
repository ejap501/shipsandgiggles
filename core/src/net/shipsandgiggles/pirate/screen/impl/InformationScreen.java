package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.task.ChangeScreenTask;

/**
 * Information Screen
 * Displays the information screen
 * Displays upon game start
 * Adds the ui for the information screen
 *
 * @author Team 23
 * @version 1.0
 */
public class InformationScreen implements Screen {
    // Main data store
    private Stage stage;
    private Table table;
    private Timer.Task task;
    private final SpriteBatch batch = new SpriteBatch();
    public final Sprite background = new Sprite(new Texture(Gdx.files.internal("models/background.PNG")));

    /** Displays the information screen */
    @Override
    public void show() {
        // Construct table
        this.table = new Table();
        this.table.setFillParent(true);
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);
        this.stage.addActor(this.table);

        // Creating all the labels
        Label informationLabel = new Label("INFORMATION!", Configuration.SKIN, "title");
        informationLabel.setAlignment(Align.center);

        Label keysInformation = new Label("Use your arrow keys or WASD to move around.", Configuration.SKIN, "big");
        keysInformation.setAlignment(Align.center);

        Label shootingInformation = new Label("There are two methods of shooting, burst and singular.", Configuration.SKIN, "big");
        shootingInformation.setAlignment(Align.center);

        Label shootingInformationTwo = new Label("Click where you want to shoot!", Configuration.SKIN, "big");
        shootingInformationTwo.setAlignment(Align.center);

        Label burstShoot = new Label("Right-Click to burst shoot!", Configuration.SKIN, "big");
        burstShoot.setAlignment(Align.center);

        Label singularShoot = new Label("Left-Click to singular shoot!", Configuration.SKIN, "big");
        singularShoot.setAlignment(Align.center);

        Label collegeInfo1 = new Label("There are two ways to win the game!", Configuration.SKIN, "big");
        Label collegeInfo2 = new Label("taking down a college will give you 5 gold and 3 score a second and if you capture all of them you win!", Configuration.SKIN, "big");
        Label collegeInfo3 = new Label("the other way is to destroy all of them which will give you an instant 500 gold and 250 score!", Configuration.SKIN, "big");
        Label collegeInfo4 = new Label("destroying all of them will result in a victory!", Configuration.SKIN, "big");

        Label spaceToSkip = new Label("Press the space bar to skip the information!", Configuration.SKIN);
        spaceToSkip.setAlignment(Align.center);

        // Creates a uniform X/Y table.
        this.table.add(informationLabel);
        this.table.row();
        this.table.add(Configuration.SPACER_LABEL);
        this.table.row();
        this.table.add(keysInformation);
        this.table.row();
        this.table.add(Configuration.SPACER_LABEL);
        this.table.row();
        this.table.add(shootingInformation);
        this.table.row();
        this.table.add(shootingInformationTwo);
        this.table.row();
        this.table.add(Configuration.SPACER_LABEL);
        this.table.row();
        this.table.add(burstShoot);
        this.table.row();
        this.table.add(singularShoot);
        this.table.row();
        this.table.add(Configuration.SPACER_LABEL);
        this.table.row();
        this.table.add(Configuration.SPACER_LABEL);
        this.table.row();
        this.table.row();
        this.table.add(collegeInfo1);
        this.table.row();
        this.table.add(collegeInfo2);
        this.table.row();
        this.table.add(collegeInfo3);
        this.table.row();
        this.table.add(collegeInfo4);
        this.table.row();
        this.table.row();
        this.table.add(spaceToSkip);

        this.task = Timer.schedule(new ChangeScreenTask(ScreenType.GAME), 40);
    }

    /**
     * Renders the information screen to the world
     *
     * @param deltaTime : Delta time (elapsed time since last game tick)
     */
    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(.98f, .91f, .761f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        batch.end();

        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        this.stage.draw();

        takeInput();
    }

    /**
     * Resizes the information screen to fit the viewport
     *
     * @param width : Width of the screen
     * @param height : Height of the screen
     */
    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    /** Hiding the screen after display */
    @Override
    public void hide() {
        this.stage.getRoot().getColor().a = 0;
        this.stage.getRoot().addAction(Actions.fadeOut(1));
    }

    /** Disposing of the screen data */
    @Override
    public void dispose() {
        this.stage.dispose();
    }

    /** User input to proceed to next step (termination of the information screen) */
    public void takeInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.task.run();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}