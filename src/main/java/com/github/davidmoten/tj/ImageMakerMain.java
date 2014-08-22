package com.github.davidmoten.tj;

public class ImageMakerMain {

	public static void main(String[] args) {
		System.setProperty("https.proxyHost", "proxy.amsa.gov.au");
		System.setProperty("https.proxyPort", "8080");

		final double topLat = Double.parseDouble(args[0]);
		final double leftLon = Double.parseDouble(args[1]);
		final double rightLon = Double.parseDouble(args[2]);
		final int width = Integer.parseInt(args[3]);
		final int height = Integer.parseInt(args[4]);
		final String filename = args[5];
		final String imageFormat = args[6];
		final String mapType = args[7];

		ImageMaker.createImage(topLat, leftLon, rightLon, width, height, filename,
				imageFormat, mapType);
	}

}
