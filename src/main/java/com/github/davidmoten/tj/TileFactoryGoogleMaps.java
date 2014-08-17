package com.github.davidmoten.tj;

public class TileFactoryGoogleMaps implements TileFactory {

	@Override
	public Tile getTileFor(double lat, double lon, double zoom) {
		if (lat < -90 || lat > 90)
			throw new IllegalArgumentException("lat must be in range -90 to 90");
		if (lon < -180 || lon > 180)
			throw new IllegalArgumentException(
					"lon must be in range -180 to 180");

		// correct the latitude to go from 0 (north) to 180 (south),
		// instead of 90(north) to -90(south)
		lat = 90 - lat;

		// correct the longitude to go from 0 to 360
		lon = 180 + lon;

		// find tile size from zoom level
		double latTileSize = 180 / (Math.pow(2, (17 - zoom)));
		double longTileSize = 360 / (Math.pow(2, (17 - zoom)));

		// find the tile coordinates
		int tilex = (int) (lon / longTileSize);
		int tiley = (int) (lat / latTileSize);

		TileIndex tileIndex = new TileIndex(tilex, tiley);

		return null;
	}
}
