package com.github.davidmoten.tj;

final class Tile {

	private final TileIndex index;
	private final Integer zoom;

	Tile(TileIndex index, Integer zoom) {
		this.index = index;
		this.zoom = zoom;
	}

	TileIndex getIndex() {
		return index;
	}

	Integer getZoom() {
		return zoom;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Tile [index=");
		builder.append(index);
		builder.append(", zoom=");
		builder.append(zoom);
		builder.append("]");
		return builder.toString();
	}

}
