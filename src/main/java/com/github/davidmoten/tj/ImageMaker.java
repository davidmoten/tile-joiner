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
	private final double lat2;
	private final double lon2;
	private final int widthPixels;
	private final int heightPixels;
	private final TileCache cache;

	public ImageMaker(double lat1, double lon1, double lat2, double lon2,
			int widthPixels, int heightPixels, TileCache cache) {
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
		this.widthPixels = widthPixels;
		this.heightPixels = heightPixels;
		this.cache = cache;
	}

	public void createImage(File file, String formatName) {
		try {
			ImageIO.write(createImage(), formatName, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage createImage() {
		BufferedImage image = new BufferedImage(widthPixels, heightPixels,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		drawMap(g);
		return image;
	}

	private void drawMap(Graphics2D g) {
		List<TileUrl> tiles = new TileFactoryGoogleMaps().getCoverage(lat1,
				lon1, lat2, lon2, widthPixels, heightPixels);
		TileUrl first = tiles.get(0);
		for (TileUrl tile : tiles) {
			BufferedImage img = cache.getImage(tile.getUrl());
			int x = (tile.getTile().getIndex().getX() - first.getTile()
					.getIndex().getX())
					* TileFactoryGoogleMaps.TILE_SIZE;
			int y = (tile.getTile().getIndex().getY() - first.getTile()
					.getIndex().getY())
					* TileFactoryGoogleMaps.TILE_SIZE;
			log.info("drawing image at {},{}", x, y);
			g.drawImage(img, x, y, null);
		}
	}
}
