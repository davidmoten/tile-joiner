package com.github.davidmoten.tj;

import java.util.Collection;

public interface TileFactory {

	Collection<Tile> getCoverage(double lat1, double lon1, double lat2,
			double lon2, long diffx, long diffY);
}
