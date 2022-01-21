package net.shipsandgiggles.pirate.entity.impl.college;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.college.College;

import java.util.UUID;

public class LangwithCollege extends College {

	public LangwithCollege(UUID uuid, Type type, Texture texture, Location location, float maximumHealth, float height, float width) {
		super(UUID.randomUUID(), Type.LANGWITH, texture, location, maximumHealth, height, width);
	}

	@Override
	public boolean perform() {
		return false;
	}

	@Override
	public void draw(Batch batch) {

	}

	@Override
	public void death() {

	}
}
