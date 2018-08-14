package org.colorcoding.tools.btulz.transformer;

import java.io.File;
import java.io.FileWriter;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.data.emYesNo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * xml文件和领域模型的转换器
 * 
 * 使用DOM4J解析，保存时顺序不乱
 * 
 * @author Niuren.Zhu
 *
 */
public class XmlTransformerDom4j extends XmlTransformer {

	@Override
	protected void save(File outFolder, IDomain domain) throws Exception {
		String fileName = this.getSaveFilePath(outFolder.getPath(), domain);
		Document document = DocumentHelper.createDocument();
		// 领域模型
		Element domainElement = document.addElement("Domain");
		this.writeElement(domain, domainElement);
		// 模型
		for (IModel model : domain.getModels()) {
			Element modelElement = domainElement.addElement("Model");
			this.writeElement(model, modelElement);
			// 模型属性
			for (IProperty property : model.getProperties()) {
				Element propertyElement = modelElement.addElement("Property");
				this.writeElement(property, propertyElement);
			}
		}
		// 业务对象
		for (IBusinessObject businessObject : domain.getBusinessObjects()) {
			Element boElement = domainElement.addElement("BusinessObject");
			this.writeElement(businessObject, boElement);
			for (IBusinessObjectItem boItem : businessObject.getRelatedBOs()) {
				Element biElement = boElement.addElement("RelatedBO");
				this.writeElement(boItem, biElement);
			}
		}
		OutputFormat xmlFormat = OutputFormat.createCompactFormat();
		xmlFormat.setEncoding(XML_FILE_ENCODING);
		xmlFormat.setNewlines(true);
		xmlFormat.setIndent(true);
		xmlFormat.setIndent("  ");
		XMLWriter writer = new XMLWriter(new FileWriter(fileName), xmlFormat);
		writer.write(document);
		writer.close();
	}

	private void writeElement(IDomain domain, Element element) {
		element.addAttribute("Name", domain.getName());
		element.addAttribute("ShortName", domain.getShortName());
	}

	private void writeElement(IModel model, Element element) {
		element.addAttribute("Name", model.getName());
		element.addAttribute("Description", model.getDescription());
		element.addAttribute("ModelType", String.valueOf(model.getModelType()));
		element.addAttribute("Mapped", model.getMapped());
		if (!model.isEntity()) {
			element.addAttribute("Entity", String.valueOf(emYesNo.valueOf(model.isEntity())));
		}
	}

	private void writeElement(IProperty property, Element element) {
		element.addAttribute("Name", property.getName());
		element.addAttribute("Description", property.getDescription());
		element.addAttribute("DataType", String.valueOf(property.getDataType()));
		element.addAttribute("DataSubType", String.valueOf(property.getDataSubType()));
		element.addAttribute("EditSize", String.valueOf(property.getEditSize()));
		if (property.getDeclaredType() != null) {
			element.addAttribute("DeclaredType", String.valueOf(property.getDeclaredType()));
		}
		element.addAttribute("Mapped", property.getMapped());
		if (property.isPrimaryKey()) {
			element.addAttribute("PrimaryKey", String.valueOf(emYesNo.valueOf(property.isPrimaryKey())));
		}
		if (property.isUniqueKey()) {
			element.addAttribute("UniqueKey", String.valueOf(emYesNo.valueOf(property.isUniqueKey())));
		}
		if (property.isSearchKey()) {
			element.addAttribute("SearchKey", String.valueOf(emYesNo.valueOf(property.isSearchKey())));
		}
		if (property.getLinked() != null) {
			element.addAttribute("Linked", property.getLinked());
		}
	}

	private void writeElement(IBusinessObject bo, Element element) {
		if (bo.getName() != null && bo.getName().equals(bo.getMappedModel())) {
			element.addAttribute("MappedModel", bo.getMappedModel());
			element.addAttribute("ShortName", bo.getShortName());
		} else {
			element.addAttribute("Name", bo.getName());
			element.addAttribute("Description", bo.getDescription());
			element.addAttribute("ShortName", bo.getShortName());
			element.addAttribute("ShortName", bo.getShortName());
			element.addAttribute("MappedModel", bo.getMappedModel());
		}
	}

	private void writeElement(IBusinessObjectItem boItem, Element element) {
		element.addAttribute("Relation", String.valueOf(boItem.getRelation()));
		if (boItem.getName() != null && boItem.getName().equals(boItem.getMappedModel())) {
			element.addAttribute("MappedModel", boItem.getMappedModel());
			if (boItem.getShortName() != null && !boItem.getShortName().isEmpty()) {
				element.addAttribute("ShortName", boItem.getShortName());
			}
		} else {
			element.addAttribute("Name", boItem.getName());
			element.addAttribute("Description", boItem.getDescription());
			if (boItem.getShortName() != null && !boItem.getShortName().isEmpty()) {
				element.addAttribute("ShortName", boItem.getShortName());
			}
			element.addAttribute("MappedModel", boItem.getMappedModel());
		}
		for (IBusinessObjectItem item : boItem.getRelatedBOs()) {
			Element biElement = element.addElement("RelatedBO");
			this.writeElement(item, biElement);
			element.appendContent(biElement);
		}
	}
}
