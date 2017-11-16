package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.util.NamingRules;

/**
 * 模型对象实体
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class Entity implements Cloneable {

	public abstract String getName();

	public String getName(String type) {
		return NamingRules.format(type, this.getName());
	}

	@Override
	public String toString() {
		return String.format("{%s: %s}", this.getClass().getSimpleName().toLowerCase(), this.getName());
	}

	@Override
	public Object clone() {
		throw new UnsupportedOperationException();
	}
}
