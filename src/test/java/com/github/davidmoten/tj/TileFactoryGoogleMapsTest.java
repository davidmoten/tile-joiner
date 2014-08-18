package com.github.davidmoten.tj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TileFactoryGoogleMapsTest {

	@Test
	public void test() {
		final TileFactoryGoogleMaps g = new TileFactoryGoogleMaps();
		for (final String url : g.getCoverage(-35, 149, -37, 150, 800, 600)) {
			System.out.println(url);
		}
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
