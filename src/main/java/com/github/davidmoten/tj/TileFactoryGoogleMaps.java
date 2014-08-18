package com.github.davidmoten.tj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TileFactoryGoogleMaps {

	// https://mts1.google.com/vt/lyrs=m&x=1325&y=3143&z=13 -- normal
	// https://mts1.google.com/vt/lyrs=y&x=1325&y=3143&z=13 -- satellite
	// https://mts1.google.com/vt/lyrs=t&x=1325&y=3143&z=13 -- terrain

	public Collection<String> getCoverage(double lat1, double lon1,
			double lat2, double lon2, long diffX, long diffY) {
		final long tileSize = 256;
		final double diffLat = Math.abs(lat1 - lat2);
		final double diffLon = Math.abs(lon1 - lon2);
		final int zoom;
		if (diffLat < diffLon) {
			// TODO this not right because of mercator projection

			// find biggest n>=0 such that (180/2^n) <
			// diffLat*tileSize/diffY
			// i.e 2^n >180 * diffY/(diffLat*tileSize)
			// of n> ln(180*diffY/diffLat/tileSize)/ln(2)
			zoom = (int) (Math.round(Math.floor(Math.log(180.0 * diffY
					/ diffLat / tileSize)
					/ Math.log(2))) + 1);
			throw new RuntimeException("not ready");
		} else {
			zoom = (int) (Math.round(Math.floor(Math.log(360.0 * diffX
					/ diffLon / tileSize)
					/ Math.log(2))) + 1);
		}

		final TileIndex index1 = getIndexFor(lat1, lon1, zoom);
		final TileIndex index2 = getIndexFor(lat2, lon2, zoom);
		final int minIndexX = Math.min(index1.getX(), index2.getX());
		final int minIndexY = Math.min(index1.getY(), index2.getY());
		final int maxIndexX = Math.max(index1.getX(), index2.getX());
		final int maxIndexY = Math.max(index1.getY(), index2.getY());

		final List<Tile> tiles = new ArrayList<>();
		for (int x = minIndexX; x <= maxIndexX; x++)
			for (int y = minIndexY; y <= maxIndexY; y++) {
				tiles.add(new Tile(new TileIndex(x, y), zoom));
			}
		final List<String> result = new ArrayList<>();
		for (final Tile tile : tiles) {
			result.add(toUrl(tile));
		}
		return result;
	}

	private static String toUrl(Tile tile) {
		return String.format(
				"https://mts1.google.com/vt/lyrs=m&x=%s&y=%s3&z=%s", tile
						.getIndex().getX(), tile.getIndex().getY(), tile
						.getZoom());
	}

	static TileIndex getIndexFor(double lat, double lon, int zoom) {
		if (lat < -90 || lat > 90)
			throw new IllegalArgumentException("lat must be in range -90 to 90");
		if (lon < -180 || lon > 180)
			throw new IllegalArgumentException(
					"lon must be in range -180 to 180");

		// correct the longitude to go from 0 to 360
		lon = 180 + lon;

		// find tile size from zoom level
		final double longTileSize = 360 / (Math.pow(2, zoom));

		// find the tile coordinates
		final int tilex = (int) Math.round(Math.floor(lon / longTileSize));
		final int tiley = latToTileY(lat, zoom);

		return new TileIndex(tilex, tiley);
	}

	private static int latToTileY(double lat, int zoom) {
		Double exp = Math.sin(lat * Math.PI / 180);
		if (exp < -.9999)
			exp = -.9999;
		if (exp > .9999)
			exp = .9999;
		return (int) (Math.round(256 * (Math.pow(2, zoom - 1))) + ((.5 * Math
				.log((1 + exp) / (1 - exp))) * ((-256 * (Math.pow(2, zoom))) / (2 * Math.PI)))) / 256;
	}

}
