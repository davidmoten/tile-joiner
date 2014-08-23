package com.github.davidmoten.tj;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class TileFactory {

	private static final Logger log = LoggerFactory
			.getLogger(TileFactory.class);

	public static final int TILE_SIZE = 256;
	private final String mapType;

	public TileFactory(String mapType) {
		this.mapType = mapType;
	}

	// https://mts1.google.com/vt/lyrs=m&x=1325&y=3143&z=13 -- normal
	// https://mts1.google.com/vt/lyrs=y&x=1325&y=3143&z=13 -- satellite
	// https://mts1.google.com/vt/lyrs=t&x=1325&y=3143&z=13 -- terrain

	public static class Coverage {
		private final List<TileUrl> tiles;
		private final int minIndexX;
		private final int maxIndexX;
		private final int minIndexY;
		private final int deltaX;
		private final int deltaY;
		private final int scaledTileSize;

		public Coverage(List<TileUrl> tiles, int deltaX, int deltaY,
				int scaledTileSize) {
			Preconditions.checkArgument(!tiles.isEmpty(), "tiles is empty!");
			this.tiles = tiles;
			this.deltaX = deltaX;
			this.deltaY = deltaY;
			this.scaledTileSize = scaledTileSize;
			this.minIndexX = getMinIndexX(tiles);
			this.maxIndexX = getMaxIndexX(tiles);
			this.minIndexY = getMinIndexY(tiles);
		}

		private static int getMaxIndexX(List<TileUrl> tiles) {
			Integer max = null;
			for (final TileUrl tile : tiles) {
				if (max == null || max < tile.getTile().getIndex().getX())
					max = tile.getTile().getIndex().getX();
			}
			return max;
		}

		private static int getMinIndexX(List<TileUrl> tiles) {
			Integer min = null;
			for (final TileUrl tile : tiles) {
				if (min == null || min > tile.getTile().getIndex().getX())
					min = tile.getTile().getIndex().getX();
			}
			return min;
		}

		private static int getMinIndexY(List<TileUrl> tiles) {
			Integer min = null;
			for (final TileUrl tile : tiles) {
				if (min == null || min > tile.getTile().getIndex().getY())
					min = tile.getTile().getIndex().getY();
			}
			return min;
		}

		public List<TileUrl> getTiles() {
			return tiles;
		}

		public int getMinIndexX() {
			return minIndexX;
		}

		public int getMaxIndexX() {
			return maxIndexX;
		}

		public int getMinIndexY() {
			return minIndexY;
		}

		public int getDeltaX() {
			return deltaX;
		}

		public int getDeltaY() {
			return deltaY;
		}

		public int getScaledTileSize() {
			return scaledTileSize;
		}

	}

	public Coverage getCoverage(double topLat, double leftLon, double rightLon,
			int width, int height) {

		final double diffLon;
		if (rightLon >= leftLon)
			diffLon = Math.abs(rightLon - leftLon);
		else
			diffLon = Math.abs(rightLon - leftLon + 360);

		final int zoom = calculateZoom(width, diffLon);

		final TileIndex index1 = getIndexFor(topLat, leftLon, zoom);
		final int xIndex2 = lonToTileIndexX(rightLon, zoom);

		final int leftIndexX = index1.getX();
		final int topIndexY = index1.getY();
		final int rightIndexX = xIndex2;

		final int deltaY = TileFactory.latToYInTile(topLat, zoom);
		final int deltaX = TileFactory.lonToXInTile(leftLon, zoom);
		final int deltaX2 = TileFactory.lonToXInTile(rightLon, zoom);

		final int virtualRightIndexX;
		if (leftIndexX <= rightIndexX)
			virtualRightIndexX = rightIndexX;
		else
			virtualRightIndexX = rightIndexX + pow2(zoom);

		final int tilesAcross = virtualRightIndexX - leftIndexX + 1;

		final int scaledTileSize = (int) Math
				.round((width)
						/ (tilesAcross - 1 - (double) deltaX / TILE_SIZE + (double) deltaX2
								/ TILE_SIZE));
		log.info("deltaX=" + deltaX + ",deltaX2=" + deltaX2 + ","
				+ "minIndexX=" + leftIndexX + ", maxIndexX=" + rightIndexX
				+ ",scaledTileSize=" + scaledTileSize);

		final int maxIndexY = topIndexY + height / scaledTileSize + 1;

		final List<Tile> tiles = new ArrayList<>();
		for (int x = leftIndexX; x <= virtualRightIndexX; x++)
			for (int y = topIndexY; y <= maxIndexY; y++) {
				tiles.add(new Tile(new TileIndex(x, y), zoom));
			}

		final List<TileUrl> tileUrls = new ArrayList<>();
		for (final Tile tile : tiles) {
			tileUrls.add(new TileUrl(tile, toUrl(tile, mapType)));
		}

		return new Coverage(tileUrls, deltaX, deltaY, scaledTileSize);
	}

	private static int pow2(int zoom) {
		return (int) Math.round(Math.pow(2, zoom));
	}

	private static int calculateZoom(int width, final double diffLon) {
		return (int) (Math.round(Math.floor(Math.log(360.0 * width / diffLon
				/ TILE_SIZE)
				/ Math.log(2))) + 1);
	}

	private static Optional<String> toUrl(Tile tile, String mapType) {
		if (tile.getIndex().getY() >= pow2(tile.getZoom()))
			return Optional.absent();
		else
			return Optional.of(String.format(
					"https://mts1.google.com/vt/lyrs=%s&x=%s&y=%s&z=%s",
					mapType, tile.getIndex().getX() % pow2(tile.getZoom()),
					tile.getIndex().getY(), tile.getZoom()));
	}

	static TileIndex getIndexFor(double lat, double lon, int zoom) {
		if (lat < -90 || lat > 90)
			throw new IllegalArgumentException("lat must be in range -90 to 90");

		final int tilex = lonToTileIndexX(lon, zoom);
		final int tiley = latToTileY(lat, zoom);

		return new TileIndex(tilex, tiley);
	}

	private static int lonToTileIndexX(double lon, int zoom) {
		if (lon < -180 || lon > 180)
			throw new IllegalArgumentException(
					"lon must be in range -180 to 180");

		// correct the longitude to go from 0 to 360
		lon = 180 + lon;

		// find tile size from zoom level
		final double longTileSize = 360 / (Math.pow(2, zoom));

		// find the tile coordinates
		return (int) Math.round(Math.floor(lon / longTileSize));
	}

	private static int latToTileY(double lat, int zoom) {
		final double y = latToY(lat, zoom);
		return (int) y / 256;
	}

	public static int latToYInTile(double lat, int zoom) {
		final double y = latToY(lat, zoom);
		return (int) Math.round(Math.floor(y - TILE_SIZE * ((int) y / 256)));
	}

	public static int lonToXInTile(double lon, int zoom) {
		final double longTileSize = 360 / (Math.pow(2, zoom));

		// find the tile coordinates
		return (int) Math.round(TILE_SIZE
				* (lon / longTileSize - Math.floor(lon / longTileSize)));
	}

	private static double latToY(double lat, int zoom) {
		Double exp = Math.sin(lat * Math.PI / 180);
		if (exp < -.9999)
			exp = -.9999;
		else if (exp > .9999)
			exp = .9999;
		final double y = (Math.round(TILE_SIZE * (Math.pow(2, zoom - 1))) + ((.5 * Math
				.log((1 + exp) / (1 - exp))) * ((-TILE_SIZE * (Math
				.pow(2, zoom))) / (2 * Math.PI))));
		return y;
	}

}
