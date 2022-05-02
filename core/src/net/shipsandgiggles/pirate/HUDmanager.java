package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

/**
 * HUD Manager
 * Manages the data displayed on the hud
 *
 * @author Team 23
 * @version 1.0
 */
public class HUDmanager {
    // Main data store
    public Stage stage;
    public static int gold;
    public static int score;
    private static Viewport viewport;

    public static float timeCounter = 0;
    public static float fontScale = 1.5f;
    public static float coolDownTimerTime = Ship.burstTimer;

    public Texture healthBar = new Texture("models/bar.png");

    // Setting labels and getting other textures
    public static Label scoreLabelCounter = new Label(String.format("%06d", score), Configuration.SKIN, "big");
    public static Label goldLabel = new Label(String.format("%06d", gold), Configuration.SKIN, "big");
    public static Label scoreLabel;
    public static Label cooldownTimer = new Label("" + coolDownTimerTime, Configuration.SKIN, "big");
    public static Label invTimer  = new Label("" + GameScreen.invincibilityTimer, Configuration.SKIN, "big");
    public static Label coinTimer  = new Label("" + GameScreen.coinTimer, Configuration.SKIN, "big");
    public static Label pointTimer  = new Label("" + GameScreen.pointTimer, Configuration.SKIN, "big");
    public static Label speedTimer  = new Label("" + GameScreen.speedTimer, Configuration.SKIN, "big");
    public static Label damTimer  = new Label("" + GameScreen.damageTimer, Configuration.SKIN, "big");
    public static Label invLabel ;
    public static Label coinLabel ;
    public static Label pointLabel;
    public static Label speedLabel;
    public static Label damLabel ;
    public static Image goldCoin = new Image(new Texture("models/gold_coin.png"));
    public static Image burstLogo = new Image(new Texture("models/burst_icon.png"));
    public static Image shootLogo = new Image(new Texture("models/attack_icon.png"));
    public static Image burstCooldownLogo = new Image(new Texture("models/burst_onCoolDown.png"));
    public static Image shopRange = new Image(new Texture("models/shop_icon.png"));
    public static Image shopRangeCooldown = new Image(new Texture("models/shop_icon_cooldown.png"));
    public static Stack cooldown = new Stack();
    public static Stack shop = new Stack();
    public static Table abalities = new Table();
    public static Table bottomLeftTable = new Table();
    public static Table topRightTable =  new Table();
    public static Table topLeftTable = new Table();
    public static Table timerLabels = new Table();

    public static Label healthLabel = new Label("Health: ", Configuration.SKIN, "big");
    public static Label health = new Label("" + Ship.health + " / " + Ship.maxHealth, Configuration.SKIN, "big");


    /**
     * Initialises the hud
     *
     * @param batch : The batch of sprite data
     */
    public HUDmanager(SpriteBatch batch){
        // Setting a view port and stage for the camera to paste it on the screen and not map
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        createParts();
        stage.addActor(topLeftTable);
        stage.addActor(topRightTable);
        stage.addActor(timerLabels);
        stage.addActor(bottomLeftTable);
        stage.addActor(abalities);

    }

    /**
     * Updates all the variables on screen
     *
     * @param batch : The batch of sprite data
     * */
    public void updateLabels(Batch batch){
        batch.setColor(healthBarChecker());
        // Draws health bar
        batch.begin();
        batch.draw(healthBar, 0,140,Gdx.graphics.getWidth()/5 * (Ship.health/Ship.maxHealth), 30);
        batch.end();

        batch.setColor(Color.WHITE);

        variableUpdates();
    }

