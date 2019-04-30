package org.colorcoding.tools.btulz.transformer.region.model;

import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.data.emDataSubType;
import org.colorcoding.tools.btulz.model.data.emDataType;
import org.colorcoding.tools.btulz.template.Parameter;

public class Property extends Entity implements IProperty {

	public Property(IProperty property) {
		this.entity = property;
	}

	private IProperty entity;

	@Override
	public String getName() {
		return this.entity.getName();
	}

	@Override
	public void setName(String name) {
		this.entity.setName(name);
	}

	@Override
	public String getDescription() {
		return this.entity.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.entity.setDescription(description);
	}

	@Override
	public void setDeclaredType(String declaredType) {
		this.entity.setDeclaredType(declaredType);
	}

	@Override
	public boolean isPrimaryKey() {
		return this.entity.isPrimaryKey();
	}

	public String isPrimaryKey(String type) {
		if (this.isPrimaryKey())
			return "Y";
		else
			return "N";
	}

	@Override
	public void setPrimaryKey(boolean value) {
		this.entity.setPrimaryKey(value);
	}

	@Override
	public boolean isUniqueKey() {
		return this.entity.isUniqueKey();
	}

	public String isUniqueKey(String type) {
		if (this.isUniqueKey())
			return "Y";
		else
			return "N";
	}

	@Override
	public void setUniqueKey(boolean value) {
		this.entity.setUniqueKey(value);
	}

	@Override
	public boolean isSearchKey() {
		return this.entity.isSearchKey();
	}

	@Override
	public void setSearchKey(boolean value) {
		this.entity.setSearchKey(value);
	}

	public String isSearched(String type) {
		if (this.isPrimaryKey() || this.isUniqueKey()) {
			return "Y";
		}
		if (this.isSearchKey()) {
			return "Y";
		}
		if (this.getName() != null) {
			if (this.getName().equalsIgnoreCase("Code")) {
				return "Y";
			} else if (this.getName().equalsIgnoreCase("Name")) {
				return "Y";
			}
		}
		return "N";
	}

	public String isSystemed() {
		if (this.getName() != null && this.getName().startsWith("U_")) {
			return "N";
		}
		return "Y";
	}

	@Override
	public emDataType getDataType() {
		return this.entity.getDataType();
	}

	@Override
	public void setDataType(emDataType dataType) {
		this.entity.setDataType(dataType);
	}

	@Override
	public emDataSubType getDataSubType() {
		return this.entity.getDataSubType();
	}

	@Override
	public void setDataSubType(emDataSubType dataSubType) {
		this.entity.setDataSubType(dataSubType);
	}

	@Override
	public int getEditSize() {
		return this.entity.getEditSize();
	}

	@Override
	public void setEditSize(int editSize) {
		this.entity.setEditSize(editSize);
	}

	@Override
	public String getMapped() {
		if (this.entity.getMapped() == null) {
			return this.getName();
		}
		return this.entity.getMapped();
	}

	@Override
	public String getDefaultValue() {
		return this.entity.getDefaultValue();
	}

	public String getDefaultValue(String type) {
		if ("DB".equalsIgnoreCase(type)) {
			if (this.entity.getDefaultValue() == null) {
				return "null";
			}
			if (this.getDataType() == emDataType.Numeric || this.getDataType() == emDataType.Decimal) {
				return this.entity.getDefaultValue();
			}
			return String.format("N'%s'", this.entity.getDefaultValue());
		}
		return this.getDefaultValue();
	}

	@Override
	public void setDefaultValue(String value) {
		this.entity.setDefaultValue(value);
	}

	@Override
	public void setMapped(String mapped) {
		this.entity.setMapped(mapped);
	}

	@Override
	public String getLinked() {
		return this.entity.getMapped();
	}

	@Override
	public void setLinked(String linked) {
		this.entity.setMapped(linked);
	}

	@Override
	public IProperty clone() {
		return (IProperty) super.clone();
	}

	private boolean last;

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	private List<DataTypeMapping> declaredTypeMappings;

	public List<DataTypeMapping> getDeclaredTypeMappings() {
		if (this.declaredTypeMappings == null) {
			this.declaredTypeMappings = new ArrayList<>();
		}
		return declaredTypeMappings;
	}

	public void addDeclaredTypeMappings(Iterable<?> value) {
		this.getDeclaredTypeMappings().clear();
		if (value != null) {
			for (Object item : value) {
				if (item instanceof DataTypeMapping) {
					this.getDeclaredTypeMappings().add((DataTypeMapping) item);
				}
			}
		}
	}

	public void addDeclaredTypeMappings(Parameter par) {
		if (par == null) {
			return;
		}
		Object value = par.getValue();
		if (Iterable.class.isInstance(value)) {
			this.addDeclaredTypeMappings((Iterable<?>) value);
		}
	}

	@Override
	public String getDeclaredType() {
		// 优先定义
		if (this.entity.getDeclaredType() != null) {
			// 已定义类型的转换
			for (DataTypeMapping mapping : this.getDeclaredTypeMappings()) {
				if (mapping == null) {
					continue;
				}
				if (!this.entity.getDeclaredType().equals(mapping.getDeclaredType())) {
					continue;
				}
				// 存在映射
				try {
					return mapping.getMapped(this);
				} catch (Exception e) {
					Environment.getLogger().error(e);
				}
			}
			return this.entity.getDeclaredType();
		}
		// 未发现定义使用映射
		for (DataTypeMapping mapping : this.getDeclaredTypeMappings()) {
			if (mapping == null) {
				continue;
			}
			if (mapping.getDataType() != this.getDataType()) {
				continue;
			}
			if (mapping.getSubType() != null && mapping.getSubType() != this.getDataSubType()) {
				continue;
			}
			// 存在映射
			try {
				return mapping.getMapped(this);
			} catch (Exception e) {
				Environment.getLogger().error(e);
			}
		}
		return this.entity.getDeclaredType();
	}

