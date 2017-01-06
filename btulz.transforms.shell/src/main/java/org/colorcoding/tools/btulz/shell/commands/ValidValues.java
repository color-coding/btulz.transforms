package org.colorcoding.tools.btulz.shell.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 项目的有效值集合
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ValidValues", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class ValidValues implements Iterable<ValidValue> {
	/**
	 * 取值类型
	 */
	@XmlAttribute(name = "ClassName")
	private String className;

	public final String getClassName() {
		return className;
	}

	public final void setClassName(String className) {
		this.className = className;
	}

	private List<ValidValue> values;

	public List<ValidValue> getValues() {
		if (this.values == null) {
			this.values = new ArrayList<>();
		}
		return this.values;
	}

	@XmlElement(name = "ValidValue", type = ValidValue.class)
	List<ValidValue> getValuesProxy() {
		if (this.getClassName() != null) {
			return null;
		}
		if (this.getClassName() != null && !this.getClassName().isEmpty()) {
			return null;
		}
		return this.getValues();
	}

	void getValuesProxy(List<ValidValue> values) {
		this.values = values;
	}

	/**
	 * 初始化下
	 */
	public void get() {
		if (this.getValues().size() > 0) {
			// 存在值，则表示已经初始化过
			return;
		}
		if (this.getClassName() == null || this.getClassName().isEmpty()) {
			return;
		}
		try {
			Class<?> type = Class.forName(this.getClassName());
			if (type == null) {
				return;
			}
			if (type.isEnum()) {
				// 枚举类型
				for (Object item : type.getEnumConstants()) {
					this.getValues().add(new ValidValue(item.toString()));
				}
			} else if (type.equals(Boolean.class)) {
				// 布尔值
				this.getValues().add(new ValidValue(Boolean.TRUE.toString()));
				this.getValues().add(new ValidValue(Boolean.FALSE.toString()));
			} else if (ValidValuesGetter.class.isAssignableFrom(type)) {
				// 可选值获取者
				try {
					Object object = type.newInstance();
					if (object instanceof ValidValuesGetter) {
						ValidValuesGetter getter = (ValidValuesGetter) object;
						for (ValidValue validValue : getter.get()) {
							this.getValues().add(validValue);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int size() {
		return this.getValues().size();
	}

	public boolean isEmpty() {
		return this.getValues().isEmpty();
	}

	public boolean contains(Object o) {
		return this.getValues().contains(o);
	}

	public boolean add(ValidValue e) {
		return this.getValues().add(e);
	}

	public boolean remove(Object o) {
		return this.getValues().remove(o);
	}

	public ValidValue get(int index) {
		return this.getValues().get(index);
	}

	public ValidValue set(int index, ValidValue element) {
		return this.getValues().set(index, element);
	}

	public ValidValue remove(int index) {
		return this.getValues().remove(index);
	}

	@Override
	public Iterator<ValidValue> iterator() {
		return this.getValues().listIterator();
	}

	public ValidValue[] toArray() {
		return this.getValues().toArray(new ValidValue[] {});
	}
}
