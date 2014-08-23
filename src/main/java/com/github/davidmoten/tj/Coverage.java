package com.github.davidmoten.tj;

import java.util.List;

import com.google.common.base.Preconditions;

public class Coverage {
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
