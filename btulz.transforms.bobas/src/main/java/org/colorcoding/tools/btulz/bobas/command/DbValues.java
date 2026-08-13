package org.colorcoding.tools.btulz.bobas.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.bobas.Environment;

@XmlRootElement(name = "DbValues", namespace = Environment.NAMESPACE_BTULZ_BOBAS)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DbValues", namespace = Environment.NAMESPACE_BTULZ_BOBAS)
@XmlSeeAlso({ DbValue.class })
public class DbValues implements Iterable<DbValue> {

	@XmlElement(name = "DbValue")
	private ArrayList<DbValue> values = new ArrayList<>();

	protected static DbValues create(String valueFile) {
		DbValues dbValues = new DbValues();
		dbValues.init(valueFile);
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
			this.setValues(unmarshaller.unmarshal(new StreamSource(inputStream), DbValues.class).getValue());
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

	@Override
	public Iterator<DbValue> iterator() {
		return this.values.iterator();
	}

	public boolean isEmpty() {
		return this.values.isEmpty();
	}

	public void add(DbValue value) {
		this.values.add(value);
	}

	protected void setValues(DbValue[] value) {
		this.values.clear();
		if (value != null) {
			for (DbValue item : value) {
				this.values.add(item);
			}
		}
	}

	protected void setValues(DbValues value) {
		this.values.clear();
		if (value != null) {
			for (DbValue item : value) {
				this.values.add(item);
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

	public DbValue() {
	}

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

	public DbValueItem() {
	}

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
