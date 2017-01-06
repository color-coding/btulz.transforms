package org.colorcoding.tools.btulz.util;

import java.util.Objects;
import java.util.function.Predicate;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 集合数组
 * 
 * @author Niuren.Zhu
 *
 * @param <E>
 *            集合类型
 */
@XmlType(name = "ArrayList", namespace = Environment.NAMESPACE_BTULZ_UTIL)
@XmlRootElement(name = "ArrayList", namespace = Environment.NAMESPACE_BTULZ_UTIL)
public class ArrayList<E> extends java.util.ArrayList<E> implements List<E> {

	private static final long serialVersionUID = 1L;

	public E firstOrDefault() {
		if (this.size() > 0)
			return this.get(0);
		return null;
	}

	public E lastOrDefault() {
		if (this.size() > 0)
			return this.get(this.size() - 1);
		return null;
	}

	public E firstOrDefault(Predicate<? super E> filter) {
		Objects.requireNonNull(filter);
		for (int i = 0; i < this.size(); i++) {
			E item = this.get(i);
			if (item == null)
				continue;
			if (filter.test(item))
				return item;
		}
		return null;
	}

	public E lastOrfault(Predicate<? super E> filter) {
		Objects.requireNonNull(filter);
		for (int i = this.size() - 1; i >= 0; i--) {
			E item = this.get(i);
			if (item == null)
				continue;
			if (filter.test(item))
				return item;
		}
		return null;
	}

	public boolean addAll(E[] c) {
		if (c != null) {
			for (E item : c) {
				this.add(item);
			}
		}
		return true;
	}

	public boolean addAll(Iterable<? extends E> c) {
		if (c != null) {
			for (E item : c) {
				this.add(item);
			}
		}
		return true;
	}
}
