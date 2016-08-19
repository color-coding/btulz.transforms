package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.models.data.emPropertyType;
import org.colorcoding.tools.btulz.models.data.emYesNo;

public class PropertyData extends Property implements IPropertyData {

	private emYesNo primaryKey;

	
	public emYesNo isPrimaryKey() {
		if (this.primaryKey == null) {
			this.primaryKey = emYesNo.No;
		}
		return this.primaryKey;
	}

	
	public void setPrimaryKey(emYesNo value) {
		this.primaryKey = value;
	}

	private emYesNo uniqueKey;

	
	public emYesNo isUniqueKey() {
		if (this.uniqueKey == null) {
			this.uniqueKey = emYesNo.No;
		}
		return this.uniqueKey;
	}

	
	public void setUniqueKey(emYesNo value) {
		this.uniqueKey = value;
	}

	private emDataType dataType;

	
	public emDataType getDataType() {
		if (this.dataType == null) {
			this.dataType = emDataType.dt_Alphanumeric;
		}
		return this.dataType;
	}

	
	public void setDataType(emDataType dataType) {
		this.dataType = dataType;
	}

	private emDataSubType dataSubType;

	
	public emDataSubType getDataSubType() {
		if (this.dataSubType == null) {
			this.dataSubType = emDataSubType.st_None;
		}
		return this.dataSubType;
	}

	
	public void setDataSubType(emDataSubType dataSubType) {
		this.dataSubType = dataSubType;
	}

	private int editSize;

	
	public int getEditSize() {
		return this.editSize;
	}

	
	public void setEditSize(int editSize) {
		this.editSize = editSize;
	}

	private String mapped;

	
	public String getMapped() {
		return this.mapped;
	}

	
	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	
	public emPropertyType getPropertyType() {
		return emPropertyType.pt_Data;
	}

}
