package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

public class Weather extends Sprite {
    private final float x;
    private final float y;
    private final int size;
    private Texture weather;
    public World world;
    public Body body;

    public Weather(World world, float x, float y, int size) {
        weather = new Texture("models/fog.png");
        this.world = world;
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

    public void defineWeather() {
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
        shape.dispose();
    }

    public void draw(Batch batch){
        batch.begin();
        int c = 32 * size;
        int x = 0;

        for (int i = size; i > 0; i--){
            for (int j = size; j > 0; j--) {
                batch.draw(weather, this.body.getPosition().x - 32 * size + x, this.body.getPosition().y - c, weather.getWidth() * 2f, weather.getHeight() * 2f);
                c = c - 64;
            }
            c = 32*size;
            x = x + 64;
        }
        batch.end();
    }
}
