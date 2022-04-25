package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

public class Weather extends Sprite {
    private final GameScreen screen;
    private Texture weather;
    public World world;
    public Body body;

    public Weather(GameScreen screen) {
        this.screen = screen;
        weather = new Texture("models/fog.png");
        this.world = screen.getWorld();

        defineWeather();
        setBounds(0, 0, 64, 64);
        setRegion(weather);
        setOrigin(32,32);
    }

    public void update(float dt) {

    }

    private void defineWeather() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(2000, 1600);
        bDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32, 32);

        fDef.filter.categoryBits = Configuration.Cat_Weather;
        fDef.shape = shape;
        fDef.isSensor = true;

        body.createFixture(fDef).setUserData(this);
    }

    public void draw(Batch batch){
        batch.begin();
        batch.draw(weather, this.body.getPosition().x - 32, this.body.getPosition().y - 32, 64, 64);
        batch.end();
    }
}
