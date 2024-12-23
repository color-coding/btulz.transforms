package org.colorcoding.tools.btulz.bobas.transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.common.SqlQuery;
import org.colorcoding.ibas.bobas.data.IDataTable;
import org.colorcoding.ibas.bobas.data.IDataTableRow;
import org.colorcoding.ibas.bobas.repository.BORepository4DbReadonly;
import org.colorcoding.tools.btulz.bobas.Environment;
import org.colorcoding.tools.btulz.transformer.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RoutingTransformer extends Transformer {

	private String configFile;

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	private String dataUrl;

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	private String viewUrl;

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	private String outFile;

	public String getOutFile() {
		if (this.outFile == null || this.outFile.isEmpty()) {
			this.outFile = MyConfiguration.getWorkFolder() + "/service_routiong.xml";
		}
		return outFile;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public void transform() throws Exception {
		// 检查配置文件
		if (this.getConfigFile() == null || this.getConfigFile().isEmpty()
				|| !new File(this.getConfigFile()).isFile()) {
			throw new IOException("not found config file.");
		}
		// 检查输出目录
		File file = new File(this.getOutFile()).getParentFile();
		if (!file.exists()) {
			file.mkdirs();
		}
		// 读取配置文件
		MyConfiguration.create(this.getConfigFile());
		if (this.getQuery() == null || this.getQuery().isEmpty()) {
			throw new Exception("invaild query.");
		}
		BORepository4DbReadonly boRepository = new BORepository4DbReadonly("Master");
		IOperationResult<IDataTable> opRslt = boRepository
				.query(new SqlQuery(MyConfiguration.applyVariables(this.getQuery())));
		if (opRslt.getError() != null) {
			throw opRslt.getError();
		}
		IDataTable table = opRslt.getResultObjects().firstOrDefault();
		if (table == null) {
			throw new Exception("not query data table.");
		}
		Map<String, String> modules = new LinkedHashMap<String, String>();
		for (IDataTableRow row : table.getRows()) {
			modules.put(String.valueOf(row.getValue("ModuleId")), String.valueOf(row.getValue("ModuleName")));
		}

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		// 领域模型
		Element root = document.createElementNS("http://colorcoding.org/ibas/initialfantasy/service", "ServiceRouting");
		root.setPrefix("ns");
		for (Entry<String, String> entry : modules.entrySet()) {
			Element module = document.createElement("ServiceInformation");
			// id
			Element element = document.createElement("Id");
			element.setTextContent(entry.getKey());
			module.appendChild(element);
			// name
			element = document.createElement("Name");
			element.setTextContent(entry.getValue());
			module.appendChild(element);
			MyConfiguration.addConfigValue("ModuleId", entry.getKey());
			MyConfiguration.addConfigValue("ModuleName", entry.getValue());
			// providers
			Element providers = document.createElement("Providers");
			// data provider
			Element provider = document.createElement("Provider");
			// address
			element = document.createElement("Address");
			element.setTextContent(MyConfiguration.applyVariables(this.getDataUrl()).toLowerCase());
			provider.appendChild(element);
			// enabled
			element = document.createElement("Enabled");
			element.setTextContent("true");
			provider.appendChild(element);
			// type
			element = document.createElement("Type");
			element.setTextContent("DATA");
			provider.appendChild(element);

			providers.appendChild(provider);

			// view provider
			provider = document.createElement("Provider");
			// address
			element = document.createElement("Address");
			element.setTextContent(MyConfiguration.applyVariables(this.getViewUrl()).toLowerCase());
			provider.appendChild(element);
			// enabled
			element = document.createElement("Enabled");
			element.setTextContent("true");
			provider.appendChild(element);
			// type
			element = document.createElement("Type");
			element.setTextContent("VIEW");
			provider.appendChild(element);

			providers.appendChild(provider);

			module.appendChild(providers);
			MyConfiguration.addConfigValue("ModuleId", null);
			MyConfiguration.addConfigValue("ModuleName", null);
			root.appendChild(module);
		}
		document.appendChild(root);
		// 将xml写到文件中
		javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(document);
		// 添加xml 头信息
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(this.getOutFile()))) {
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			writer.flush();
		}

		Environment.getLogger().info(String.format("out file [%s].", this.getOutFile()));
	}

}
