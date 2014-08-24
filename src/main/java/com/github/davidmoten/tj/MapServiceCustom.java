package com.github.davidmoten.tj;

public class MapServiceCustom implements Template {

	private final String template;

	public MapServiceCustom(String template) {
		this.template = template;
	}

	@Override
	public String getTemplate() {
		return template;
	}

}
