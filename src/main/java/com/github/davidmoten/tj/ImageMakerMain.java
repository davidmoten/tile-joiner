package com.github.davidmoten.tj;

import java.io.File;
import java.util.Collection;

public class ImageMakerMain {

	public static void main(String[] args) {
		System.setProperty("https.proxyHost", "proxy.amsa.gov.au");
		System.setProperty("https.proxyPort", "8080");
		TileCache cache = new TileCache();
		final TileFactory g = new TileFactory();

		double lat1 = Double.parseDouble(args[0]);
		double lon1 = Double.parseDouble(args[1]);
		double lat2 = Double.parseDouble(args[2]);
		double lon2 = Double.parseDouble(args[3]);
		int width = Integer.parseInt(args[4]);
		int height = Integer.parseInt(args[5]);
		String filename = args[6];
		Collection<TileUrl> tiles = g.getCoverage(lat1, lon1, lat2, lon2,
				width, height);
		for (TileUrl tile : tiles)
			cache.getImage(tile.getUrl());
		new ImageMaker(lat1, lon1, lat2, lon2, width, height, cache)
				.createImage(new File(filename), "PNG");

	}

}
