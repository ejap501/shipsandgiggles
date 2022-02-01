package net.shipsandgiggles.pirate.entity.impl.college;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.cache.impl.BallHandler;
import net.shipsandgiggles.pirate.entity.college.College;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class LangwithCollege extends College {
	public World world;


	public LangwithCollege(Sprite texture, Location location, float maximumHealth, World world) {
		super(UUID.randomUUID(), Type.LANGWITH, texture, location, maximumHealth, texture.getHeight(), texture.getWidth());

		Body body;
		BodyDef def = new BodyDef();

		def.type = BodyDef.BodyType.StaticBody;

		def.position.set(location.getX(), location.getY());

		def.fixedRotation = true;
		body = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef. density = 1f;
		fixtureDef.filter.categoryBits = Configuration.CAT_COLLEGE;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();
		this.body = body;
		this.cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
		this.world = world;

		this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX(),(int)location.getY(), 700,400);


	}

	@Override
	public boolean perform() {
		return false;
	}

	@Override
	public void draw(Batch batch) {
	if(dead){
		return;
	}
		this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f));
		this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
		batch.begin();

		this.getSkin().draw(batch);
		batch.end();

	}

	@Override
	public void shootPlayer(Ship player) {
		if(this.hitBox.overlaps(player.hitBox) && timer <= 0 && !this.dead) {
			BallHandler.get().createBall(this.world, new Vector2(this.body.getPosition().x, this.body.getPosition().y), new Vector2(player.getBody().getPosition().x, player.getBody().getPosition().y), cannonBallSprite, (short)(Configuration.CAT_ENEMY | Configuration.CAT_COLLEGE), Configuration.CAT_PLAYER, (short) 0);
			this.timer = this.cooldownTimer;
		}
		else if(timer <= 0) this.timer = 0;
		else this.timer -= Gdx.graphics.getDeltaTime();
	}


}
