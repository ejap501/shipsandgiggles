package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Rain {
    private ParticleEffect rain;
    private Batch batch;
    private float deltaTime;
    private float count = 0;
    private Sprite brightness;
    private float alpha = 0f;
    private boolean stopCycle = false;

    public Rain() {
        rain = new ParticleEffect();
        rain.load(Gdx.files.internal("models/rain.p"), Gdx.files.internal("models"));
        rain.setPosition(Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2f);

        brightness = new Sprite(new Texture("models/black.png"));
        brightness.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void draw(Batch batch, float deltaTime) {
        this.batch = batch;
        this.deltaTime = deltaTime;
        count += deltaTime;

        if((count >= 0) && (count < 1)) {
            rain.start();
        }
        if(count >= 15f) {
            if (!stopCycle){
                batch.begin();
                brightness.setAlpha(alpha + 0.0025f);
                brightness.draw(batch);
                if (alpha <= 0.3f) {
                    alpha = alpha + 0.0025f;}
                batch.end();
            }
        }
        if(count >= 15.4f) {
            batch.begin();
            rain.draw(batch, deltaTime);
            batch.end();
        }
        if(count >= 30.4f) {
            rain.allowCompletion();
        }
        if(count >= 31f) {
            batch.begin();
            brightness.setAlpha(alpha - 0.001f);
            brightness.draw(batch);
            if (alpha >= 0.01f) {
                alpha = alpha - 0.001f;
            }
            batch.end();
            if (!stopCycle) {
                stopCycle = true;
            }
        }
        if(count >= 37f) {
            count = 0;
            if (stopCycle) {
                stopCycle = false;
            }
        }
    }
}
