package com.github.davidmoten.tj;

public interface TileFactory {

	Tile getTileFor(double lat, double lon, double zoom);

}
