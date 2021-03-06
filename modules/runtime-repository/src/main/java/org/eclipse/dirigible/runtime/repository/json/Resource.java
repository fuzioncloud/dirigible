package org.eclipse.dirigible.runtime.repository.json;

public class Resource {

	private static final String TYPE_RESOURCE = "resource";

	private String name;

	private String path;

	private String contentType;

	private String type = TYPE_RESOURCE;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getType() {
		return type;
	}

}
