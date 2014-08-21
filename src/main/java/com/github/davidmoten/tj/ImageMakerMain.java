package com.github.davidmoten.tj;

import java.io.File;
import java.util.Collection;

public class ImageMakerMain {

	public static void main(String[] args) {
		System.setProperty("https.proxyHost", "proxy.amsa.gov.au");
		System.setProperty("https.proxyPort", "8080");
		TileCache cache = new TileCache();
		final TileFactory g = new TileFactory();
		Collection<TileUrl> tiles = g.getCoverage(-35, 149, -37, 150, 800, 600);
		for (TileUrl tile : tiles)
			cache.getImage(tile.getUrl());
		new ImageMaker(-35, 149, -37, 150, 800, 600, cache).createImage(
				new File("target/test.png"), "PNG");

	}

}
