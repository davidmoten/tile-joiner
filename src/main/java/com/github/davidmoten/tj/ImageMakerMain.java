package com.github.davidmoten.tj;

public class ImageMakerMain {

	public static void main(String[] args) {
		System.setProperty("https.proxyHost", "proxy.amsa.gov.au");
		System.setProperty("https.proxyPort", "8080");

		double lat1 = Double.parseDouble(args[0]);
		double lon1 = Double.parseDouble(args[1]);
		double lat2 = Double.parseDouble(args[2]);
		double lon2 = Double.parseDouble(args[3]);
		int width = Integer.parseInt(args[4]);
		int height = Integer.parseInt(args[5]);
		String filename = args[6];
		String imageFormat = args[7];
		String mapType = args[8];

		ImageMaker.createImage(lat1, lon1, lat2, lon2, width, height, filename,
				imageFormat, mapType);
	}

}
