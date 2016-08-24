package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emYesNo;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * xml文件和领域模型的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class XmlTransformer extends FileTransformer {

	public static final String FILE_EXTENSION = ".xml";

	protected IXmlParser createXmlParser(String sign) {
		if (sign.equals("DomainModel")) {
			return new XmlParser1();// 旧版解释器
		} else if (sign.equals("Domain")) {
			return new XmlParser();// 默认解释器
		}
		return null;
	}

	@Override
	protected IDomain[] load(File file) throws ParserConfigurationException, IOException, SAXException {
		ArrayList<IDomain> domains = new ArrayList<>();
		if (file != null && file.isFile() && file.getName().endsWith(FILE_EXTENSION)) {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			IXmlParser xmlParser = null;
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

		}
		return domains.toArray(new IDomain[] {});
	}

	@Override
	protected void save(File outFolder, IDomain domain)
			throws ParserConfigurationException, FileNotFoundException, TransformerException {
		// 生成文件名称 DS_DN_DataInputTemplate.xml
		String name = "unspecified";
		String shortName = null;
		if (domain.getShortName() != null && !domain.getShortName().equals("")) {
			shortName = domain.getShortName();
		}
		if (domain.getName() != null && !domain.getName().equals("")) {
			name = domain.getName();
		}
		String fileName = outFolder.getPath() + File.separator + String
				.format("ds_%s%s%s", shortName == null ? "" : shortName + "_", name, FILE_EXTENSION).toLowerCase();
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Comment comment = document.createComment("by btulz transforms v0.0.1");
		document.appendChild(comment);
		// 领域模型
		// Element root =
		// document.createElementNS(Environment.NAMESPACE_BTULZ_TRANSFORMERS,
		// "Domain");
		Element root = document.createElement("Domain");
		this.writeElement(domain, root);
		// 模型
		for (IModel model : domain.getModels()) {
			Element modelElement = document.createElement("Model");
			this.writeElement(model, modelElement);
			// 模型属性
			for (IProperty property : model.getProperties()) {
				Element propertyElement = document.createElement("Property");
				this.writeElement(property, propertyElement);
				modelElement.appendChild(propertyElement);
			}
			root.appendChild(modelElement);
		}
		// 业务对象
		for (IBusinessObject businessObject : domain.getBusinessObjects()) {
			Element boElement = document.createElement("BusinessObject");
			this.writeElement(businessObject, boElement);
			for (IBusinessObjectItem boItem : businessObject.getRelatedBOs()) {
				Element biElement = document.createElement("RelatedBO");
				this.writeElement(boItem, biElement, document);
				boElement.appendChild(biElement);
			}
			root.appendChild(boElement);
		}
		document.appendChild(root);
		// 将xml写到文件中
		javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(document);
		// 添加xml 头信息
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		StreamResult result = new StreamResult(pw);
		transformer.transform(source, result);
	}

	protected void writeElement(IDomain domain, Element element) {
		element.setAttribute("Name", domain.getName());
		element.setAttribute("ShortName", domain.getShortName());
	}

	protected void writeElement(IModel model, Element element) {
		element.setAttribute("Name", model.getName());
		element.setAttribute("Description", model.getDescription());
		element.setAttribute("Mapped", model.getMapped());
		element.setAttribute("ModelType", String.valueOf(model.getModelType()));
		if (model.isEntity() == emYesNo.No) {
			element.setAttribute("Entity", String.valueOf(model.isEntity()));
		}
	}

	protected void writeElement(IProperty property, Element element) {
		element.setAttribute("Name", property.getName());
		element.setAttribute("Description", property.getDescription());
		element.setAttribute("DataType", String.valueOf(property.getDataType()));
		element.setAttribute("DataSubType", String.valueOf(property.getDataSubType()));
		element.setAttribute("EditSize", String.valueOf(property.getEditSize()));
		element.setAttribute("Mapped", property.getMapped());
		if (property.isPrimaryKey() == emYesNo.Yes) {
			element.setAttribute("PrimaryKey", String.valueOf(property.isPrimaryKey()));
		}
		if (property.isUniqueKey() == emYesNo.Yes) {
			element.setAttribute("UniqueKey", String.valueOf(property.isUniqueKey()));
		}
	}

	protected void writeElement(IBusinessObject bo, Element element) {
		if (bo.getName().equals(bo.getMappedModel())) {
			element.setAttribute("MappedModel", bo.getMappedModel());
			element.setAttribute("ShortName", bo.getShortName());
		} else {
			element.setAttribute("Name", bo.getName());
			element.setAttribute("Description", bo.getDescription());
			element.setAttribute("ShortName", bo.getShortName());
			element.setAttribute("ShortName", bo.getShortName());
			element.setAttribute("MappedModel", bo.getMappedModel());
		}
	}

	protected void writeElement(IBusinessObjectItem boItem, Element element, Document document) {
		element.setAttribute("Relation", String.valueOf(boItem.getRelation()));
		if (boItem.getName().equals(boItem.getMappedModel())) {
			element.setAttribute("MappedModel", boItem.getMappedModel());
			if (boItem.getShortName() != null && !boItem.getShortName().equals("")) {
				element.setAttribute("ShortName", boItem.getShortName());
			}
		} else {
			element.setAttribute("Name", boItem.getName());
			element.setAttribute("Description", boItem.getDescription());
			if (boItem.getShortName() != null && !boItem.getShortName().equals("")) {
				element.setAttribute("ShortName", boItem.getShortName());
			}
			element.setAttribute("MappedModel", boItem.getMappedModel());
		}
		if (document != null) {
			for (IBusinessObjectItem item : boItem.getRelatedBOs()) {
				Element biElement = document.createElement("RelatedBO");
				this.writeElement(item, biElement);
				element.appendChild(biElement);
			}
		}
	}
}
