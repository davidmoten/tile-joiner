package com.github.davidmoten.tj;

public enum MapType {

	ROADS("r"), SATELLITE("s"), HYBRID("m"), TERRAIN("t");

	private String code;

	private MapType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
