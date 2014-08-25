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

public class ImageCreator {

	private static final Logger log = LoggerFactory
			.getLogger(ImageCreator.class);

	private final double topLat;
	private final double leftLon;
	private final double rightLon;
	private final int width;
	private final int height;
	private final String mapType;
	private final TileCache cache;
	private final Template service;

	public ImageCreator(double topLat, double leftLon, double rightLon,
			int widthPixels, int heightPixels, String mapType, TileCache cache,
			Template service) {
		this.topLat = topLat;
		this.leftLon = leftLon;
		this.rightLon = rightLon;
		this.width = widthPixels;
		this.height = heightPixels;
		this.mapType = mapType;
		this.cache = cache;
		this.service = service;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private double topLat;
		private double leftLon;
		private double rightLon;
		private int width;
		private int height;
		private String mapType;
		private TileCache cache = TileCache.instance();
		private File outputFile = new File("target/map.png");
		private String imageFormat = "PNG";
		private Template service = MapService.GOOGLE;

		private Builder() {
		}

		public Builder topLat(double topLat) {
			this.topLat = topLat;
			return this;
		}

		public Builder leftLon(double leftLon) {
			this.leftLon = leftLon;
			return this;
		}

		public Builder rightLon(double rightLon) {
			this.rightLon = rightLon;
			return this;
		}

		public Builder width(int width) {
			this.width = width;
			return this;
		}

		public Builder height(int height) {
			this.height = height;
			return this;
		}

		public Builder mapType(String mapType) {
			this.mapType = mapType;
			return this;
		}

		public Builder mapType(MapType mapType) {
			return mapType(mapType.getCode());
		}

		public Builder cache(TileCache cache) {
			this.cache = cache;
			return this;
		}

		public Builder outputFile(File file) {
			this.outputFile = file;
			return this;
		}

		public Builder outputFile(String filename) {
			return outputFile(new File(filename));
		}

		public Builder imageFormat(String imageFormat) {
			this.imageFormat = imageFormat;
			return this;
		}

		public Builder service(Template service) {
			this.service = service;
			return this;
		}

		/**
		 * The <code>template</code> parameter is a url template where {x} is
		 * replaced with the x index of the tile, {y} is replaced with the y
		 * index of the tile, {z} is replaced with the zoom value, {layers} is
		 * replaced with the layer name(s). See {@link MapService} for examples.
		 * 
		 * @param template
		 * @return
		 */
		public Builder service(final String template) {
			this.service = new Template() {
				@Override
				public String getTemplate() {
					return template;
				}
			};
			return this;
		}

		public void create() {
			new ImageCreator(topLat, leftLon, rightLon, width, height, mapType,
					cache, service).createImage(outputFile, imageFormat);
		}
	}

	public void createImage(File file, String formatName) {
		try {
			ImageIO.write(createImage(), formatName, file);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage createImage() {
		final BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D) image.getGraphics();
		drawMap(g);
		return image;
	}

	private void drawMap(Graphics2D g) {
		final Coverage coverage = new TileFactory(service, mapType)
				.getCoverage(topLat, leftLon, rightLon, width, height);

		final int deltaY = coverage.getDeltaY();
		final int deltaX = coverage.getDeltaX();
		final int scaledTileSize = coverage.getScaledTileSize();

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
			String imageFormat, Template service, String mapType) {
		new ImageCreator(topLat, leftLon, rightLon, width, height, mapType,
				TileCache.instance(), service).createImage(new File(filename),
				imageFormat);
	}
}
