package com.github.davidmoten.tj;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageMaker {

	private static final Logger log = LoggerFactory.getLogger(ImageMaker.class);

	private final double lat1;
	private final double lon1;
	private final double lon2;
	private final int widthPixels;
	private final int heightPixels;
	private final TileCache cache;

	private final String mapType;

	public ImageMaker(double lat1, double lon1, double lon2, int widthPixels,
			int heightPixels, String mapType, TileCache cache) {
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lon2 = lon2;
		this.widthPixels = widthPixels;
		this.heightPixels = heightPixels;
		this.mapType = mapType;
		this.cache = cache;
	}

	public void createImage(File file, String formatName) {
		try {
			ImageIO.write(createImage(), formatName, file);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage createImage() {
		final BufferedImage image = new BufferedImage(widthPixels,
				heightPixels, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D) image.getGraphics();
		drawMap(g);
		return image;
	}

	private void drawMap(Graphics2D g) {
		final List<TileUrl> tiles = new TileFactory(mapType).getCoverage(lat1,
				lon1, lon2, widthPixels, heightPixels);

		final TileUrl first = tiles.get(0);

		final int zoom = first.getTile().getZoom();
		final int deltaY = TileFactory.latToYInTile(lat1, zoom);
		final int deltaX = TileFactory.longToXInTile(lon1, zoom);

		for (final TileUrl tile : tiles) {
			final BufferedImage img = cache.getImage(tile.getUrl());
			final int x = (tile.getTile().getIndex().getX() - first.getTile()
					.getIndex().getX())
					* TileFactory.TILE_SIZE - deltaX;
			final int y = (tile.getTile().getIndex().getY() - first.getTile()
					.getIndex().getY())
					* TileFactory.TILE_SIZE - deltaY;
			log.info("drawing image at {},{}", x, y);
			g.drawImage(img, x, y, null);
		}
	}

	public static void createImage(double lat1, double lon1, double lon2,
			int width, int height, String filename, String imageFormat,
			String mapType) {
		new ImageMaker(lat1, lon1, lon2, width, height, mapType,
				TileCache.instance()).createImage(new File(filename),
				imageFormat);
	}
}
