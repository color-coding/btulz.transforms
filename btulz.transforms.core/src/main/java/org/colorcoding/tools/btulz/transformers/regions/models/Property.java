package org.colorcoding.tools.btulz.transformers.regions.models;

import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.models.data.emYesNo;
import org.colorcoding.tools.btulz.templates.Parameter;

public class Property implements IProperty {

	public Property(org.colorcoding.tools.btulz.models.IProperty property) {
		this.property = property;
	}

	private IProperty property;

	@Override
	public String getName() {
		return this.property.getName();
	}

	@Override
	public void setName(String name) {
		this.property.setName(name);
	}

	@Override
	public String getDescription() {
		return this.property.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.property.setDescription(description);
	}

	@Override
	public String getDeclaredType() {
		return this.property.getDeclaredType();
	}

	@Override
	public void setDeclaredType(String declaredType) {
		this.property.setDeclaredType(declaredType);
	}

	@Override
	public emYesNo isPrimaryKey() {
		return this.property.isPrimaryKey();
	}

	@Override
	public void setPrimaryKey(emYesNo value) {
		this.property.setPrimaryKey(value);
	}

	@Override
	public emYesNo isUniqueKey() {
		return this.property.isUniqueKey();
	}

	@Override
	public void setUniqueKey(emYesNo value) {
		this.property.setUniqueKey(value);
	}

	@Override
	public emDataType getDataType() {
		return this.property.getDataType();
	}

	@Override
	public void setDataType(emDataType dataType) {
		this.property.setDataType(dataType);
	}

	@Override
	public emDataSubType getDataSubType() {
		return this.property.getDataSubType();
	}

	@Override
	public void setDataSubType(emDataSubType dataSubType) {
		this.property.setDataSubType(dataSubType);
	}

	@Override
	public int getEditSize() {
		return this.property.getEditSize();
	}

	@Override
	public void setEditSize(int editSize) {
		this.property.setEditSize(editSize);
	}

	@Override
	public String getMapped() {
		return this.property.getMapped();
	}

	@Override
	public void setMapped(String mapped) {
		this.property.setMapped(mapped);
	}

	@Override
	public IProperty clone() {
		return null;
	}

	private boolean last;

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public String toString() {
		return String.format("RegionProperty %s", this.getName());
	}

	private List<DataTypeMapping> dataTypeMappings;

	public List<DataTypeMapping> getDataTypeMappings() {
		if (this.dataTypeMappings == null) {
			this.dataTypeMappings = new ArrayList<>();
		}
		return dataTypeMappings;
	}

	public void addDataTypeMappings(Iterable<?> value) {
		this.getDataTypeMappings().clear();
		if (value != null) {
			for (Object item : value) {
				if (item instanceof DataTypeMapping) {
					this.getDataTypeMappings().add((DataTypeMapping) item);
				}
			}
		}
	}

	public void addDataTypeMappings(Parameter par) {
		if (par == null) {
			return;
		}
		Object value = par.getValue();
		if (Iterable.class.isInstance(value)) {
			this.addDataTypeMappings((Iterable<?>) value);
		}
	}

	public String getMappedType() throws Exception {
		// 优先使用映射
		for (DataTypeMapping mapping : this.getDataTypeMappings()) {
			if (mapping == null) {
				continue;
			}
			if (mapping.getDateType() != this.getDataType()) {
				continue;
			}
			if (mapping.getSubType() != this.getDataSubType()) {
				continue;
			}
			// 存在映射
			return mapping.getMappedType(this);
		}
		// 默认类型
		switch (this.getDataType()) {
		case Memo:
			if (this.getDataSubType() == emDataSubType.Default)
				return "ntext";
		case Numeric:
			return "int";
		case Date:
			if (this.getDataSubType() == emDataSubType.Default)
				return "datetime";
			else if (this.getDataSubType() == emDataSubType.Time)
				return "smallint";
		case Decimal:
			return "numeric(19, 6)";
		case Bytes:
			return "bit";
		default:
			break;
		}
		return String.format("nvarchar(%s)", this.getEditSize());
	}

	public String getNullType() {
		if (this.isPrimaryKey() == emYesNo.Yes) {
			return "not null";
		}
		return "null";
	}

	public String getSeparator(String value) {
		return this.isLast() ? "" : value;
	}
}
