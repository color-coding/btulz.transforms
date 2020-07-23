package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.util.ConditionJudger;
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

	private OutputMappingList outputMappings;

	public final OutputMappingList getOutputMappings() {
		if (this.outputMappings == null) {
			this.setOutputMappings(new OutputMappingList());
		}
		return outputMappings;
	}

	private final void setOutputMappings(OutputMappingList outputMappings) {
		this.outputMappings = outputMappings;
	}

	@SuppressWarnings("unchecked")
	public final void addOutputMappings(Parameter parameter) {
		if (parameter == null) {
			return;
		}
		Object value = parameter.getValue();
		if (Iterable.class.isInstance(value)) {
			this.getOutputMappings().addAll((Iterable<OutputMapping>) value);
		}
	}

	public String output(String type) {
		OutputMapping opMapping = this.getOutputMappings()
				.firstOrDefault(c -> this.getClass().getSimpleName().equalsIgnoreCase(c.getBinding())
						&& c.getName().equalsIgnoreCase(type));
		if (opMapping != null) {
			for (OutputItem opItem : opMapping.getItems()) {
				ConditionJudger judger = new ConditionJudger(opItem.getConditions());
				if (judger.judge(this)) {
					return opItem.getContent();
				}
			}
			if (opMapping.getUnmatched() == UnmatchedHandling.EXCEPTION) {
				throw new RuntimeException(String.format("%s not have output type %s.", this.toString(), type));
			} else if (opMapping.getUnmatched() == UnmatchedHandling.SKIP) {
				return "";
			}
		}
		throw new RuntimeException(String.format("%s not defined output type %s.", this.toString(), type));
	}
}
