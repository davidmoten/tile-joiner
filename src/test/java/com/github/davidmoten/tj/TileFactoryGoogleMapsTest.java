package com.github.davidmoten.tj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class TileFactoryGoogleMapsTest {

	@Test
	public void test() {
		final TileFactoryGoogleMaps g = new TileFactoryGoogleMaps();
		Collection<String> urls = g.getCoverage(-35, 149, -37, 150, 800, 600);
		for (final String url : urls) {
			System.out.println(url);
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
}
