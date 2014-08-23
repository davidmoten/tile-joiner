package com.github.davidmoten.tj;

import com.google.common.base.Optional;

public class TileUrl {

	private final Tile tile;
	private final Optional<String> url;

	public TileUrl(Tile tile, Optional<String> url) {
		this.tile = tile;
		this.url = url;
	}

	public Tile getTile() {
		return tile;
	}

	public Optional<String> getUrl() {
		return url;
	}

}
