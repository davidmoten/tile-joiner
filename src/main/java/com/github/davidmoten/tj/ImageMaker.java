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

	private final double topLat;
	private final double leftLon;
	private final double rightLon;
	private final int widthPixels;
	private final int heightPixels;
	private final TileCache cache;

	private final String mapType;

	public ImageMaker(double topLat, double leftLon, double rightLon,
			int widthPixels, int heightPixels, String mapType, TileCache cache) {
		this.topLat = topLat;
		this.leftLon = leftLon;
		this.rightLon = rightLon;
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
		final Coverage coverage = new TileFactory(mapType).getCoverage(topLat,
				leftLon, rightLon, widthPixels, heightPixels);

		final int deltaY = coverage.getDeltaY();
		final int deltaX = coverage.getDeltaX();
		final int scaledTileSize = coverage.getScaledTileSize();
		final double scale = (double) scaledTileSize / TILE_SIZE;

		for (final TileUrl tile : coverage.getTiles()) {
			if (tile.getUrl().isPresent()) {
				final BufferedImage img = cache.getImage(tile.getUrl().get());
				final int scaledDeltaX = (int) Math.round((double) deltaX
						/ TILE_SIZE * scaledTileSize);
				final int scaledDeltaY = (int) Math.round((double) deltaY
						/ TILE_SIZE * scaledTileSize);
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
	}

	public static void createImage(double topLat, double leftLon,
			double rightLon, int width, int height, String filename,
			String imageFormat, String mapType) {
		new ImageMaker(topLat, leftLon, rightLon, width, height, mapType,
				TileCache.instance()).createImage(new File(filename),
				imageFormat);
	}
}
