package net.shipsandgiggles.pirate;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.maps.objects.PolylineMapObject;

import net.shipsandgiggles.pirate.conf.Configuration;

/**
 * Tiled Object Util
 * Dynamic body creation of the map
 * Used to create colliders that the player interacts with so the player doesn't drive outside the map
 *
 * @author Team 23
 * @version 1.0
 */
public class TiledObjectUtil {
	/**
	 * Applies tile objects
	 *
	 * @param world : World data
	 * @param objects : World objects
	 */
	public static void parseTiledObjectLayer(World world, MapObjects objects) {
		// Checks for map object
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
			fixtureDef. density = 1f;
			fixtureDef.filter.categoryBits = Configuration.Cat_walls;
			body.createFixture(fixtureDef);
			shape.dispose();
		}
	}

	/**
	 * Creates a new polyline
	 * Creates the lines that are used in the Tiled application
	 *
	 * @param polyline : Constructed line between determined points
	 * @return The chain shape containing a polyline
	 */
	private static ChainShape createPolyLine(PolylineMapObject polyline) {
		// Gets the data used to create each line
		float[] vertices = polyline.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		// Creates the chainshape and returns it
		for (int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = new Vector2(vertices[i * 2] / Configuration.PIXEL_PER_METER, vertices[i * 2 + 1] / Configuration.PIXEL_PER_METER);
		}
		ChainShape cs = new ChainShape();
		cs.createChain(worldVertices);
		return cs;
	}
}
