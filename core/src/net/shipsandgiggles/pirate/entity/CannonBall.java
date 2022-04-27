package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Cannon Ball
 * This is the creation and update for each individual ball
 *
 * @author Team 23
 * @version 1.0
 */
public class CannonBall {
    public short maskBits;
    public short categoryBits;
    // Main data store
    public float timer = 0.8f;
    public World world;
    public Body body;
    public boolean isDestroyed = false;
    public Vector2 target;
    public boolean setAngle = false;
    public float angle;
    public Sprite cannonBall;
    public float speed = 1.1f;
    public float damageDelt = 50f;
    public boolean teleported = false;
    public float finalX = 0;
    public float finalY = 0;
    public int multiplier;

    /**
     * Instantiates the cannon ball object
     *
     * @param world : World data
     * @param cannonBall : Image used for the object
     * @param multiplier : Damage multiplier
     * @param width : Width of the object sprite
     * @param height : Height of the object sprite
     * @param position : Ball location of the object in the world
     * @param target : Target location of the object in the world
     * @param categoryBits : Category of the object
     * @param maskBit : Object mask
     * @param groupIndex : Position in group
     */
    public CannonBall(World world, Sprite cannonBall, int multiplier, int width, int height, Vector2 position, Vector2 target, short categoryBits, short maskBit, short groupIndex){ //constructor
        //LoadingScreen.soundController.playCannonShot(); /**plays sound of shooting */ COMMENTED OUT FOR TESTING
        this.world = world;

        // Instantiating a body
        Body body;
        BodyDef def = new BodyDef();
        this.target = target;
        this.cannonBall = cannonBall;
        this.multiplier = multiplier;
        this.categoryBits = categoryBits;
        this.maskBits = maskBit;


        def.bullet = true;
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x, position.y);

        // Creation of the body
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height * 1.5f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = categoryBits; // Telling it what category it is
        fixtureDef.filter.maskBits = (short) (maskBit); // Telling it what can be hit
        fixtureDef.filter.groupIndex = groupIndex;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
    }

    /**
     * Instantiates the cannon ball object
     *
     * @param world : World data
     * @param cannonBall : Image used for the object
     * @param width : Width of the object sprite
     * @param height : Height of the object sprite
     * @param position : Ball location of the object in the world
     * @param target : Target location of the object in the world
     * @param categoryBits : Category of the object
     * @param maskBit : Object mask
     * @param groupIndex : Position in group
     */
    CannonBall(World world, Sprite cannonBall, int width, int height, Vector2 position, float target, short categoryBits, short maskBit, short groupIndex){ // constructor
        //LoadingScreen.soundController.playCannonShot(); /**plays sound of shooting */ COMMENTED OUT FOR TESTING
        this.world = world;

        // Instantiating a body
        Body body;
        BodyDef def = new BodyDef();
        this.angle = target;
        this.cannonBall = cannonBall;
        this.categoryBits = categoryBits;
        this.maskBits = maskBit;

        def.bullet = true;
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x, position.y);

        // Creation of the body
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height * 1.5f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = categoryBits; // Telling it what category it is
        fixtureDef.filter.maskBits = (short) (maskBit); // Telling it what can be hit
        fixtureDef.filter.groupIndex = groupIndex;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y, angle); //sets the angle
        this.setAngle = true;
    }

    /**
     * Updates the state of each cannonball
     *
     * @param batch : The batch of spite data
     */
    public void update(Batch batch) {
        movement();
        batch.begin();
        this.cannonBall.draw(batch);
        batch.end();
    }

    /**
     * Applies damage to an object
     *
     * @return Damage dealt
     */
    public float getDamageDelt() {
        if (this.maskBits == Configuration.Cat_Player){
            if(DifficultyScreen.difficulty == 1){
                return this.damageDelt / 4;
            }
            else if (DifficultyScreen.difficulty == 2) {
                return this.damageDelt / 2;
            }

            return this.damageDelt;
        }
        return (this.damageDelt * this.multiplier);
    }

    /**
     * Used to increase the speed of the cannon fire
     * Applies the explosion sound effect (when initially fired)
     */
    public void teleportBall(){
        // Checks if teleported
        if(teleported) return;

        // Plays explosion noise
        LoadingScreen.soundController.playExplosion();

        // Teleports cannonball
        finalX = this.body.getPosition().x;
        finalY = this.body.getPosition().y;
        GameScreen.add(new Vector2(finalX, finalY));
        this.body.setTransform(10000,10000,0);
        this.teleported = true;
    }

    /**
     * Applies movement to the cannonball
     */
    public void movement(){
        // Checks if the ball has reached the maximum time it can be alive
        timer -= Gdx.graphics.getDeltaTime();
        if(timer <= 0 && !this.isDestroyed){
            // Plays explosion noise
            LoadingScreen.soundController.playExplosion();

            // Sets final position
            finalX = this.body.getPosition().x;
            finalY = this.body.getPosition().y;

            // Adds explosion animation
            GameScreen.add(new Vector2(finalX, finalY));

            // Destorys the cannonball
            this.world.destroyBody(this.body);
            this.isDestroyed = true;

            // Removes the cannonball from the array
            BallsManager.removeNext();
        }

        /*
        Checks if the cannonball has an angle or a target
        If it doesn't have an angle, create one towards the target
        */
        if(!setAngle){
            this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y, (float)Math.atan2( this.body.getPosition().x-this.target.x,this.target.y- this.body.getPosition().y  )); // setting the angle towards the target
            this.setAngle = true;
            this.angle = this.body.getAngle();//getting the angle
        }
        if(this.body.getAngle() != this.angle | this.body.getPosition().x < 0 | this.body.getPosition().y < 0){ // checks ifthe ball left the play area or has changes in its angle (if collided)
            teleportBall();
        }

        // Applies force to the ball
        this.body.applyForceToCenter(this.body.getWorldVector(new Vector2(0, 200079f)), true);

        // Gets the direction the ball is going towards
        Vector2 direction = new Vector2(this.body.getWorldPoint(new Vector2(0,this.cannonBall.getHeight())));
        Vector2 position = this.body.getPosition();

        // Changes the direction and slightly teleports the ball so it can travel way faster
        position.x = position.x + (direction.x - position.x) * speed * PIXEL_PER_METER;
        position.y = position.y + (direction.y - position.y) * speed * PIXEL_PER_METER;

        // Moves ball forward
        this.body.setTransform(position, this.body.getAngle());
        this.cannonBall.setPosition(this.body.getPosition().x * PIXEL_PER_METER - (this.cannonBall.getWidth() / 2f), this.body.getPosition().y * PIXEL_PER_METER - (this.cannonBall.getHeight() / 2f));
        this.cannonBall.setRotation((float) Math.toDegrees(this.body.getAngle()));
    }
}
