package com.github.davidmoten.tj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

public class TileFactoryGoogleMapsTest {

	@Test
	public void test() {
		final TileFactoryGoogleMaps g = new TileFactoryGoogleMaps();
		Collection<TileUrl> tiles = g.getCoverage(-35, 149, -37, 150, 800, 600);
		Collection<String> urls = new HashSet<>();
		for (final TileUrl tile : tiles) {
			System.out.println(tile.getUrl());
			urls.add(tile.getUrl());
		}

		assertTrue(urls
				.contains("https://mts1.google.com/vt/lyrs=m&x=1871&y=1236&z=11"));
		assertTrue(urls
				.contains("https://mts1.google.com/vt/lyrs=m&x=1877&y=1250&z=11"));
	}

	@Test
	public void test2() {
		System.out.println(TileFactoryGoogleMaps.getIndexFor(10, -170, 0));
	}

	@Test
	public void test3() {
		final TileIndex t = TileFactoryGoogleMaps.getIndexFor(-35.3, 149.124,
				10);
		assertEquals(936, t.getX());
		assertEquals(619, t.getY());
	}

	@Test
	public void testDownload() {
		System.setProperty("https.proxyHost", "proxy.amsa.gov.au");
		System.setProperty("https.proxyPort", "8080");
		TileCache cache = new TileCache(new File("/tmp"));
		assertNotNull(cache
				.getImage("https://mts1.google.com/vt/lyrs=m&x=1871&y=1236&z=11"));
		final TileFactoryGoogleMaps g = new TileFactoryGoogleMaps();
		Collection<TileUrl> tiles = g.getCoverage(-35, 149, -37, 150, 800, 600);
		for (TileUrl tile : tiles)
			cache.getImage(tile.getUrl());
		new ImageMaker(-35, 149, -37, 150, 800, 600, cache).createImage(
				new File("target/test.png"), "PNG");

	}
}
