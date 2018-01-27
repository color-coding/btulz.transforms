package org.colorcoding.tools.btulz.bobas.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.bobas.Environment;

@XmlRootElement(name = "DbValues", namespace = Environment.NAMESPACE_BTULZ_BOBAS)
@XmlType(name = "DbValues", namespace = Environment.NAMESPACE_BTULZ_BOBAS)
@XmlSeeAlso({ DbValue.class })
public class DbValues extends ArrayList<DbValue> {

	private static final long serialVersionUID = -1684584523309129310L;

	private volatile static DbValues dbValues;

	protected synchronized static DbValues create(String valueFile) {
		if (dbValues == null) {
			synchronized (DbValues.class) {
				if (dbValues == null) {
					dbValues = new DbValues();
					dbValues.init(valueFile);
				}
			}
		}
		return dbValues;
	}

	public void init(String valueFile) {
		InputStream inputStream = null;
		try {
			if (valueFile != null && !valueFile.isEmpty()) {
				inputStream = new FileInputStream(new File(valueFile));
			}
			if (inputStream == null) {
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db_values.xml");
			}
			JAXBContext context = JAXBContext.newInstance(DbValues.class, DbValue.class, DbValueItem.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			this.setValues((DbValues) unmarshaller.unmarshal(inputStream));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	@XmlElement(name = "DbValue")
	protected DbValue[] getValues() {
		return this.toArray(new DbValue[] {});
	}

	protected void setValues(DbValue[] value) {
		this.clear();
		if (value != null) {
			for (DbValue item : value) {
				this.add(item);
			}
		}
	}

	protected void setValues(DbValues value) {
		this.clear();
		if (value != null) {
			for (DbValue item : value) {
				this.add(item);
			}
		}
	}

	public String getValue(String type, String key) {
		for (DbValue dbValue : this) {
			if (dbValue.getName().equalsIgnoreCase(type)) {
				for (DbValueItem item : dbValue.getItems()) {
					if (item.getKey().equalsIgnoreCase(key)) {
						return item.getValue();
					}
				}
			}
		}
		throw new RuntimeException(String.format("unresolved value [%s - %s].", type, key));
	}
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "DbValue", namespace = Environment.NAMESPACE_BTULZ_BOBAS)
@XmlSeeAlso({ DbValueItem.class })
class DbValue {

	@XmlAttribute(name = "Name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "DbValueItem")
	private ArrayList<DbValueItem> items;

	public ArrayList<DbValueItem> getItems() {
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		return items;
	}

	public void setItems(ArrayList<DbValueItem> items) {
		this.items = items;
	}

}

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "DbValueItem", namespace = Environment.NAMESPACE_BTULZ_BOBAS)
class DbValueItem {

	@XmlAttribute(name = "Key")
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@XmlAttribute(name = "Value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}