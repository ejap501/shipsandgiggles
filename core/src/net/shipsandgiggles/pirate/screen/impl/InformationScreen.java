package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
 * @author Team 22 : Edward Poulter
 * @version 2.0
 */
public class InformationScreen implements Screen {
    // Main data store
    public static Stage stage;
    public static Table table;
    public static Timer.Task task;
    private final SpriteBatch batch = new SpriteBatch();
    public final Sprite background = new Sprite(new Texture(Gdx.files.internal("models/background.PNG")));

    /** Displays the information screen */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        createParts();
        stage.addActor(table);
    }

    /**
     * Renders the information screen to the world
     *
     * @param deltaTime : Delta time (elapsed time since last game tick)
     */
    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(165f / 255f, 220f / 255f, 236f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.begin();
        //background.draw(batch);
        //batch.end();

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

    public static void createParts(){
        table = new Table();
        table.setFillParent(true);

        // Creating all the textures
        Texture goldCoinTexture = new Texture(Gdx.files.internal("models/gold_1.png"));
        Texture silverCoinTexture = new Texture(Gdx.files.internal("models/silver_1.png"));
        Texture copperCoinTexture = new Texture(Gdx.files.internal("models/bronze_1.png"));

        Texture speedTexture = new Texture(Gdx.files.internal("models/speed_up.png"));
        Texture invincibilityTexture = new Texture(Gdx.files.internal("models/invincshield2.png"));
        Texture damageIncreaseTexture = new Texture(Gdx.files.internal("models/damage_increase.png"));
        Texture coinMultiplierTexture = new Texture(Gdx.files.internal("models/coin_multiplier.png"));
        Texture pointMultiplierTexture = new Texture(Gdx.files.internal("models/point_multiplier.png"));

        // Creating all the labels
        Label informationLabel = new Label("INFORMATION!", Configuration.SKIN, "title");
        informationLabel.setAlignment(Align.center);

        Label keysInformation1 = new Label("Use your arrow keys or WASD to move around.", Configuration.SKIN, "big");
        keysInformation1.setAlignment(Align.center);

        Label keysInformation2 = new Label("1 and 2 to zoom.", Configuration.SKIN, "big");
        keysInformation2.setAlignment(Align.center);

        Label keysInformation3 = new Label("ESC to save and exit.", Configuration.SKIN, "big");
        keysInformation3.setAlignment(Align.center);

        Label shootingInformation = new Label("There are two methods of shooting, burst and singular.", Configuration.SKIN, "big");
        shootingInformation.setAlignment(Align.center);

        Label shootingInformationTwo = new Label("Click where you want to shoot!", Configuration.SKIN, "big");
        shootingInformationTwo.setAlignment(Align.center);

        Label burstShoot = new Label("Right-Click to burst shoot!", Configuration.SKIN, "big");
        burstShoot.setAlignment(Align.center);

        Label singularShoot = new Label("Left-Click to singular shoot!", Configuration.SKIN, "big");
        singularShoot.setAlignment(Align.center);

        Label warning = new Label("Be careful not to kill the ducks, you will anger long boi!", Configuration.SKIN, "title");
        warning.setAlignment(Align.center);
        warning.setColor(1, 0, 0, 1);

        Label gold = new Label("Gold Coin + 10 plunder", Configuration.SKIN);
        gold.setAlignment(Align.left);

        Label silver = new Label("Silver Coin + 5 plunder", Configuration.SKIN);
        silver.setAlignment(Align.center);

        Label copper = new Label("Copper Coin + 1 plunder", Configuration.SKIN);
        copper.setAlignment(Align.center);

        Label speed = new Label("Increased Speed", Configuration.SKIN);
        speed.setAlignment(Align.center);

        Label invincibility = new Label("Invincibility", Configuration.SKIN);
        invincibility.setAlignment(Align.center);

        Label increasedDamage = new Label("Increased Damage", Configuration.SKIN);
        increasedDamage.setAlignment(Align.center);

        Label coinMultiplier = new Label("Coin Multiplier", Configuration.SKIN);
        coinMultiplier.setAlignment(Align.center);

        Label pointMultiplier = new Label("Point Multiplier", Configuration.SKIN);
        pointMultiplier.setAlignment(Align.center);



        Label collegeInfo1 = new Label("There are two ways to win the game!", Configuration.SKIN, "big");
        Label collegeInfo2 = new Label("taking down a college will give you 5 gold and 3 score a second and if you capture all of them you win!", Configuration.SKIN, "big");
        Label collegeInfo3 = new Label("the other way is to destroy all of them which will give you instant gold and points!", Configuration.SKIN, "big");
        Label collegeInfo4 = new Label("destroying all of them will result in a victory!", Configuration.SKIN, "big");
        Label weather = new Label("More points are recieved in bad weather, but watch out for the penalties", Configuration.SKIN, "big");

        Label spaceToSkip = new Label("Press the space bar to skip the information!", Configuration.SKIN);
        spaceToSkip.setAlignment(Align.center);

        // Creates a uniform X/Y table.
        table.add(informationLabel);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(keysInformation1);
        table.row();
        table.add(keysInformation2);
        table.row();
        table.add(keysInformation3);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(shootingInformation);
        table.row();
        table.add(shootingInformationTwo);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(burstShoot);
        table.row();
        table.add(singularShoot);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(gold);
        table.row();
        table.add(new Image(goldCoinTexture));
        table.row();
        table.add(silver);
        table.row();
        table.add(new Image(silverCoinTexture));
        table.row();
        table.add(copper);
        table.row();
        table.add(new Image(copperCoinTexture));
        table.row();
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.row();
        table.add(collegeInfo1);
        table.row();
        table.add(collegeInfo2);
        table.row();
        table.add(collegeInfo3);
        table.row();
        table.add(collegeInfo4);
        table.row();
        table.add(weather);
        table.row();
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(Configuration.SPACER_LABEL);
        table.row();
        table.add(speed);
        table.row();
        table.add(new Image(speedTexture));
        table.row();
        table.add(invincibility);
        table.row();
        table.add(new Image(invincibilityTexture));
        table.row();
        table.add(increasedDamage);
        table.row();
        table.add(new Image(damageIncreaseTexture));
        table.row();
        table.add(coinMultiplier);
        table.row();
        table.add(new Image(coinMultiplierTexture));
        table.row();
        table.add(pointMultiplier);
        table.row();
        table.add(new Image(pointMultiplierTexture));
        table.row();
        table.row();
        table.add(warning);
        table.row();
        table.row();
        table.row();
        task = Timer.schedule(new ChangeScreenTask(ScreenType.GAME), 40);
    }}
