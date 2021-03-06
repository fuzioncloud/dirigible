package org.eclipse.dirigible.core.workspace.json;

import java.util.ArrayList;
import java.util.List;

public class ProjectDescriptor {

	private String name;

	private String path;

	private String type = "project";

	private List<FolderDescriptor> folders = new ArrayList<FolderDescriptor>();

	private List<FileDescriptor> files = new ArrayList<FileDescriptor>();

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

	public List<FolderDescriptor> getFolders() {
		return folders;
	}

	public void set(List<FolderDescriptor> folders) {
		this.folders = folders;
	}

	public List<FileDescriptor> getFiles() {
		return files;
	}

	public void setFiles(List<FileDescriptor> files) {
		this.files = files;
	}

}
