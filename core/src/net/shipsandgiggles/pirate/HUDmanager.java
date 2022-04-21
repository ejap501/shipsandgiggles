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
        Table topLeftTable = new Table();
        topLeftTable.setSize(200,Gdx.graphics.getHeight());
        topLeftTable.top().left();

        scoreLabelCounter = new Label(String.format("%06d", score), Configuration.SKIN, "big");
        goldLabel = new Label(String.format("%06d", gold), Configuration.SKIN, "big");
        cooldownTimer = new Label("" + coolDownTimerTime, Configuration.SKIN, "big");
        scoreLabel = new Label("Score: ", Configuration.SKIN, "big");

        // Order of adding the UI
        topLeftTable.add(goldCoin);
        topLeftTable.add(goldLabel);

        topLeftTable.row();
        topLeftTable.add(scoreLabel);
        topLeftTable.add(scoreLabelCounter);

        stage.addActor(topLeftTable);

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