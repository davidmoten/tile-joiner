package com.github.davidmoten.tj;

public enum MapService implements Template {
	GOOGLE("https://mts1.google.com/vt/lyrs={layers}&x={x}&y={y}&z={z}"), ARCGIS(
			"http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}.png"), OPENSTREETMAP(
			"http://c.tile.openstreetmap.org/{z}/{x}/{y}.png");

	private final String urlTemplate;

	private MapService(String urlTemplate) {
		this.urlTemplate = urlTemplate;
	}

	@Override
	public String getTemplate() {
		return urlTemplate;
	}

}
