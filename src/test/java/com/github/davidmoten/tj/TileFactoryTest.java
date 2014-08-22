package com.github.davidmoten.tj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

public class TileFactoryTest {

	@Test
	public void test() {
		final TileFactory g = new TileFactory("m");
		final Collection<TileUrl> tiles = g
				.getCoverage(-35, 149, 150, 800, 600).getTiles();
		final Collection<String> urls = new HashSet<>();
		for (final TileUrl tile : tiles) {
			System.out.println(tile.getUrl());
			urls.add(tile.getUrl());
		}

		assertTrue(urls
				.contains("https://mts1.google.com/vt/lyrs=m&x=1871&y=1236&z=11"));
		assertTrue(urls
				.contains("https://mts1.google.com/vt/lyrs=m&x=1877&y=1239&z=11"));
	}

	@Test
	public void test2() {
		System.out.println(TileFactory.getIndexFor(10, -170, 0));
	}

	@Test
	public void test3() {
		final TileIndex t = TileFactory.getIndexFor(-35.3, 149.124, 10);
		assertEquals(936, t.getX());
		assertEquals(619, t.getY());
	}

}
