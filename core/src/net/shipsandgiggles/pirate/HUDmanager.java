package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.awt.*;

public class HUDmanager {
    public Stage stage;
    private Viewport viewport;

    private int score;
    private int gold;

    public float fontScale = 1.5f;
    public float timeCounter = 0;
    public float coolDownTimerTime;

    public Texture healthBar = new Texture("models/bar.png");

    Label scoreLabelCounter;
    Label goldLabel;
    Label scoreLabel;
    Label cooldownTimer;
    Label health;
    Label healthLabel;
    Image goldCoin = new Image(new Texture("models/gold_coin.PNG"));
    Image burstLogo = new Image(new Texture("models/burst_icon.png"));
    Image shootLogo = new Image(new Texture("models/attack_icon.png"));
    Image burstCooldownLogo = new Image(new Texture("models/burst_onCoolDown.png"));
    Stack cooldown = new Stack();
    Table bottomRightTable = new Table();
    Table bottomLeftTable = new Table();


    public HUDmanager(SpriteBatch batch){
        score = Currency.get().balance(Currency.Type.POINTS);
        gold = Currency.get().balance(Currency.Type.GOLD);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table topLeftTable = new Table();

        topLeftTable.setSize(200,Gdx.graphics.getHeight());
        topLeftTable.top().left();

        scoreLabelCounter = new Label(String.format("%06d", score), Configuration.SKIN, "big");
        goldLabel = new Label(String.format("%06d", gold), Configuration.SKIN, "big");
        cooldownTimer = new Label("" + coolDownTimerTime, Configuration.SKIN, "big");
        scoreLabel = new Label("Score: ", Configuration.SKIN, "big");



        topLeftTable.add(goldCoin);
        topLeftTable.add(goldLabel);

        topLeftTable.row();
        topLeftTable.add(scoreLabel);
        topLeftTable.add(scoreLabelCounter);

        stage.addActor(topLeftTable);



        bottomRightTable.setSize(Gdx.graphics.getWidth(),200);
        bottomRightTable.top().left();

        cooldown.add(burstLogo);


        bottomRightTable.add(shootLogo);
        bottomRightTable.add(cooldown);

        bottomRightTable.setPosition(Gdx.graphics.getWidth()-300, -70);

        stage.addActor(bottomRightTable);

        healthLabel = new Label("Health: ", Configuration.SKIN, "big");
        health = new Label("" + Ship.health + " / " + Ship.maxHealth, Configuration.SKIN, "big");
        bottomLeftTable.add(healthLabel);
        bottomLeftTable.add(health);
        bottomLeftTable.setPosition(150, 80);
        bottomLeftTable.row();
        stage.addActor(bottomLeftTable);
    }

    public void updateLabels(Batch batch){
        coolDownTimerTime = Ship.burstTimer;
        health.setText("" + Ship.health + " / " + Ship.maxHealth);

        if(Ship.health > (Ship.maxHealth * 0.6)){
            batch.setColor(Color.GREEN);
        }
        else if(Ship.health > (Ship.maxHealth * 0.25)){
            batch.setColor(Color.ORANGE);
        }
        else{
            batch.setColor(Color.RED);
        }

        batch.begin();
        batch.draw(healthBar, 0,20,Gdx.graphics.getWidth()/5 * (Ship.health/Ship.maxHealth), 30);
        batch.end();

        batch.setColor(Color.WHITE);

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





        gold = Currency.get().balance(Currency.Type.GOLD);
        score = Currency.get().balance(Currency.Type.POINTS);

        scoreLabelCounter.setText(String.format("%06d", score));
        goldLabel.setText(String.format("%06d", gold));
    }
}