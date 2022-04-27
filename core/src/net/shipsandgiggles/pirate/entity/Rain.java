package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Rain {
    private ParticleEffect rain;
    private ShapeRenderer shapeRenderer;
    private Batch batch;
    private float deltaTime;

    public Rain() {
        rain = new ParticleEffect();
        rain.load(Gdx.files.internal("models/rain.p"), Gdx.files.internal("models"));
        rain.setPosition(Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2f);
        rain.start();

        shapeRenderer = new ShapeRenderer();
    }

    public void draw(Batch batch, float deltaTime) {
        this.batch = batch;
        this.deltaTime = deltaTime;
        batch.begin();
        rain.draw(batch, deltaTime);
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.2f));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
