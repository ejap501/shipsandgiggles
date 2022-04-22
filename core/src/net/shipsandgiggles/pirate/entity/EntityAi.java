package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;

import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.conf.Configuration;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Entity AI
 * The creation and AI of the enemy boat
 * Edited from original
 *
 * @author Team 23
 * @author Team 22 : Edward Poulter
 * @version 2.0
 */
public class EntityAi implements Steerable<Vector2> {
    // Main data store
    public Body body;
    boolean tagged;
    float maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration, boundingRadius, zeroLinearSpeedThreshold, speedMultiplier, turnMultiplier;
    Sprite texture;
    boolean isPlayer;
    Body target;
    float amountOfRotations = 0;
    private boolean independentFacing = false; // Defines if the entity can move in a direction other than the way it faces)
    float angleToTarget = 0;
    int health;
    int maxHealth;
    public boolean dead;
    public float counter = 0;
    public float timer = 0f;
    private Rectangle hitBox;
    public Sprite cannonBallSprite =  new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

    SteeringBehavior<Vector2> behavior;
    public SteeringAcceleration<Vector2> steeringOutput;

    /**
     * Creation of the Ai of the enemy
     *
     * @param body : Enemy entity
     * @param boundingRadius : Positional bounds
     * @param texture: Image used for the object
     * @param width : Width of the enemy
     * @param height : Height of the enemy
     */
    public EntityAi(Body body, float boundingRadius, Sprite texture, int width, int height){
        // Constructor
        this.body = body;
        this.boundingRadius = boundingRadius;
        this.texture = texture;

        this.isPlayer = false;
        this.maxLinearSpeed = 5000;
        this.maxLinearAcceleration = 5000;
        this.maxAngularSpeed = 90;
        this.maxAngularAcceleration = 30;
        this.zeroLinearSpeedThreshold = 0.1f;
        this.speedMultiplier = 60f;
        this.turnMultiplier = 0.01f;

        this.tagged = false;
        this.body.setFixedRotation(false);
        MassData MassData = new MassData();
        MassData.mass= 6000f;
        MassData.center.set(this.getPosition().x/2, this.getPosition().y/2);
        this.body.setMassData(MassData);

        this.steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);
        this.body.setLinearDamping(1f);

        // Sets health
        this.health = 100;
        this.maxHealth = 100;
        this.dead = false;

        // Constructs hitbox detector
        this.hitBox =  new Rectangle(this.body.getPosition().x, this.body.getPosition().y, texture.getWidth() + 350, texture.getHeight() + 350);

