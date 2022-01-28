package net.shipsandgiggles.pirate.cache.impl;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.cache.Cache;
import net.shipsandgiggles.pirate.entity.type.CannonBall;

import java.util.UUID;

public class BallHandler {

	private static BallHandler INSTANCE;

	private final Cache<UUID, CannonBall> cache;

	private BallHandler() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Balls Cache is being duplicated!");
		}

		this.cache = new Cache<>(null);
	}

	public static BallHandler get() {
		if (INSTANCE == null) {
			INSTANCE = new BallHandler();
		}

		return INSTANCE;
	}

	public void createBall(World world, Vector2 position, Vector2 target, Sprite texture, short categoryBits, short maskBit, short groupIndex) {

		CannonBall cannonBall = new CannonBall(texture, position, target, world, categoryBits, maskBit, groupIndex);
		this.cache.cache(cannonBall.getUniqueId(), cannonBall);
	}

	public void createBallAtAngle(World world, Vector2 position, float angle, Sprite texture, short categoryBits, short maskBit, short groupIndex) {
		CannonBall cannonBall = new CannonBall(texture, position, angle, world, categoryBits, maskBit, groupIndex);
		this.cache.cache(cannonBall.getUniqueId(), cannonBall);
	}

	public void remove(UUID uuid) {
		this.cache.remove(uuid);
	}

	public void updateBalls(Batch batch) {
		this.cache.all().forEach(ball -> ball.update(batch));
	}
}