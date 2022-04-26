package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

public class Weather extends Sprite {
    private final GameScreen screen;
    private final float x;
    private final float y;
    private final int size;
    private Texture weather;
    public World world;
    public Body body;

    public Weather(GameScreen screen, float x, float y, int size) {
        this.screen = screen;
        weather = new Texture("models/fog.png");
        this.world = screen.getWorld();
        this.x = x;
        this.y = y;
        this.size = size;

        defineWeather();
        setBounds(0, 0, 32, 32);
        setRegion(weather);
        setOrigin(32,32);
    }

    public void update(float dt) {

    }

    private void defineWeather() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(weather.getWidth() * size, weather.getHeight() * size);

        fDef.filter.categoryBits = Configuration.Cat_Weather;
        fDef.shape = shape;
        fDef.isSensor = true;

        body.createFixture(fDef).setUserData(this);
    }

    public void draw(Batch batch){
        batch.begin();
        int c = 32 * size;

        for (int i = size - 1; i > 0; i--){
            batch.draw(weather, this.body.getPosition().x - c, this.body.getPosition().y - c, weather.getWidth() * 2f, weather.getHeight() * 2f);
            batch.draw(weather, this.body.getPosition().x - c, this.body.getPosition().y - c + 64, weather.getWidth() * 2f, weather.getHeight() * 2f);
            batch.draw(weather, this.body.getPosition().x - c + 64, this.body.getPosition().y - c, weather.getWidth() * 2f, weather.getHeight() * 2f);
            batch.draw(weather, this.body.getPosition().x - c + 64, this.body.getPosition().y - c + 64, weather.getWidth() * 2f, weather.getHeight() * 2f);
            c = c - 64 * 2;

        }
        batch.end();
    }
}
