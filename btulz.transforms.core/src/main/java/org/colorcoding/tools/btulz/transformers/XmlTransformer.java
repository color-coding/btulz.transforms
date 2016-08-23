package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.tools.btulz.models.IDomain;
import org.w3c.dom.Document;
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

	@Override
	protected IDomain[] load(File file) {
		ArrayList<IDomain> domains = new ArrayList<>();
		if (file != null && file.isFile() && file.getName().endsWith(".xml")) {
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
	protected void save(File outFolder, IDomain domain) {
		// TODO Auto-generated method stub

	}

}