    /**
     * Creates screen elements
     */
    public static void createParts(){

        // Setting the score
        score = Currency.get().balance(Currency.Type.POINTS);
        gold = Currency.get().balance(Currency.Type.GOLD);


        // Creation of the top left bit of the screen

        topLeftTable.setSize(200,Gdx.graphics.getHeight());
        topLeftTable.top().left();

        topRightTable.setSize(400,Gdx.graphics.getHeight());
        //topRightTable.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
        //topRightTable.setPosition(1350,375);
        topRightTable.setFillParent(true);
        topRightTable.top().right();
        topRightTable.pad(10,0,0,50);

        timerLabels.setSize(400,Gdx.graphics.getHeight());
        //timerLabels.setPosition(1150,375);
        timerLabels.setFillParent(true);
        timerLabels.top().right();
        timerLabels.pad(10,0,0,100);


        scoreLabel = new Label("Score: ", Configuration.SKIN, "big");

        invLabel = new Label("Invincibility Timer : ", Configuration.SKIN, "big");
        coinLabel = new Label("Coin Multiplier Timer : ", Configuration.SKIN, "big");
        pointLabel = new Label("Point Multiplier Timer : ", Configuration.SKIN, "big");
        speedLabel = new Label("Speed Boost Timer : ", Configuration.SKIN, "big");
        damLabel = new Label("Damage Boost Timer : ", Configuration.SKIN, "big");

        // Order of adding the UI
        topLeftTable.add(goldCoin);
        topLeftTable.add(goldLabel);

        topLeftTable.row();
        topLeftTable.add(scoreLabel);
        topLeftTable.add(scoreLabelCounter);

        topRightTable.add(invTimer);
        topRightTable.row();
        topRightTable.add(coinTimer);
        topRightTable.row();
        topRightTable.add(pointTimer);
        topRightTable.row();
        topRightTable.add(speedTimer);
        topRightTable.row();
        topRightTable.add(damTimer);
        topRightTable.row();

        timerLabels.add(invLabel);
        timerLabels.row();
        timerLabels.add(coinLabel);
        timerLabels.row();
        timerLabels.add(pointLabel);
        timerLabels.row();
        timerLabels.add(speedLabel);
        timerLabels.row();
        timerLabels.add(damLabel);


        // Creation of bottom left of the screen
        abalities.setSize(Gdx.graphics.getWidth(),200);
        abalities.top().left();

        cooldown.add(burstLogo);
        shop.add(shopRangeCooldown);

        abalities.add(shootLogo);
        abalities.add(cooldown);
        abalities.add(shop);

        abalities.setPosition(0, -70);



        // Adds order
        bottomLeftTable.add(healthLabel);
        bottomLeftTable.add(health);
        bottomLeftTable.setPosition(150, 200);
        bottomLeftTable.row();
    }
    /**
     * Calculates the colour for the player health bar
     *
     * @return the health bar colour
     */
    public static Color healthBarChecker(){

        String healthText = " " + Ship.health;

        // Changes colour of health bar based on health percentage
        if(Ship.health > (Ship.maxHealth * 0.49)){
            health.setText("" + healthText.substring(0,6) + " / " + Ship.maxHealth);
            return Color.GREEN;
        }
        else if(Ship.health > (Ship.maxHealth * 0.25)){
            health.setText("" + healthText.substring(0,5) + " / " + Ship.maxHealth);
            return Color.ORANGE;
        }
        else{
            health.setText("" + healthText.substring(0,5) + " / " + Ship.maxHealth);
            return Color.RED;
        }
    }

    /**
     * Updates variables of onscreen timers, health and gold/points
     */
    public static void variableUpdates(){
        coolDownTimerTime = Ship.burstTimer;
        // Update variables and give points to player every 2 seconds
        timeCounter += Gdx.graphics.getDeltaTime();
        if(timeCounter >= 2){
            Currency.get().give(Currency.Type.POINTS, 1);
            timeCounter = 0;
            if(Ship.inFog) {
                Currency.get().give(Currency.Type.POINTS, 1);
            }
        }
        if(coolDownTimerTime > 0){
            cooldown.removeActor(burstLogo);
            cooldown.add(burstCooldownLogo);
            String coolDownText = "" + coolDownTimerTime;
            cooldownTimer.setText("    " + coolDownText.substring(0,3));
            cooldownTimer.setFontScale(1.2f);
            cooldown.add(cooldownTimer);
        }
        else{
            cooldown.removeActor(burstCooldownLogo);
            cooldown.removeActor(cooldownTimer);
            cooldown.add(burstLogo);
        }

        if(GameScreen.coinTimer > 0){
            String coinText = "" + GameScreen.coinTimer;
            coinTimer.setText("" + coinText.substring(0,1));
        }
        else{
            coinTimer.setText(0);
        }

        if(GameScreen.pointTimer > 0){
            String pointText = "" + GameScreen.pointTimer;
            pointTimer.setText("" + pointText.substring(0,1));
        }
        else {
            pointTimer.setText(0);
        }

        if(GameScreen.damageTimer > 0){
            String damageText = "" + GameScreen.damageTimer;
            damTimer.setText("" + damageText.substring(0,1));
        }
        else {
            damTimer.setText(0);
        }

        if(GameScreen.speedTimer > 0){
            String speedText = "" + GameScreen.speedTimer;
            speedTimer.setText("" + speedText.substring(0,1));
        }
        else {
            speedTimer.setText(0);
        }

        if(GameScreen.invincibilityTimer > 0){
            String invText = "" + GameScreen.invincibilityTimer;
            invTimer.setText("" + invText.substring(0,1));
        }
        else {
            invTimer.setText(0);
        }



        if(Ship.buyMenuRange){
            shop.removeActor(shopRangeCooldown);
            shop.add(shopRange);
        }else{
            shop.removeActor(shopRange);
            shop.add(shopRangeCooldown);
        }

        // Updates balance
        gold = Currency.get().balance(Currency.Type.GOLD);
        score = Currency.get().balance(Currency.Type.POINTS);

        scoreLabelCounter.setText(String.format("%06d", score));
        goldLabel.setText(String.format("%06d", gold));}
}
