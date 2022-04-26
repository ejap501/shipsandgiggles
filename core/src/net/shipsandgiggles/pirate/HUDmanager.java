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
    private Viewport viewport;

    public float timeCounter = 0;
    public float fontScale = 1.5f;
    public float coolDownTimerTime;

    public Texture healthBar = new Texture("models/bar.png");

    // Setting labels and getting other textures
    Label scoreLabelCounter;
    Label goldLabel;
    Label scoreLabel;
    Label cooldownTimer;
    Label health;
    Label healthLabel;
    Label invTimer;
    Label speedTimer;
    Label coinTimer;
    Label pointTimer;
    Label damTimer;
    Label invLabel ;
    Label coinLabel ;
    Label pointLabel;
    Label speedLabel;
    Label damLabel ;
    Image goldCoin = new Image(new Texture("models/gold_coin.png"));
    Image burstLogo = new Image(new Texture("models/burst_icon.png"));
    Image shootLogo = new Image(new Texture("models/attack_icon.png"));
    Image burstCooldownLogo = new Image(new Texture("models/burst_onCoolDown.png"));
    Image shopRange = new Image(new Texture("models/shop_icon.png"));
    Image shopRangeCooldown = new Image(new Texture("models/shop_icon_cooldown.png"));
    Stack cooldown = new Stack();
    Stack shop = new Stack();
    Table abalities = new Table();
    Table bottomLeftTable = new Table();
    Table topRightTable =  new Table();
    Table topLeftTable = new Table();
    Table timerLabels = new Table();


    /**
     * Initialises the hud
     *
     * @param batch : The batch of sprite data
     */
    public HUDmanager(SpriteBatch batch){
        // Setting the score
        score = Currency.get().balance(Currency.Type.POINTS);
        gold = Currency.get().balance(Currency.Type.GOLD);

        // Setting a view port and stage for the camera to paste it on the screen and not map
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        // Creation of the top left bit of the screen

        topLeftTable.setSize(200,Gdx.graphics.getHeight());
        topLeftTable.top().left();

        topRightTable.setSize(400,Gdx.graphics.getHeight());
        topRightTable.pad(0,3300,900,0);

        timerLabels.setSize(400,Gdx.graphics.getHeight());
        timerLabels.pad(0,2800,900,0);

        scoreLabelCounter = new Label(String.format("%06d", score), Configuration.SKIN, "big");
        goldLabel = new Label(String.format("%06d", gold), Configuration.SKIN, "big");
        cooldownTimer = new Label("" + coolDownTimerTime, Configuration.SKIN, "big");
        scoreLabel = new Label("Score: ", Configuration.SKIN, "big");

        invLabel = new Label("Invincibility Timer : ", Configuration.SKIN, "big");
        coinLabel = new Label("Coin Multiplier Timer : ", Configuration.SKIN, "big");
        pointLabel = new Label("Point Multiplier Timer : ", Configuration.SKIN, "big");
        speedLabel = new Label("Speed Boost Timer : ", Configuration.SKIN, "big");
        damLabel = new Label("Damage Boost Timer : ", Configuration.SKIN, "big");

        invTimer  = new Label("" + GameScreen.invincibilityTimer, Configuration.SKIN, "big");
        coinTimer  = new Label("" + GameScreen.coinTimer, Configuration.SKIN, "big");
        pointTimer  = new Label("" + GameScreen.pointTimer, Configuration.SKIN, "big");
        speedTimer  = new Label("" + GameScreen.speedTimer, Configuration.SKIN, "big");
        damTimer  = new Label("" + GameScreen.damageTimer, Configuration.SKIN, "big");

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

        stage.addActor(topLeftTable);
        stage.addActor(topRightTable);
        stage.addActor(timerLabels);

        // Creation of bottom left of the screen
        abalities.setSize(Gdx.graphics.getWidth(),200);
        abalities.top().left();

        cooldown.add(burstLogo);
        shop.add(shopRangeCooldown);

        abalities.add(shootLogo);
        abalities.add(cooldown);
        abalities.add(shop);

        abalities.setPosition(0, -70);

        stage.addActor(abalities);

        healthLabel = new Label("Health: ", Configuration.SKIN, "big");
        health = new Label("" + Ship.health + " / " + Ship.maxHealth, Configuration.SKIN, "big");

        // Adds order
        bottomLeftTable.add(healthLabel);
        bottomLeftTable.add(health);
        bottomLeftTable.setPosition(150, 200);
        bottomLeftTable.row();
        stage.addActor(bottomLeftTable);
    }

    /**
     * Updates all the variables on screen
     *
     * @param batch : The batch of sprite data
     * */
    public void updateLabels(Batch batch){
        coolDownTimerTime = Ship.burstTimer;
        String healthText = " " + Ship.health;

        // Changes colour of health bar based on health percentage
        if(Ship.health > (Ship.maxHealth * 0.49)){
            batch.setColor(Color.GREEN);
            health.setText("" + healthText.substring(0,6) + " / " + Ship.maxHealth);
        }
        else if(Ship.health > (Ship.maxHealth * 0.25)){
            batch.setColor(Color.ORANGE);
            health.setText("" + healthText.substring(0,5) + " / " + Ship.maxHealth);
        }
        else{
            batch.setColor(Color.RED);
            health.setText("" + healthText.substring(0,5) + " / " + Ship.maxHealth);
        }

        // Draws health bar
        batch.begin();
        batch.draw(healthBar, 0,140,Gdx.graphics.getWidth()/5 * (Ship.health/Ship.maxHealth), 30);
        batch.end();

        batch.setColor(Color.WHITE);

        // Update variables and give points to player every 2 seconds
        timeCounter += Gdx.graphics.getDeltaTime();
        if(timeCounter >= 2){
            Currency.get().give(Currency.Type.POINTS, 1);
            timeCounter = 0;
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
            coinTimer.setText("" + coinText.substring(0,2));
        }
        else{
            coinTimer.setText(0);
        }

        if(GameScreen.pointTimer > 0){
            String pointText = "" + GameScreen.pointTimer;
            pointTimer.setText("" + pointText.substring(0,2));
        }
        else {
            pointTimer.setText(0);
        }

        if(GameScreen.damageTimer > 0){
            String damageText = "" + GameScreen.damageTimer;
            damTimer.setText("" + damageText.substring(0,2));
        }
        else {
            damTimer.setText(0);
        }

        if(GameScreen.speedTimer > 0){
            String speedText = "" + GameScreen.speedTimer;
            speedTimer.setText("" + speedText.substring(0,2));
        }
        else {
            speedTimer.setText(0);
        }

        if(GameScreen.invincibilityTimer > 0){
            String invText = "" + GameScreen.invincibilityTimer;
            invTimer.setText("" + invText.substring(0,2));
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
        goldLabel.setText(String.format("%06d", gold));
    }
}
