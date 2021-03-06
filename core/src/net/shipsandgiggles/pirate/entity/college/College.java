package net.shipsandgiggles.pirate.entity.college;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

/**
 * College
 * College data that allows us to perform animations / fights more easily.
 *
 * @author Team 23
 * @version 1.0
 */
public abstract class College extends Entity {
	// Main data store
	public Body body;
	public Rectangle hitBox;
	public float counter = 0;
	public boolean dead = false;
	public boolean removed = false;
	private final College.Type type;
	public Texture healthBar = new Texture("models/bar.png");
	public Sprite cannonBallSprite =  new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

	// Timers
	public float timer = 0f;
	public float cooldownTimer = 1f;

	public Boolean captured = false;
	public Boolean givenChanceCapture = false;

	/**
	 * Instantiates the college type
	 *
	 * @param uuid : The unique id of the object
	 * @param type : The type of college
	 * @param texture : Image used for the object
	 * @param location : Position of the object in the world
	 * @param maximumHealth : Maximum health of the entity
	 * @param height : Height of the college
	 * @param width : Width of the college
	 * */
	public College(UUID uuid, College.Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
		super(uuid, texture, location, EntityType.COLLEGE, maximumHealth, height, width);
		this.type = type;
	}

	/** Fetches the type of college */
	public Type getType() {
		return type;
	}

	/** Fetches the body of the college */
	public Body getBody(){
		return this.body;
	}

	/**
	 * @param damage Damage you wish the entity to take.
	 * @return Current health after damage (i.e. {@link #getHealth() - damage}
	 */
	@Override
	public float damage(float damage) {
		if(this.captured){
			return 0f;
		}

		else if ((this.health - damage) <= 0f && !givenChanceCapture) {
			this.health = 1;
			givenChanceCapture = true;
			return 1f;

		}
		else if ((this.health - damage) <= 0f){
			this.health = 0;
			this.death();
			return 0f;
		}
		else{
			this.health -= damage;

			return this.health;
		}


	}

	/** Kills the college body */
	public void death() {

		// Terminate method if already dead
		if(this.dead) return;

		// Gives instant money and score if the player decides to kill them
		GameScreen.collegeKilled();
		Currency.get().give(Currency.Type.POINTS, 250 );
		Currency.get().give(Currency.Type.GOLD, 500);
		this.dead = true;
		//this.body.setTransform(10000,10000, 2);
	}

	/** Types of college - allows us to keep track. */
	public enum Type {
		LANGWITH;
		private final UUID randomId;

		/** Assign static value at runtime, as value will not change and maximum of 1 college. */
		Type() {
			this.randomId = UUID.randomUUID();
		}

		/**
		 * Retrieves the unique id for the college object
		 *
		 * @return Unique identifier associated with this UUID.
		 */
		public UUID getId() {
			return randomId;
		}
	}

	public abstract boolean perform();
}
