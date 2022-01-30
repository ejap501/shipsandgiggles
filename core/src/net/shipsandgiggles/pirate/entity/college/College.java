package net.shipsandgiggles.pirate.entity.college;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;

import java.util.UUID;

/**
 * College data that allows us to perform animations / fights more easily.
 */
public abstract class College extends Entity {
	public Body body;
	public boolean dead = false;
	private final College.Type type;

	public College(UUID uuid, College.Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
		super(uuid, texture, location, EntityType.COLLEGE, maximumHealth, height, width);

		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public abstract boolean perform();

	public Body getBody(){
		return this.body;
	}
	public void death() {
		this.dead = true;
		//this.body.setTransform(10000,10000, 2);
	}


	/**
	 * Types of college - allows us to keep track.
	 */
	public enum Type {

		LANGWITH;

		private final UUID randomId;

		/**
		 * Assign static value at runtime, as value will not change and maximum of 1 college.
		 **/
		Type() {
			this.randomId = UUID.randomUUID();
		}

		/**
		 * @return Unique identifier associated with this UUID.
		 */
		public UUID getId() {
			return randomId;
		}
	}
}
