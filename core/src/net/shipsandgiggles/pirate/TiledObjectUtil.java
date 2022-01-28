package net.shipsandgiggles.pirate;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;

public class TiledObjectUtil {

	public static void parseTiledObjectLayer(World world, MapObjects objects) {

		for (MapObject object : objects) {
			if (!(object instanceof PolylineMapObject)) {
				continue;
			}

			Shape shape = createPolyLine((PolylineMapObject) object);
			BodyDef def = new BodyDef();
			def.type = BodyDef.BodyType.StaticBody;

			Body body = world.createBody(def);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = 1f;
			fixtureDef.filter.categoryBits = Configuration.CAT_ENEMY;
			body.createFixture(fixtureDef);
			shape.dispose();
		}
	}

	private static ChainShape createPolyLine(PolylineMapObject polyline) {
		float[] vertices = polyline.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = new Vector2(vertices[i * 2] / Configuration.PIXEL_PER_METER, vertices[i * 2 + 1] / Configuration.PIXEL_PER_METER);
		}

		ChainShape cs = new ChainShape();
		cs.createChain(worldVertices);
		return cs;
	}
}