        // Constructs fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Enemy;

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    /**
     * Creation of the target for the player
     *
     * @param body : Enemy body
     * @param boundingRadius : Positional bounds
     */
    public EntityAi(Body body, float boundingRadius){
        this.body = body;
        this.boundingRadius = boundingRadius;

        this.isPlayer = true;
        this.maxLinearSpeed = 5000;
        this.maxLinearAcceleration = 5000;
        this.maxAngularSpeed = 90;
        this.maxAngularAcceleration = 30;
        this.zeroLinearSpeedThreshold = 0.01f;
        this.speedMultiplier = 400f;
        this.turnMultiplier = 2000f;

        this.tagged = false;
        this.body.setFixedRotation(false);

        this.steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);
        this.body.setLinearDamping(1f);
    }

    /**
     * Updates enemy data
     *
     * @param deltaTime : Delta time (elapsed time since last game tick)
     * @param batch : The batch of sprite data
     * @param player : Player data
     * @param world : World data
     */
    public void update(float deltaTime, Batch batch,Ship player, World world){
        if(isPlayer){
            return;
        }
        if (health == 0 && !dead){
            death(world);
        }
        else if(behavior != null){
            // Calculates if needs steering
            this.steeringOutput = behavior.calculateSteering(steeringOutput);
            applySteering(this.steeringOutput, deltaTime);
            shootPlayer(player, world);
        }
        drawEntity(batch);
    }

    /**
     * Draws the entity in the batch
     *
     * @param batch: The batch of sprite data
     */
    public void drawEntity(Batch batch){
        if(!this.isPlayer){
            this.getSprite().setPosition(this.body.getPosition().x * PIXEL_PER_METER - (this.getSprite().getWidth() / 2f), this.body.getPosition().y* PIXEL_PER_METER - (this.getSprite().getHeight() / 2f)); /**sets position and rotation of the boat */
            this.getSprite().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
            batch.begin();
            this.getSprite().draw(batch);
            batch.end();
        }
    }

    /**
     * @return Texture of the enemy
     */
    public Sprite getSprite(){
        return this.texture;
    }

    /**
     * Applies acceleration and directional data to enemy
     *
     * @param steeringOutput : Steering to be applied
     * @param deltaTime : Delta time (elapsed time since last game tick)
     */
    public void applySteering (SteeringAcceleration<Vector2> steeringOutput, float deltaTime) {
        if(isPlayer){
            return;
        }
        boolean anyAccelerations = false;

        // Update position and linear velocity
        if (!steeringOutput.linear.isZero()) {
            // This method internally scales the force by deltaTime
            body.applyForceToCenter(new Vector2(steeringOutput.linear.x * speedMultiplier , (steeringOutput.linear.y * speedMultiplier)), true);
            anyAccelerations = true;
        }

        //  Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                // This method internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular * turnMultiplier, true);
                anyAccelerations = true;
            }
        } else {
            //  If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = this.getLinearVelocity();
            if (steeringOutput.linear.len() > 25f) {
                float newOrientation = vectorToAngle(linVel);
                // sets new angle towards player
                this.setAngleToTarget(this.getAngleToTarget() + ((float)Math.atan2(this.target.getPosition().y - this.getPosition().y, this.target.getPosition().x - this.getPosition().x) - 1.5708f - this.angleToTarget) * turnMultiplier * PIXEL_PER_METER);
                this.getBody().setTransform(this.body.getPosition().x, this.body.getPosition().y, this.getAngleToTarget());
            }
        }

        if (anyAccelerations) {
            // Caps the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
            }
            // Caps the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
            this.hitBox.setPosition(this.body.getPosition());
        }
    }

    /**
     * Attacks the player by shooting at them
     *
     * @param player : Player data
     * @param world : World data
     */
    public void shootPlayer(Ship player, World world) {
        if(this.health == 1 && !this.dead){
            this.counter += Gdx.graphics.getDeltaTime();
            if(this.counter >= 1){
                Currency.get().give(Currency.Type.POINTS, 3);
                Currency.get().give(Currency.Type.GOLD, 5);
                this.counter = 0;
            }
        }


        // Creates shot and shoots
        if(this.hitBox.overlaps(player.hitBox) && timer <= 0 && !this.dead && this.health != 1) {/** creates shot and shoots*/
            BallsManager.createBall(world, new Vector2(this.body.getPosition().x, this.body.getPosition().y), new Vector2(player.getEntityBody().getPosition().x, player.getEntityBody().getPosition().y), 1, cannonBallSprite, (short)(Configuration.Cat_Enemy | Configuration.Cat_College), Configuration.Cat_Player, (short) 0);
            this.timer = 4;
        }

        // Ensures that there is a cool down between every shot
        else if(timer <= 0) this.timer = 0;
        else this.timer -= Gdx.graphics.getDeltaTime();
    }

    /**
     * @return Independent direction facing
     */
    public boolean isIndependentFacing () {
        return independentFacing;
    }

    /**
     * @param independentFacing : Sets new direction to face
     */
    public void setIndependentFacing (boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    /**
     * @return Linear velocity of the enemy
     */
    @Override
    public Vector2 getLinearVelocity() {
        return this.body.getPosition();
    }

    /**
     * @param amountOfRotations : Applies a number of rotations to the enemy
     */
    public void setAmountOfRotations(float amountOfRotations) {
        this.amountOfRotations = amountOfRotations;
    }

    /**
     * @return Fetches the number of rotations of the enemy
     */
    public float getAmountOfRotations() {
        return amountOfRotations;
    }

    /**
     * @return Finds the angular velocity of the enemy
     */
    @Override
    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }

    /**
     * @return Finds the bounding radius of the enemy
     */
    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }

    /**
     * @param angleToTarget : Sets a new target angle
     */
    public void setAngleToTarget(float angleToTarget){
        this.angleToTarget = angleToTarget;
    }

    /**
     * @return Target angle
     */
    public float getAngleToTarget() {
        return angleToTarget;
    }

    /**
     * @return If tagged
     */
    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    /**
     * @param tagged : Tags the enemy
     */
    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    /**
     * @return Threashold for linear speed
     */
    @Override
    public float getZeroLinearSpeedThreshold() {
        return this.zeroLinearSpeedThreshold;
    }

    /**
     * @param value : New linear speed threshold
     */
    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroLinearSpeedThreshold = value;
    }

    /**
     * @return Retrieves max linear speed
     */
    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    /**
     * @param maxLinearSpeed : Sets new max linear speed
     */
    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    /**
     * @return Max linear acceleration (1d)
     */
    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    /**
     * @param body : Entity body of new target
     */
    public void setTarget(Body body){
        this.target = body;
    }

    /**
     * @return Target entity
     */
    public Body getTarget() {
        return target;
    }

    /**
     * @param maxLinearAcceleration : New max linear acceleration (1d)
     */
    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearSpeed = maxLinearAcceleration;
    }

    /**
     * @return Speed at an angle (2d)
     */
    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    /**
     * @param maxAngularSpeed : Sets new speed at an angle (2d)
     */
    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    /**
     * @return Acceleration at an angle
     */
    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    /**
     * @param maxAngularAcceleration : New acceleration at an angle
     */
    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    /**
     * @return Position of enemy
     */
    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    /**
     * @return Orientation of enemy
     */
    @Override
    public float getOrientation() {
        return this.body.getAngle();
    }

    /**
     * @param vector : Vector of line
     * @return Angle formed from vector
     */
    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    /**
     * @param outVector : Old vector from line
     * @param angle : Angle of line
     * @return Vector from angle
     */
    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    /**
     * @return Enemy body
     */
    public Body getBody() {
        return this.body;
    }

    /**
     * Sets the steering behaviour of the enemy
     *
     * @param behavior : Steering vector
     */
    public void setBehavior(SteeringBehavior<Vector2> behavior){
        this.behavior = behavior;
    }

    /**
     * @return : Enemy behaviour
     */
    public SteeringBehavior<Vector2> getBehavior(){
        return this.behavior;
    }

    /**
     * Kills the enemy
     *
     * @param world : World data
     */
    public void death(World world){
        // Checks if dead
        if(this.dead) return;

        // Gives instant money if collected
        Currency.get().give(Currency.Type.GOLD, 10);

        // Kills enemy
        world.destroyBody(body);
        this.dead = true;
        }

    /**
     * Applies damage to enemy
     *
     * @param damageValue : Amount of damage taken by enemy
     */
    public void damage(float damageValue){
        health -= damageValue;
    }

    /**
     * @return null
     */
    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public void setOrientation(float orientation) {

    }
}

