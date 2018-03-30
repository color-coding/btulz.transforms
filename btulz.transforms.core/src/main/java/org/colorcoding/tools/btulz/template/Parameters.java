package org.colorcoding.tools.btulz.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 参数集合，自动去重
 * 
 * @author Niuren.Zhu
 *
 */
public class Parameters extends ArrayList<Parameter> {

	private static final long serialVersionUID = 9071904351211197657L;

	public Parameters() {
		super();
	}

	public Parameters(Collection<? extends Parameter> c) {
		super(c);
	}

	public Parameters(List<? extends Parameter> c) {
		super(c);
	}

	public Parameters(Parameters c) {
		super(c);
	}

	@Override
	public boolean add(Parameter e) {
		if (e == null || e.getName() == null) {
			return false;
		}
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).equals(e)) {
				this.set(i, e);
				return true;
			}
		}
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Parameter> c) {
		for (Parameter parameter : c) {
			this.add(parameter);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, Class<T> type) {
		for (Parameter parameter : this) {
			if (parameter.getName().equalsIgnoreCase(name)) {
				if (type.isInstance(parameter.getValue())) {
					return (T) parameter.getValue();
				}
			}
		}
		return null;
	}

	public Parameter get(String name) {
		for (Parameter parameter : this) {
			if (parameter.getName().equalsIgnoreCase(name)) {
				return parameter;
			}
		}
		return null;
	}
}
