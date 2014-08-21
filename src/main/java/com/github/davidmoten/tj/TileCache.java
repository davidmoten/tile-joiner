package com.github.davidmoten.tj;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

public class TileCache {

	private final File directory;

	public TileCache(File directory) {
		this.directory = directory;
	}

	public BufferedImage getImage(String url) {
		String key = "tile-joiner-"
				+ BaseEncoding.base64Url().encode(
						url.getBytes(Charsets.US_ASCII));
		File file = new File(directory, key);
		if (!file.exists()) {
			try {
				URL u = new URL(url);
				file.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				InputStream is = u.openStream();
				ByteStreams.copy(is, fos);
				fos.close();
				is.close();
			} catch (IOException e) {
				file.delete();
				throw new RuntimeException(e);
			}
		}
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
