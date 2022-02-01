package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

    Label scoreLabelCounter;
    Label goldLabel;
    Label scoreLabel;
    Label cooldownTimer;
    Image goldCoin = new Image(new Texture("models/gold_coin.PNG"));
    Image burstLogo = new Image(new Texture("models/burst_icon.png"));
    Image shootLogo = new Image(new Texture("models/attack_icon.png"));
    Image image = new Image(new Texture("models/image.png"));
    Stack cooldown = new Stack();
    Table bottomRightTable = new Table();


    public HUDmanager(SpriteBatch batch){
        score = Currency.get().balance(Currency.Type.POINTS);
        gold = Currency.get().balance(Currency.Type.GOLD);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table topLeftTable = new Table();

        topLeftTable.setSize(200,Gdx.graphics.getHeight());
        topLeftTable.top().left();

        scoreLabelCounter = new Label(String.format("%06d", score), Configuration.SKIN);
        goldLabel = new Label(String.format("%06d", gold), Configuration.SKIN);
        cooldownTimer = new Label("" + coolDownTimerTime, Configuration.SKIN);
        scoreLabel = new Label("Score: ", Configuration.SKIN);

        scoreLabel.setFontScale(fontScale);
        goldLabel.setFontScale(fontScale);
        scoreLabelCounter.setFontScale(fontScale);


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

        bottomRightTable.setPosition(Gdx.graphics.getWidth()-300, -40);

        stage.addActor(bottomRightTable);
    }

    public void updateLabels(){
        coolDownTimerTime = Ship.burstTimer;

        timeCounter += Gdx.graphics.getDeltaTime();
        if(timeCounter >= 2){
            Currency.get().give(Currency.Type.POINTS, 1);
            timeCounter = 0;
        }
            if(coolDownTimerTime > 0){
                cooldown.removeActor(burstLogo);
                cooldown.add(image);
                String coolDownText = "" + coolDownTimerTime;
                cooldownTimer.setText("   "+ coolDownText.substring(0,3));
                cooldownTimer.setFontScale(3);
                cooldown.add(cooldownTimer);
            }
            else{
                cooldown.removeActor(image);
                cooldown.removeActor(cooldownTimer);
                cooldown.add(burstLogo);
            }





        gold = Currency.get().balance(Currency.Type.GOLD);
        score = Currency.get().balance(Currency.Type.POINTS);

        scoreLabelCounter.setText(String.format("%06d", score));
        goldLabel.setText(String.format("%06d", gold));
    }

    public void updateBurstTimer(float burstTimer){
        coolDownTimerTime = burstTimer;
    }
}
