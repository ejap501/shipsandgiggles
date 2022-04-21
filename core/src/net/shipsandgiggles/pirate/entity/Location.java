package net.shipsandgiggles.pirate.entity;

/**
 * Location
 * Creates class for locaiton instead of always using position
 *
 * @author Team 23
 * @version 1.0
 */
public class Location {
	// Coordinate data store
	private float xCoordinate;
	private float yCoordinate;

	/**
	 * Instantiates position
	 * Uses X, Y coordinates
	 *
	 * @param xCoordinate : X position in world
	 * @param yCoordinate : Y position in world
	 */
	public Location(float xCoordinate, float yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	/**
	 * @return X position in world
	 */
	public float getX() {
		return xCoordinate;
	}

	/**
	 * Sets X position in world
	 *
	 * @param xCoordinate : X position in world
	 */
	public void setX(float xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * @return Y position in world
	 */
	public float getY() {
		return yCoordinate;
	}

	/**
	 * Sets Y position in world
	 *
	 * @param yCoordinate : Y position in world
	 */
	public void setY(float yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
}