package org.colorcoding.tools.btulz.shell.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 从资源中读取可选值及其数据特性。
 * <p>
 * 定义是可选值的一种，但定义集本身不是 {@link ValidValues}，避免将“规则来源”
 * 与“命令项的可选值”混为同一层级。
 */
public class ResourceDefinitions {
	private final Map<String, Definition> definitions = new LinkedHashMap<>();

	public static ResourceDefinitions loadDefinitions(String source) {
		return loadDefinitions(source, null);
	}

	public static ResourceDefinitions loadDefinitions(String source, Function<String, InputStream> resourceLoader) {
		ResourceDefinitions result = new ResourceDefinitions();
		result.load(source, resourceLoader);
		return result;
	}

	private void load(String source, Function<String, InputStream> resourceLoader) {
		try (InputStream input = open(source, resourceLoader)) {
			if (input == null) {
				throw new IllegalArgumentException("value definitions not found [" + source + "].");
			}
			this.definitions.clear();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			Element root = factory.newDocumentBuilder().parse(input).getDocumentElement();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) continue;
				Element element = (Element) node;
				String name = element.getAttribute("Name");
				if (name == null || name.isBlank()) continue;
				Definition definition = new Definition(name, element.getAttribute("Description"));
				NodeList properties = element.getChildNodes();
				for (int j = 0; j < properties.getLength(); j++) {
					Node propertyNode = properties.item(j);
					if (propertyNode.getNodeType() != Node.ELEMENT_NODE) continue;
					Element property = (Element) propertyNode;
					String key = property.getAttribute("Key");
					if (key != null && !key.isBlank()) definition.put(key, property.getAttribute("Value"));
				}
				this.definitions.put(name.toUpperCase(), definition);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid value definitions [" + source + "].", e);
		}
	}

	private static InputStream open(String source, Function<String, InputStream> resourceLoader) throws Exception {
		File file = new File(source);
		if (file.isFile()) {
			return new FileInputStream(file);
		}
		if (resourceLoader != null) {
			InputStream input = resourceLoader.apply(source);
			if (input != null) {
				return input;
			}
		}
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(source);
	}

	public Definition find(String name) {
		return name == null ? null : definitions.get(name.toUpperCase());
	}

	public List<Definition> getDefinitions() {
		return Collections.unmodifiableList(new ArrayList<>(definitions.values()));
	}

	public static class Definition extends ValidValue {
		private final Map<String, String> values = new LinkedHashMap<>();
		public Definition() {
		}

		private Definition(String name, String description) {
			super(name, description == null || description.isBlank() ? name : description);
		}

		private void put(String key, String value) {
			values.put(key, value);
		}

		public String get(String key) {
			for (Map.Entry<String, String> item : values.entrySet()) {
				if (item.getKey().equalsIgnoreCase(key)) {
					return item.getValue();
				}
			}
			return null;
		}

		public Map<String, String> getValues() {
			return Collections.unmodifiableMap(values);
		}
	}
}
