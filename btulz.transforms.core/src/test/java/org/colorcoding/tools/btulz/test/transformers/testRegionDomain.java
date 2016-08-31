package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.transformers.DataStructureOrchestration;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMapping;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMappings;

import junit.framework.TestCase;

public class testRegionDomain extends TestCase {

	public void testMSSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + testXmlTransformer.old_xml_path, true);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("db").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile + File.separator + "ds_mssql_ibas.xml");
			template.setOutPutFile(Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas.out.xml");
			ArrayList<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "localhost"));
			parameters.add(new Parameter("DbPort", "1433"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("DbSchema", "dbo"));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "sa"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			template.export(parameters);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller
					.unmarshal(new File(template.getOutPutFile()));
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}

	}

	public void testMYSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + testXmlTransformer.old_xml_path, true);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		DataTypeMappings dataTypeMappings = new DataTypeMappings();
		dataTypeMappings
				.add(new DataTypeMapping(emDataType.Memo, emDataSubType.Default, "text(${Property.getEditSize()})"));
		System.out.println(Serializer.toXmlString(dataTypeMappings, true));
		// 测试MYSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("db").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile + File.separator + "ds_mysql_ibas.xml");
			template.setOutPutFile(Environment.getWorkingFolder() + File.separator + "ds_mysql_ibas.out.xml");
			dataTypeMappings = DataTypeMappings
					.create(template.getTemplateFile().replace("ds_mysql_ibas.xml", "dm_mysql_ibas.xml"));
			ArrayList<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-ubuntu"));
			parameters.add(new Parameter("DbPort", "3306"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "root"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(DataTypeMapping.PARAMETER_NAME, dataTypeMappings));
			template.export(parameters);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller
					.unmarshal(new File(template.getOutPutFile()));
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}
}
