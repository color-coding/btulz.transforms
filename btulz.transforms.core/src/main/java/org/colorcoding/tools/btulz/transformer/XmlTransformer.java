package org.colorcoding.tools.btulz.transformer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IIndex;
import org.colorcoding.tools.btulz.model.IIndexProperty;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.data.emYesNo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * xml文件和领域模型的转换器
 * 
 * 使用DOM读取，StAX按指定顺序写入元素和属性
 * 
 * @author Niuren.Zhu
 *
 */
public class XmlTransformer extends FileTransformer {

	public static final String XML_FILE_EXTENSION = ".xml";
	public static final String XML_FILE_ENCODING = "utf-8";
	public static final String XML_FILE_INDENT = "yes";

	protected IXmlParser createXmlParser(String sign) {
		if (sign.equals("DomainModel")) {
			return new XmlParser1();// 旧版解释器
		} else if (sign.equals("Domain")) {
			return new XmlParser();// 默认解释器
		}
		return null;
	}

	public final void load(InputStream inputStream) throws Exception {
		if (inputStream == null) {
			return;
		}
		this.clearResults();
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
		IDomain[] tmpDomains = this.load(document);
		if (tmpDomains != null) {
			for (IDomain domain : tmpDomains) {
				this.getDomainModels().add(domain);
			}
		}
		// 如果有错误，则抛出错误
		if (!this.isInterruptOnError() && this.getErrors().length > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Exception exception : this.getErrors()) {
				stringBuilder.append(exception.getMessage());
			}
			throw new MultiTransformException(stringBuilder.toString(), this.getErrors());
		}
	}

	@Override
	protected IDomain[] load(File file) throws Exception {
		if (file != null && file.isFile() && file.getName().endsWith(XML_FILE_EXTENSION)) {
			Environment.getLogger().info(String.format("load file [%s]'s data.", file.getName()));
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			return this.load(document);
		}
		return new IDomain[] {};
	}

	private IDomain[] load(Document document) throws Exception {
		ArrayList<IDomain> domains = new ArrayList<>();
		IXmlParser xmlParser = null;
		Environment.getLogger().info(String.format("begin transform xml document to domain."));
		for (int i = 0; i < document.getChildNodes().getLength(); i++) {
			Node firstNode = document.getChildNodes().item(i);
			if (firstNode != null && firstNode.getNodeType() == Node.ELEMENT_NODE) {
				xmlParser = this.createXmlParser(firstNode.getNodeName());
				if (xmlParser != null) {
					IDomain domain = xmlParser.parse(firstNode);
					if (domain != null) {
						domains.add(domain);
					}
				}
			}
		}
		Environment.getLogger().info(String.format("end transform domain."));
		return domains.toArray(new IDomain[] {});
	}

	protected String getSaveFilePath(String outFolder, IDomain domain) {
		// 生成文件名称 DS_DN_DataInputTemplate.xml
		String name = null;
		String shortName = null;
		if (domain.getShortName() != null && !domain.getShortName().isEmpty()) {
			shortName = domain.getShortName();
		}
		if (this.isGroupingFile() && domain.getBusinessObjects().size() == 1) {
			name = domain.getBusinessObjects().get(0).getName();
			if (name == null || name.isEmpty()) {
				name = domain.getBusinessObjects().get(0).getMappedModel();
			}
		}
		if ((name == null || name.isEmpty() || name.equals("null")) && domain.getName() != null
				&& !domain.getName().isEmpty()) {
			name = domain.getName();
		}
		if (name == null || name.isEmpty() || name.equals("null")) {
			name = "unspecified";
		}
		String fileName = outFolder + File.separator + String
				.format("ds_%s%s%s", shortName == null ? "" : shortName + "_", name, XML_FILE_EXTENSION).toLowerCase();
		return fileName;
	}

	@Override
	protected void save(File outFolder, IDomain domain) throws Exception {
		String fileName = this.getSaveFilePath(outFolder.getPath(), domain);
		Environment.getLogger().info(String.format("begin transform domain to file [%s].", fileName));
		try (OutputStream out = Files.newOutputStream(new File(fileName).toPath())) {
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out, XML_FILE_ENCODING);
			try {
				writer.writeStartDocument(XML_FILE_ENCODING, "1.0");
				this.writeStartElement(writer, "Domain", 0, false);
				this.writeElement(domain, writer);
				for (IModel model : domain.getModels()) {
					boolean emptyModel = model.getProperties().isEmpty() && model.getIndexes().isEmpty();
					this.writeStartElement(writer, "Model", 1, emptyModel);
					this.writeElement(model, writer);
					for (IProperty property : model.getProperties()) {
						this.writeStartElement(writer, "Property", 2, true);
						this.writeElement(property, writer);
					}
					for (IIndex index : model.getIndexes()) {
						boolean emptyIndex = index.getIndexProperties().isEmpty();
						this.writeStartElement(writer, "Index", 2, emptyIndex);
						this.writeElement(index, writer);
						for (IIndexProperty indexProperty : index.getIndexProperties()) {
							this.writeStartElement(writer, "IndexProperty", 3, true);
							this.writeElement(indexProperty, writer);
						}
						if (!emptyIndex) {
							this.writeEndElement(writer, 2);
						}
					}
					if (!emptyModel) {
						this.writeEndElement(writer, 1);
					}
				}
				for (IBusinessObject businessObject : domain.getBusinessObjects()) {
					boolean empty = businessObject.getRelatedBOs().isEmpty();
					this.writeStartElement(writer, "BusinessObject", 1, empty);
					this.writeElement(businessObject, writer);
					for (IBusinessObjectItem item : businessObject.getRelatedBOs()) {
						this.writeRelatedBO(writer, item, 2);
					}
					if (!empty) {
						this.writeEndElement(writer, 1);
					}
				}
				this.writeEndElement(writer, 0);
				writer.writeCharacters("\n");
				writer.writeEndDocument();
				writer.flush();
			} finally {
				writer.close();
			}
		}
		Environment.getLogger().info("end transform domain to file.");
	}

	private void writeRelatedBO(XMLStreamWriter writer, IBusinessObjectItem item, int depth) throws XMLStreamException {
		boolean empty = item.getRelatedBOs().isEmpty();
		this.writeStartElement(writer, "RelatedBO", depth, empty);
		this.writeElement(item, writer);
		for (IBusinessObjectItem child : item.getRelatedBOs()) {
			this.writeRelatedBO(writer, child, depth + 1);
		}
		if (!empty) {
			this.writeEndElement(writer, depth);
		}
	}

	private void writeIndent(XMLStreamWriter writer, int depth) throws XMLStreamException {
		if ("yes".equals(XML_FILE_INDENT)) {
			writer.writeCharacters("\n" + "  ".repeat(depth));
		}
	}

	private void writeStartElement(XMLStreamWriter writer, String name, int depth, boolean empty) throws XMLStreamException {
		this.writeIndent(writer, depth);
		if (empty) {
			writer.writeEmptyElement(name);
		} else {
			writer.writeStartElement(name);
		}
	}

	private void writeEndElement(XMLStreamWriter writer, int depth) throws XMLStreamException {
		this.writeIndent(writer, depth);
		writer.writeEndElement();
	}

	private void writeAttribute(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
		// 与原保存行为一致：省略null属性，保留空字符串属性。
		if (value != null) {
			writer.writeAttribute(name, value);
		}
	}

	private void writeElement(IDomain domain, XMLStreamWriter writer) throws XMLStreamException {
		this.writeAttribute(writer, "Name", domain.getName());
		this.writeAttribute(writer, "ShortName", domain.getShortName());
	}

	private void writeElement(IModel model, XMLStreamWriter writer) throws XMLStreamException {
		this.writeAttribute(writer, "Name", model.getName());
		this.writeAttribute(writer, "Description", model.getDescription());
		this.writeAttribute(writer, "ModelType", String.valueOf(model.getModelType()));
		this.writeAttribute(writer, "Mapped", model.getMapped());
		if (!model.isEntity()) {
			this.writeAttribute(writer, "Entity", String.valueOf(emYesNo.valueOf(model.isEntity())));
		}
	}

	private void writeElement(IProperty property, XMLStreamWriter writer) throws XMLStreamException {
		this.writeAttribute(writer, "Name", property.getName());
		this.writeAttribute(writer, "Description", property.getDescription());
		this.writeAttribute(writer, "DataType", String.valueOf(property.getDataType()));
		this.writeAttribute(writer, "DataSubType", String.valueOf(property.getDataSubType()));
		this.writeAttribute(writer, "EditSize", String.valueOf(property.getEditSize()));
		if (property.getDeclaredType() != null && !property.getDeclaredType().isEmpty()) {
			this.writeAttribute(writer, "DeclaredType", String.valueOf(property.getDeclaredType()));
		}
		this.writeAttribute(writer, "Mapped", property.getMapped());
		if (property.isPrimaryKey()) {
			this.writeAttribute(writer, "PrimaryKey", String.valueOf(emYesNo.valueOf(property.isPrimaryKey())));
		}
		if (property.isUniqueKey()) {
			this.writeAttribute(writer, "UniqueKey", String.valueOf(emYesNo.valueOf(property.isUniqueKey())));
		}
		if (property.isSearchKey()) {
			this.writeAttribute(writer, "SearchKey", String.valueOf(emYesNo.valueOf(property.isSearchKey())));
		}
		if (property.getLinked() != null && !property.getLinked().isEmpty()) {
			this.writeAttribute(writer, "Linked", property.getLinked());
		}
		if (property.getDefaultValue() != null && !property.getDefaultValue().isEmpty()) {
			this.writeAttribute(writer, "DefaultValue", property.getDefaultValue());
		}
	}

	private void writeElement(IIndex index, XMLStreamWriter writer) throws XMLStreamException {
		if (index.getName() != null && !index.getName().isEmpty()) {
			this.writeAttribute(writer, "Name", index.getName());
		}
		if (index.getShortName() != null && !index.getShortName().isEmpty()) {
			this.writeAttribute(writer, "ShortName", index.getShortName());
		}
		if (index.getDescription() != null && !index.getDescription().isEmpty()) {
			this.writeAttribute(writer, "Description", index.getDescription());
		}
		if (index.getIndexType() != null) {
			this.writeAttribute(writer, "IndexType", String.valueOf(index.getIndexType()));
		}
	}

	private void writeElement(IIndexProperty indexProperty, XMLStreamWriter writer) throws XMLStreamException {
		this.writeAttribute(writer, "Name", indexProperty.getName());
		String direction = indexProperty.getDirection();
		if (direction != null && !direction.isEmpty() && !"ASC".equalsIgnoreCase(direction)) {
			this.writeAttribute(writer, "Direction", direction);
		}
	}

	private void writeElement(IBusinessObject bo, XMLStreamWriter writer) throws XMLStreamException {
		if (bo.getName() != null && bo.getName().equals(bo.getMappedModel())) {
			this.writeAttribute(writer, "MappedModel", bo.getMappedModel());
			this.writeAttribute(writer, "ShortName", bo.getShortName());
		} else {
			this.writeAttribute(writer, "Name", bo.getName());
			this.writeAttribute(writer, "Description", bo.getDescription());
			this.writeAttribute(writer, "ShortName", bo.getShortName());
			this.writeAttribute(writer, "MappedModel", bo.getMappedModel());
		}
	}

	private void writeElement(IBusinessObjectItem boItem, XMLStreamWriter writer) throws XMLStreamException {
		this.writeAttribute(writer, "Relation", String.valueOf(boItem.getRelation()));
		if (boItem.getName() != null && boItem.getName().equals(boItem.getMappedModel())) {
			this.writeAttribute(writer, "MappedModel", boItem.getMappedModel());
			if (boItem.getShortName() != null && !boItem.getShortName().isEmpty()) {
				this.writeAttribute(writer, "ShortName", boItem.getShortName());
			}
		} else {
			this.writeAttribute(writer, "Name", boItem.getName());
			this.writeAttribute(writer, "Description", boItem.getDescription());
			if (boItem.getShortName() != null && !boItem.getShortName().isEmpty()) {
				this.writeAttribute(writer, "ShortName", boItem.getShortName());
			}
			this.writeAttribute(writer, "MappedModel", boItem.getMappedModel());
		}
	}

	@Override
	public void transform() throws Exception {
	}
}
