package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * xml文件和领域模型的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class XmlTransformer extends FileTransformer {

	public static final String FILE_EXTENSION = ".xml";

	@Override
	protected IDomain[] load(File file) {
		ArrayList<IDomain> domains = new ArrayList<>();
		if (file != null && file.isFile() && file.getName().endsWith(FILE_EXTENSION)) {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				// 将xml文件解析
				Document document = db.parse(file);
				// 获得所有节点，递归遍历节点
				NodeList employees = document.getChildNodes();
				for (int i = 0; i < employees.getLength(); i++) {
					// 取得一个节点
					Node employee = employees.item(i);
					NodeList employeeInfo = employee.getChildNodes();
					for (int j = 0; j < employeeInfo.getLength(); j++) {
						Node node = employeeInfo.item(j);
						NodeList employeeMeta = node.getChildNodes();
						for (int k = 0; k < employeeMeta.getLength(); k++) {
							System.out.println(
									employeeMeta.item(k).getNodeName() + ":" + employeeMeta.item(k).getTextContent());
						}
					}
				}
				System.out.println("解析完毕");
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (ParserConfigurationException e) {
				System.out.println(e.getMessage());
			} catch (SAXException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
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
		Element root = document.createElement("Domain");
		root.setAttribute("Name", domain.getName());
		root.setAttribute("ShortName", domain.getShortName());
		// 模型
		for (IModel model : domain.getModels()) {
			Element modelElement = document.createElement("Model");
			modelElement.setAttribute("Name", model.getName());
			modelElement.setAttribute("Description", model.getDescription());
			modelElement.setAttribute("Mapped", model.getMapped());
			modelElement.setAttribute("Type", String.valueOf(model.getModelType()));
			if (model.isEntity() == emYesNo.No) {
				modelElement.setAttribute("Entity", String.valueOf(model.isEntity()));
			}
			// 模型属性
			for (IProperty property : model.getProperties()) {
				Element propertyElement = document.createElement("Property");
				propertyElement.setAttribute("Name", property.getName());
				propertyElement.setAttribute("Description", property.getDescription());
				propertyElement.setAttribute("DataType", String.valueOf(property.getDataType()));
				propertyElement.setAttribute("DataSubType", String.valueOf(property.getDataSubType()));
				propertyElement.setAttribute("EditSize", String.valueOf(property.getEditSize()));
				propertyElement.setAttribute("Mapped", property.getMapped());
				if (property.isPrimaryKey() == emYesNo.Yes) {
					propertyElement.setAttribute("PrimaryKey", String.valueOf(property.isPrimaryKey()));
				}
				if (property.isUniqueKey() == emYesNo.Yes) {
					propertyElement.setAttribute("UniqueKey", String.valueOf(property.isUniqueKey()));
				}
				modelElement.appendChild(propertyElement);
			}
			root.appendChild(modelElement);
		}
		// 业务对象
		for (IBusinessObject businessObject : domain.getBusinessObjects()) {
			Element boElement = document.createElement("BusinessObject");
			if (businessObject.getName().equals(businessObject.getMappedModel())) {
				boElement.setAttribute("MappedModel", businessObject.getMappedModel());
				boElement.setAttribute("ShortName", businessObject.getShortName());
			} else {
				boElement.setAttribute("Name", businessObject.getName());
				boElement.setAttribute("Description", businessObject.getDescription());
				boElement.setAttribute("ShortName", businessObject.getShortName());
				boElement.setAttribute("ShortName", businessObject.getShortName());
				boElement.setAttribute("MappedModel", businessObject.getMappedModel());
			}
			for (IBusinessObjectItem boItem : businessObject.getRelatedBOs()) {
				Element biElement = document.createElement("RelatedBO");
				biElement.setAttribute("Relation", String.valueOf(boItem.getRelation()));
				if (boItem.getName().equals(boItem.getMappedModel())) {
					biElement.setAttribute("MappedModel", boItem.getMappedModel());
					if (boItem.getShortName() != null && !boItem.getShortName().equals(businessObject.getShortName())) {
						biElement.setAttribute("ShortName", boItem.getShortName());
					}
				} else {
					biElement.setAttribute("Name", boItem.getName());
					biElement.setAttribute("Description", boItem.getDescription());
					if (boItem.getShortName() != null && !boItem.getShortName().equals(businessObject.getShortName())) {
						biElement.setAttribute("ShortName", boItem.getShortName());
					}
					biElement.setAttribute("MappedModel", boItem.getMappedModel());
				}
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

}
