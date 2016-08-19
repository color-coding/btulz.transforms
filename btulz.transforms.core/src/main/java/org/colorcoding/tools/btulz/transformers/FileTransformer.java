package org.colorcoding.tools.btulz.transformers;

import org.colorcoding.tools.btulz.models.IDomain;

public abstract class FileTransformer extends Transformer implements IFileTransformer {

	private boolean groupingFile;

	public boolean isGroupingFile() {
		return this.groupingFile;
	}

	public void setGroupingFile(boolean value) {
		this.groupingFile = value;
	}

	protected void clearResults() {
		if (!this.isKeepResults()) {

		}
	}

	public void input(String filePath) {
		// TODO Auto-generated method stub

	}

	public void input(String[] filePathes) {
		// TODO Auto-generated method stub

	}

	public void input(IDomain domain) {
		// TODO Auto-generated method stub

	}

	public void transform() {
		// TODO Auto-generated method stub

	}

	public String getWorkFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWorkFolder(String foder) {
		// TODO Auto-generated method stub

	}

	public IDomain[] getDomainModels() {
		// TODO Auto-generated method stub
		return null;
	}

}
