package com.hanweb.complat.app;

import java.io.File;

/**
 * 附件bean
 * @author 李杰
 *
 */
public class Attach {
	private File file;
	
	private String name;
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	


}