	private List<DataTypeMapping> mappedTypeMappings;

	public List<DataTypeMapping> getMappedTypeMappings() {
		if (this.mappedTypeMappings == null) {
			this.mappedTypeMappings = new ArrayList<>();
		}
		return mappedTypeMappings;
	}

	public void addMappedTypeMappings(Iterable<?> value) {
		this.getMappedTypeMappings().clear();
		if (value != null) {
			for (Object item : value) {
				if (item instanceof DataTypeMapping) {
					this.getMappedTypeMappings().add((DataTypeMapping) item);
				}
			}
		}
	}

	public void addMappedTypeMappings(Parameter par) {
		if (par == null) {
			return;
		}
		Object value = par.getValue();
		if (Iterable.class.isInstance(value)) {
			this.addMappedTypeMappings((Iterable<?>) value);
		}
	}

	public String getMappedType() throws Exception {
		// 优先使用映射
		for (DataTypeMapping mapping : this.getMappedTypeMappings()) {
			if (mapping == null) {
				continue;
			}
			if (mapping.getDataType() != this.getDataType()) {
				continue;
			}
			if (mapping.getSubType() != null && mapping.getSubType() != this.getDataSubType()) {
				continue;
			}
			// 存在映射
			return mapping.getMapped(this);
		}
		// 默认类型
		switch (this.getDataType()) {
		case Memo:
			if (this.getDataSubType() == emDataSubType.Default)
				return "ntext";
		case Numeric:
			return "int";
		case Date:
			if (this.getDataSubType() == emDataSubType.Default || this.getDataSubType() == emDataSubType.Date)
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
		if (this.isPrimaryKey() || this.isUniqueKey()) {
			return "not null";
		}
		return "null";
	}

	private String annotatedType;

	public String getAnnotatedType() {
		if (this.annotatedType == null) {
			switch (this.getDataType()) {
			case Alphanumeric:
				this.annotatedType = "ALPHANUMERIC";
				break;
			case Memo:
				this.annotatedType = "MEMO";
				break;
			case Numeric:
				this.annotatedType = "NUMERIC";
				break;
			case Date:
				if (this.getDataSubType() == emDataSubType.Default || this.getDataSubType() == emDataSubType.Date)
					this.annotatedType = "DATE";
				else if (this.getDataSubType() == emDataSubType.Time)
					this.annotatedType = "NUMERIC";
				break;
			case Decimal:
				this.annotatedType = "DECIMAL";
				break;
			case Bytes:
				this.annotatedType = "BYTES";
				break;
			case Boolean:
				this.annotatedType = "BOOLEAN";
				break;
			default:
				this.annotatedType = "UNKNOWN";
				break;
			}
		}
		return annotatedType;
	}

	public void setAnnotatedType(String annotatedType) {
		this.annotatedType = annotatedType;
	}

	public String getSeparator(String value) {
		return this.isLast() ? "" : value;
	}

	private List<TypeOutputMapping> typeOutputMappings;

	public final List<TypeOutputMapping> getTypeOutputMappings() {
		if (this.typeOutputMappings == null) {
			this.typeOutputMappings = new ArrayList<>();
		}
		return typeOutputMappings;
	}

	public void addTypeOutputMappings(Iterable<?> value) {
		this.getTypeOutputMappings().clear();
		if (value != null) {
			for (Object item : value) {
				if (item instanceof TypeOutputMapping) {
					this.getTypeOutputMappings().add((TypeOutputMapping) item);
				}
			}
		}
	}

	public void addTypeOutputMappings(Parameter par) {
		if (par == null) {
			return;
		}
		Object value = par.getValue();
		if (Iterable.class.isInstance(value)) {
			this.addTypeOutputMappings((Iterable<?>) value);
		}
	}

	public String getTypeOutput() throws Exception {
		return this.getTypeOutput(null);
	}

	public String getTypeOutput(String category) throws Exception {
		for (TypeOutputMapping item : this.getTypeOutputMappings()) {
			if (category != null) {
				if (!category.equalsIgnoreCase(item.getCategory())) {
					continue;
				}
			}
			if (this.entity.getDeclaredType() != null) {
				if (item.getDeclaredType() == null) {
					continue;
				} else if (this.entity.getDeclaredType().startsWith("em")) {
					if (!(this.entity.getDeclaredType().equalsIgnoreCase(item.getDeclaredType())
							|| "enum".equalsIgnoreCase(item.getDeclaredType()))) {
						continue;
					}
				} else if (!this.entity.getDeclaredType().equalsIgnoreCase(item.getDeclaredType())) {
					continue;
				}
			} else {
				if (item.getSubType() != null && item.getDataType() != null) {
					if (!(item.getDataType() == this.entity.getDataType()
							&& item.getSubType() == this.entity.getDataSubType())) {
						continue;
					}
				} else if (item.getDataType() != null) {
					if (item.getDataType() != this.entity.getDataType()) {
						continue;
					}
				} else {
					continue;
				}
			}
			return item.getOutput(this);
		}
		return "";
	}

	@Override
	public String toString() {
		return String.format("{property: %s %s}", this.getName(), this.getDataType());
	}

}
