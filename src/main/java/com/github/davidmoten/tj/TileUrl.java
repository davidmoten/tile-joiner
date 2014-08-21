package com.github.davidmoten.tj;

public class TileUrl {

	private final Tile tile;
	private final String url;

	public TileUrl(Tile tile, String url) {
		this.tile = tile;
		this.url = url;
	}

	public Tile getTile() {
		return tile;
	}

	public String getUrl() {
		return url;
	}

}
