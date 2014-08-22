package com.github.davidmoten.tj;

import static com.github.davidmoten.tj.TileFactory.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.davidmoten.tj.TileFactory.Coverage;

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
		final Coverage coverage = new TileFactory(mapType).getCoverage(lat1,
				lon1, lon2, widthPixels, heightPixels);

		final int deltaY = coverage.getDeltaY();
		final int deltaX = coverage.getDeltaX();
		int scaledTileSize = coverage.getScaledTileSize();
		double scale = (double) scaledTileSize / TILE_SIZE;

		for (final TileUrl tile : coverage.getTiles()) {
			final BufferedImage img = cache.getImage(tile.getUrl());
			int scaledDeltaX = (int) Math.round((double) deltaX / TILE_SIZE
					* scaledTileSize);
			int scaledDeltaY = (int) Math.round((double) deltaY / TILE_SIZE
					* scaledTileSize);
			final int x = (tile.getTile().getIndex().getX() - coverage
					.getMinIndexX()) * scaledTileSize - scaledDeltaX;
			final int y = (tile.getTile().getIndex().getY() - coverage
					.getMinIndexY()) * scaledTileSize - scaledDeltaY;
			log.info("drawing image at {},{}", x, y);
			g.drawImage(img, x, y, scaledTileSize, scaledTileSize, null);

			// outline
			g.setColor(Color.black);
			// g.drawRect(x, y, scaledTileSize, scaledTileSize);
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
