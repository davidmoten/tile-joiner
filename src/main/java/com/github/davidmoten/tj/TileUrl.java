package com.github.davidmoten.tj;

import com.google.common.base.Optional;

class TileUrl {

	private final Tile tile;
	private final Optional<String> url;

	TileUrl(Tile tile, Optional<String> url) {
		this.tile = tile;
		this.url = url;
	}

	Tile getTile() {
		return tile;
	}

	Optional<String> getUrl() {
		return url;
	}

}
