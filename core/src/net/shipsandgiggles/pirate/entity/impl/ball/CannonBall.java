package net.shipsandgiggles.pirate.entity.impl.ball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.cache.impl.BallHandler;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.MovableEntity;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class CannonBall extends MovableEntity {

    private static final float SPEED = 1.2f;

    private final World world;
    private final Vector2 target;
    private final Fixture fixture;
    private boolean isDead;
    private boolean setAngle;
    private float angle;
    private float timer;

    private float x = 0;
    private float y = 0;
    private float damageDealt = 50f;
    private boolean teleported = false;

    public CannonBall(Sprite texture, Vector2 position, Vector2 target, World world, short categoryBits, short maskBit, short groupIndex) {
        super(UUID.randomUUID(), texture, null, EntityType.CANNON_BALL, 20f, 0f, 0f, texture.getHeight(), texture.getWidth());

        this.isDead = false;
        this.world = world;
        this.target = target;
        this.setAngle = false;
        this.timer = 5;

        BodyDef def = new BodyDef();
        def.bullet = true;
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x, position.y);

        Body body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() * 1.5f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = (short) (maskBit | Configuration.CAT_WALLS);
        fixtureDef.filter.groupIndex = groupIndex;
        this.fixture = body.createFixture(fixtureDef);
        this.setBody(body);

        shape.dispose();
    }

    public CannonBall(Sprite texture, Vector2 position, float target, World world, short categoryBits, short maskBit, short groupIndex) {
        this(texture, position, null, world, categoryBits, maskBit, groupIndex);

        this.angle = target;
        this.setAngle = true;

        this.fixture.setUserData(this);
        this.getBody().setTransform(this.getBody().getPosition().x, this.getBody().getPosition().y, angle);
    }

    public void update(Batch batch) {
            timer -= Gdx.graphics.getDeltaTime();

            if(timer <= 0 && !this.isDead()){
                this.x = this.getBody().getPosition().x;
                this.y = this.getBody().getPosition().y;
                GameScreen.add(new Vector2(x, y));
                this.world.destroyBody(this.getBody());
                this.isDead = true;
                BallHandler.get().get().remove(this.getUniqueId());
            }

            if(!setAngle){
                this.getBody().setTransform(this.getBody().getPosition().x, this.getBody().getPosition().y, (float)Math.atan2( this.getBody().getPosition().x-this.target.x,this.target.y- this.getBody().getPosition().y  ));
                this.setAngle = true;
                this.angle = this.getBody().getAngle();
            }
            
            if(this.getBody().getAngle() != this.angle | this.getBody().getPosition().x < 0 | this.getBody().getPosition().x > 1920 | this.getBody().getPosition().y < 0 | this.getBody().getPosition().y > 1080){
                teleportBall();
            }

            this.getBody().applyForceToCenter(this.getBody().getWorldVector(new Vector2(0, 200079f)), true);
            Vector2 direction = new Vector2(this.getBody().getWorldPoint(new Vector2(0,this.getSkin().getHeight())));
            Vector2 position = this.getBody().getPosition();
            position.x = position.x + (direction.x - position.x) * this.getSpeed() * PIXEL_PER_METER;
            position.y = position.y + (direction.y - position.y) * this.getSpeed() * PIXEL_PER_METER;
            this.getBody().setTransform(position, this.getBody().getAngle());
            this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f));
            this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));

           this.draw(batch);
        }

    @Override
    public void draw(Batch batch) {
        batch.begin();
        this.getSkin().draw(batch);
        batch.end();
    }

    @Override
    public void death() {
        this.world.destroyBody(this.getBody());
        this.isDead = true;
        BallHandler.get().get().remove(this.getUniqueId());
    }

    @Override
    public void shootPlayer(Ship player) {

    }

    public World getWorld() {
        return world;
    }

    public Vector2 getTarget() {
        return target;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean shouldSetAngle() {
        return setAngle;
    }

    public float getAngle() {
        return angle;
    }

    public float getDamageDealt() {
        return this.damageDealt;
    }

    public boolean isTeleported() {
        return this.teleported;
    }

    public void teleportBall(){
        if(this.teleported) return;
        x = this.getBody().getPosition().x;
        y = this.getBody().getPosition().y;
        GameScreen.add(new Vector2(x, y));
        this.getBody().setTransform(10000,10000,0);
        this.teleported = true;
    }
}